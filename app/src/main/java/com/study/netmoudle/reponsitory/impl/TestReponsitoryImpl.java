package com.study.netmoudle.reponsitory.impl;

import com.study.netmoudle.baseurl.BaseUrl;
import com.study.netmoudle.bean.TestBean;
import com.study.netmoudle.reponsitory.TestReponsitory;
import com.study.netmoudle.reponsitory.base.BaserImpl;
import com.study.netmoudle.service.TestService;

import io.reactivex.Observable;

/**
 * Created by LHD on 2019/3/9.
 */
public class TestReponsitoryImpl extends BaserImpl implements TestReponsitory {

    @Override
    public String getBaseUrl() {
        return BaseUrl.TEST;
    }


    @Override
    public Observable<TestBean> getTest() {
        return createService(TestService.class).getTestBean();
    }

}
