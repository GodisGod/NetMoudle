package com.study.netmoudle.usecase;

import com.study.netmoudle.bean.TestBean2;
import com.study.netmoudle.reponsitory.TestReponsitory;

import io.reactivex.Observable;

/**
 * Created by LHD on 2019/3/9.
 */
public class TestUsecase extends BaseUsecase<TestReponsitory>{

    public Observable<TestBean2> getTest(){
       return reponsitory.getTest();
    }

}
