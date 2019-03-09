package com.study.netmoudle.reponsitory.base;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by LHD on 2019/3/9.
 */
public abstract class BaserImpl {

    /**
     * 构造service
     * 基础地址，service类，工厂(gson,fastjson等)，拦截器
     */
    protected <T> T createService(String url, Class<T> serviceClass, Converter.Factory factory, Interceptor... interceptors) {
        OkHttpClient okHttpClient = RetrofitHelper.instance().createClient(interceptors);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .client(okHttpClient)
                .build();
        return retrofit.create(serviceClass);
    }

    /**
     * 构造service
     * 基础地址，service类，拦截器
     */
    protected <T> T createService(String url, Class<T> serviceClass, Interceptor... interceptors) {
        //获取默认的转换器，GsonConverterFactory
        Converter.Factory factory = RetrofitHelper.instance().getFactory();
        return this.createService(url, serviceClass, factory, interceptors);
    }

    /**
     * 构造service
     * 基础地址，service类
     */
    protected <T> T createService(String url, Class<T> serviceClass) {
        return this.createService(url, serviceClass, new Interceptor[0]);
    }

    /**
     * 构造service
     * service类
     */
    protected <T> T createService(Class<T> serviceClass) {
        return this.createService(getBaseUrl(), serviceClass);
    }

    public abstract String getBaseUrl();

}
