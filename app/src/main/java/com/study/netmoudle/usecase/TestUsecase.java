package com.study.netmoudle.usecase;

import com.study.netmoudle.bean.TestBean;
import com.study.netmoudle.reponsitory.TestReponsitory;

import io.reactivex.Observable;

/**
 * Created by LHD on 2019/3/9.
 */
public class TestUsecase extends BaseUsecase<TestReponsitory>{

    public Observable<TestBean> getTest(){
       return reponsitory.getTest();
    }

}
