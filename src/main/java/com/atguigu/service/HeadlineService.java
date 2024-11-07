package com.atguigu.service;

import com.atguigu.pojo.Headline;
import com.atguigu.pojo.vo.PortalVo;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author george zeng
* @description Service used to operate table news_headline
*/
public interface HeadlineService extends IService<Headline> {

    /**
     * look up data in home page
     * @param portalVo portal vo
     * @return result
     */
    Result findNewsPage(PortalVo portalVo);

    /**
     * look up headline detail by id
     * @param hid headline id
     * @return result
     */
    Result showHeadlineDetail(Integer hid);


    /**
     * publish headline
     * @param headline headline
     * @return result
     */
    Result publish(Headline headline,String token);

    /**
     * update headline
     * @param headline headline
     * @return result
     */
    Result updateData(Headline headline);
}
