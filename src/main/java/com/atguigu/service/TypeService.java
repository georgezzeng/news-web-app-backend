package com.atguigu.service;

import com.atguigu.pojo.Type;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author george zeng
* @description Service used to operate table news_type
*/
public interface TypeService extends IService<Type> {

    /**
     * loop up data of all types
     * @return
     */
    Result findAllTypes();
}
