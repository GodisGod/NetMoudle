package com.study.netmoudle.reponsitory.base;

import com.google.gson.Gson;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LHD on 2019/3/9.
 */
public class RetrofitHelper {
    private Set<Interceptor> defInterceptors;
    private OkHttpClient defOkHttpClient;
    private static RetrofitHelper INSTANCE;
    private Converter.Factory factory;
    private CookieJar cookieJar;

    private static String[] VERIFY_HOST_NAME_ARRAY = new String[2];

    private RetrofitHelper() {
        this.defInterceptors = new HashSet();
    }

    public static synchronized void init() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitHelper();
        }
    }

    public void setCookieJar(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }

    public CookieJar getCookieJar() {
        return this.cookieJar;
    }

    public void setFactory(Converter.Factory factory) {
        this.factory = factory;
    }

    public Converter.Factory getFactory() {
        if (this.factory == null) {
            this.factory = GsonConverterFactory.create(new Gson());
        }
        return this.factory;
    }

    public static RetrofitHelper instance() {
        return INSTANCE;
    }

    public void addInterceptor(Interceptor interceptor) {
        if (this.defInterceptors == null) {
            synchronized(this) {
                this.defInterceptors = new HashSet();
            }
        }

        this.defInterceptors.add(interceptor);
    }

    public void addInterceptor(Set<Interceptor> interceptors) {
        if (this.defInterceptors == null) {
            synchronized(this) {
                this.defInterceptors = new HashSet();
            }
        }

        this.defInterceptors.addAll(interceptors);
    }

    public OkHttpClient getOkHttpClient(int connectTime, int readTime) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder .hostnameVerifier(new HostnameVerifier() {
//            public boolean verify(String hostname, SSLSession session) {
//                Log.i("LHD","");
//                //为了正确处理主机名的验证，需要重写此方法
//                if (TextUtils.isEmpty(hostname))return false;
//                //在数组里定义需要校验的主机名
//                return!Arrays.asList(VERIFY_HOST_NAME_ARRAY).contains(hostname);
//            }
//        });
        builder.sslSocketFactory(getSslSocketFactory()).build();
        if (this.cookieJar != null) {
            builder.cookieJar(this.cookieJar);
        }

        if (this.defInterceptors != null) {
            Iterator iterators = this.defInterceptors.iterator();
            while(iterators.hasNext()) {
                Interceptor interceptor = (Interceptor)iterators.next();
                builder.addInterceptor(interceptor);
            }
        }

        builder.connectTimeout((long)connectTime, TimeUnit.SECONDS);
        builder.readTimeout((long)readTime, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }

    private OkHttpClient getOkHttpClient() {
        if (this.defOkHttpClient == null) {
            synchronized(this) {
                if (this.defOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.hostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                    builder.sslSocketFactory(getSslSocketFactory());
                    if (this.cookieJar != null) {
                        builder.cookieJar(this.cookieJar);
                    }
                    this.defOkHttpClient = builder.build();
                }
            }
        }

        return this.defOkHttpClient;
    }

    public void setDefOkHttpClient(OkHttpClient defOkHttpClient) {
        this.defOkHttpClient = defOkHttpClient;
    }

    private OkHttpClient.Builder newBuilder() {
        OkHttpClient okHttpClient = this.getOkHttpClient();
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        return builder;
    }

    public OkHttpClient createClient(Interceptor... interceptors) {
        if (interceptors != null && interceptors.length != 0) {
            OkHttpClient.Builder builder = this.newBuilder();
            for (Interceptor interceptor:interceptors){
                builder.addInterceptor(interceptor);
            }
            return builder.build();
        } else {
            return this.getOkHttpClient();
        }
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
