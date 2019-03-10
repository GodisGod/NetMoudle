package com.study.netmoudle.reponsitory.impl;

import com.study.netmoudle.baseurl.BaseUrl;
import com.study.netmoudle.bean.BlogBean;
import com.study.netmoudle.reponsitory.BlogReponsitory;
import com.study.netmoudle.reponsitory.base.BaserImpl;
import com.study.netmoudle.reponsitory.base.RetrofitHelper;
import com.study.netmoudle.service.MyBlogService;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by LHD on 2019/3/10.
 */
public class BlogReponsitoryImpl extends BaserImpl implements BlogReponsitory {
    @Override
    public String getBaseUrl() {
        return BaseUrl.BLOG;
    }

    @Override
    public Observable<BlogBean> getBlogJson() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.hostnameVerifier(new HostnameVerifier(){
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        builder.sslSocketFactory(getSslSocketFactory());
        OkHttpClient okHttpClient = builder.build();
        Converter.Factory factory = RetrofitHelper.instance().getFactory();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .client(okHttpClient)
                .build();
        MyBlogService myBlogService = retrofit.create(MyBlogService.class);
        Observable observable =  myBlogService.getTestBean();
//       call.enqueue(new Callback() {
//           @Override
//           public void onResponse(Call call, Response response) {
//               Log.i("LHD","onResponse = "+response.body().toString());
//           }
//
//           @Override
//           public void onFailure(Call call, Throwable t) {
//               Log.i("LHD","onFailure "+t.getMessage());
//           }
//       });
//        return retrofit.create(TestService.class).getTestBean();
        return observable;
    }

    private static SSLSocketFactory getSslSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init((KeyManager[])null, trustAllCerts, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception var3) {
            ;
        }

        return sslSocketFactory;
    }
}
