package com.study.netmoudle.reponsitory;

import com.study.netmoudle.bean.TestBean2;
import com.study.netmoudle.reponsitory.base.BaseReponsitory;

import io.reactivex.Observable;

/**
 * Created by LHD on 2019/3/9.
 */
public interface TestReponsitory extends BaseReponsitory {

    Observable<TestBean2> getTest();

}
