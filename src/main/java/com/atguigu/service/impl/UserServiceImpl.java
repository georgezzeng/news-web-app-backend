package com.atguigu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.MD5Util;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author george zeng
* @description Service used to operate table news_user
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;

    /**
     * This method checks username and passwords
     * @param user user object passed from controller
     * @return a result object
     */
    @Override
    public Result login(User user) {
        //根据账号查询数据
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        User loginUser = userMapper.selectOne(lambdaQueryWrapper);

        //Checks username
        if (loginUser == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //compare passwords
        if (!StringUtils.isEmpty(user.getUserPwd())
                && MD5Util.encrypt(user.getUserPwd()).equals(loginUser.getUserPwd())){
            //log in successful

            //generate token and encapsulate into a Result object using JwtHelper
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));
            //store the token in response data
            Map data = new HashMap();
            data.put("token",token);
            return Result.ok(data);
        }

        //log in failed
        return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
    }


    /**
     * Get user information using token
     * @param token token passed by the controller
     * @return a result that's either successful or failed
     */
    @Override
    public Result getUserInfo(String token) {
        //check if it's expired
        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            //If yes, returns notLogin code
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }
        //get userId using the token
        int userId = jwtHelper.getUserId(token).intValue();

        User user = userMapper.selectById(userId);
        user.setUserPwd("");

        //Then store the user object into a hashMap
        Map data = new HashMap();
        data.put("loginUser",user);

        return Result.ok(data);
    }


    /**
     * Check is username exists in the database
     * @param username username
     * @return
     */
    @Override
    public Result checkUserName(String username) {

        LambdaQueryWrapper<User> queryWrapper
                = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        Long count = userMapper.selectCount(queryWrapper);

        //if count == 0, username can be used
        if (count == 0) {
            return Result.ok(null);
        }
        return Result.build(null,ResultCodeEnum.USERNAME_USED);
    }


    /**
     * Register user
     * @param user user
     * @return result
     */
    @Override
    public Result regist(User user) {

        LambdaQueryWrapper<User> queryWrapper
                = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        Long count = userMapper.selectCount(queryWrapper);
        //if count > 0, username already exists
        if (count > 0) {
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }
        //encrypt user password since it's plain text
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));

        //store user into database
        userMapper.insert(user);

        return Result.ok(null);
    }
}




