package com.study.netmoudle.usecase;

import com.study.netmoudle.reponsitory.base.BaseReponsitory;
import com.study.netmoudle.reponsitory.base.RepositoryMgr;

import java.lang.reflect.ParameterizedType;

/**
 * Created by LHD on 2019/3/9.
 */
public class BaseUsecase<T extends BaseReponsitory> {
    protected T reponsitory;

    public BaseUsecase() {
        Class<BaseReponsitory> clazz = (Class<BaseReponsitory>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.reponsitory = (T)RepositoryMgr.instance().getRepository(clazz);
    }

}
