package com.atguigu.service;

import com.atguigu.pojo.User;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author george zeng
* @description Service used to operate table news_user
*/
public interface UserService extends IService<User> {

    /**
     * login
     * @param user user
     * @return result
     */
    Result login(User user);

    /**
     * get user information using token
     * @param token token
     * @return result
     */
    Result getUserInfo(String token);

    /**
     * check username availability
     * @param username username
     * @return result
     */
    Result checkUserName(String username);

    /**
     * user registration
     * @param user user
     * @return result
     */
    Result regist(User user);
}
