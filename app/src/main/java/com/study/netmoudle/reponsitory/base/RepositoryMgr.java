package com.study.netmoudle.reponsitory.base;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LHD on 2019/3/9.
 */
public class RepositoryMgr {
    private final Map<Class, BaseReponsitory> dataRepositoryMap = new HashMap<>();
    private static RepositoryMgr INSTANCE = null;

    private RepositoryMgr() {

    }

    public static RepositoryMgr instance() {
        if (INSTANCE == null) {
            synchronized (RepositoryMgr.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryMgr();
                }
            }
        }
        return INSTANCE;
    }

    public void addRepository(Class<? extends BaseReponsitory> clazz, @NonNull BaseReponsitory dataRepository) {
        dataRepositoryMap.put(clazz, dataRepository);
    }

    @NonNull
    public <T extends BaseReponsitory> T getRepository(Class<T> clazz) {
        BaseReponsitory dataRepository = dataRepositoryMap.get(clazz);
        if (dataRepository == null) {
            throw new RuntimeException("must addRepository(" + clazz.getSimpleName() + ")");
        }
        return (T) dataRepository;
    }
}
