package com.atguigu.service.impl;

import com.atguigu.pojo.vo.PortalVo;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.Headline;
import com.atguigu.service.HeadlineService;
import com.atguigu.mapper.HeadlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author george zeng
* @description Service used to operate table news_headline
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{


    @Autowired
    private HeadlineMapper headlineMapper;

    @Autowired
    private JwtHelper jwtHelper;

    /**
     *
     *   1. Conduct pagination data query
     *   2. Pagination data, concatenate to result as needed
     *
     * @param portalVo
     * @return
     */
    @Override
    public Result findNewsPage(PortalVo portalVo) {

        //Page -> current page  page capacity
        IPage<Map> page = new Page<>(portalVo.getPageNum(),portalVo.getPageSize());
        headlineMapper.selectMyPage(page,portalVo);

        Map data = new HashMap();

        data.put("pageData",page.getRecords());
        data.put("pageNum",page.getCurrent());
        data.put("pageSize",page.getSize());
        data.put("totalPage",page.getPages());
        data.put("totalSize",page.getTotal());

        Map pageInfo = new HashMap();
        pageInfo.put("pageInfo",data);

        return Result.ok(pageInfo);
    }


    /**
     * Query details by ID
     *  query the corresponding data / Multiple tables
     *  Increment view count by +1 [Use optimistic locking, version corresponding to the current data]
     *
     * @param hid
     * @return
     */
    @Override
    public Result showHeadlineDetail(Integer hid) {

        Map data =  headlineMapper.queryDetailMap(hid);
        Map headlineMap = new HashMap();
        headlineMap.put("headline",data);


        Headline headline = new Headline();
        headline.setHid((Integer) data.get("hid"));
        headline.setVersion((Integer) data.get("version"));
        //view count + 1
        headline.setPageViews((Integer) data.get("pageViews") +1);
        headlineMapper.updateById(headline);

        return Result.ok(headlineMap);
    }

    /**
     * publish headline
     * @param headline headline
     * @return result
     */
    @Override
    public Result publish(Headline headline,String token) {

        //use token to get user id
        int userId = jwtHelper.getUserId(token).intValue();
        //set headline properties
        headline.setPublisher(userId);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());

        headlineMapper.insert(headline);

        return Result.ok(null);
    }


    /**
     * Update headline data
     *
     * @param headline
     * @return
     */
    @Override
    public Result updateData(Headline headline) {

        //use hid to get the version number
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();

        //optimistic locking
        headline.setVersion(version);
        //update the updateTime to now
        headline.setUpdateTime(new Date());

        headlineMapper.updateById(headline);

        return Result.ok(null);
    }
}




