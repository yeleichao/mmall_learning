package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by yeleichao on 2017-6-22.
 */
public interface IUserService {

     ServerResponse<User> login(String username, String password);
}
