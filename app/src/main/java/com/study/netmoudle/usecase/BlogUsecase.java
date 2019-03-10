package com.study.netmoudle.usecase;

import com.study.netmoudle.bean.BlogBean;
import com.study.netmoudle.reponsitory.BlogReponsitory;

import io.reactivex.Observable;

/**
 * Created by LHD on 2019/3/10.
 */
public class BlogUsecase extends BaseUsecase<BlogReponsitory>{

    public Observable<BlogBean> getBlogJson(){
        return reponsitory.getBlogJson();
    }

}
