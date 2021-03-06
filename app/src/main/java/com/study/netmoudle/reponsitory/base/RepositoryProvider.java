package com.study.netmoudle.reponsitory.base;

import com.study.netmoudle.reponsitory.BlogReponsitory;
import com.study.netmoudle.reponsitory.TestReponsitory;
import com.study.netmoudle.reponsitory.impl.BlogReponsitoryImpl;
import com.study.netmoudle.reponsitory.impl.TestReponsitoryImpl;

/**
 * Created by LHD on 2019/3/9.
 */
public class RepositoryProvider {

    public void addRepository(){
        RepositoryMgr.instance().addRepository(TestReponsitory.class, new TestReponsitoryImpl());
        RepositoryMgr.instance().addRepository(BlogReponsitory.class, new BlogReponsitoryImpl());
    }

}
