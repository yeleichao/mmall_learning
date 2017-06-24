package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by yeleichao on 2017-6-22.
 */
public interface IUserService {

     ServerResponse<User> login(String username, String password);

     ServerResponse<String> register(User user);

     ServerResponse<String> checkVaild(String value, String type);

     ServerResponse selectQuestion(String username);

     ServerResponse<String> checkAnswer(String username, String question, String answer);
}
