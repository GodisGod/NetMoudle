package com.study.netmoudle.service;

import com.study.netmoudle.bean.BlogBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by LHD on 2019/3/9.
 */
public interface MyBlogService {

    //rxjava2和retrofit2结合的关键是把返回值封装成rxjava2的Observable对象
    @GET("basil2style")
    Observable<BlogBean> getTestBean();

}
