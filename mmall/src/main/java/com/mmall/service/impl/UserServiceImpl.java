package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by yeleichao on 2017-6-22.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0) {
          return ServerResponse.createByErrorMessage("用户名不存在");
        }

        // todo 密码登录md5加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username, md5Password);
        if(user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }


    /**
     * 用户注册
     * @param user
     * @return
     */
    public ServerResponse<String> register(User user) {
        ServerResponse serverResponse = this.checkVaild(user.getUsername(),Const.USERNAME);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        serverResponse = this.checkVaild(user.getEmail(), Const.EMAIL);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        user.setRole(Const.role.ROLE_CUSTOMER);//默认为普通用户
        //md5加密
        String md5Password = MD5Util.MD5EncodeUtf8(user.getPassword());
        user.setPassword(md5Password);

        int resultCount = userMapper.insert(user);
        if(resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }


    /**
     * 校验 用户名 与 邮箱 是否已被注册
     * @param value 用户名或者邮箱的值
     * @param type 判断是用户名还是邮箱
     * @return
     */
    public ServerResponse<String> checkVaild(String value, String type) {
        if(StringUtils.isNotBlank(type)) {
            //开始校验
            if(Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(value);
                if(resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(value);
                if(resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已被注册");
                }
            }

            return ServerResponse.createBySuccessMessage("校验成功");
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
    }

    /**
     * 将用户的问题返回
     * @param username
     * @return
     */
    public ServerResponse selectQuestion(String username) {
        ServerResponse vaildResponse = this.checkVaild(username, Const.USERNAME);
        if(vaildResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    /**
     * 获取问题，并将token放入本地缓存
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount > 0) {
            //用户回答正确
            String forgetToken = UUID.randomUUID().toString();//UUID
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }

        return ServerResponse.createByErrorMessage("问题回答错误!");
     }


    /**
     * 忘记密码状态下用户重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse vaildResponse = this.checkVaild(username, Const.USERNAME);
        if(vaildResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //从本地缓存中得到token
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        //判断两个token是否相同
        if(org.apache.commons.lang3.StringUtils.equals(token, forgetToken)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);

            if(resultCount > 0) {
                return ServerResponse.createByErrorMessage("修改密码成功");
            }

        }else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取");
        }

        return ServerResponse.createByErrorMessage("修改密码失败");
    }


    /**
     * 登录状态下修改密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userMapper.checkPasswordByUserId(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if(resultCount == 0) {
           return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }

        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    /**
     * 更新个人信息
     * @param user
     * @return
     */
    public ServerResponse<User> updateInformation(User user) {
        //username不可修改
        //email修改是要校验，并且如果email存在的话，不能是登录的这个id中的eamil
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if(resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在，请更换email");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);

        if(updateCount > 0){
            return  ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }

        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }


    /**
     * 获取用户个人信息
     * @param userId
     * @return
     */
    public ServerResponse<User> getInformation(Integer userId) {
        User currentUser = userMapper.selectByPrimaryKey(userId);
        if(currentUser == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }

        currentUser.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(currentUser);

    }
}
