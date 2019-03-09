package com.study.netmoudle.reponsitory.base;

import android.content.Context;

import com.google.gson.Gson;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
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
    private Context context;
    private OkHttpClient defOkHttpClient;
    private static RetrofitHelper INSTANCE;
    private Converter.Factory factory;
    private CookieJar cookieJar;

    private RetrofitHelper(Context context) {
        this.context = context;
        this.defInterceptors = new HashSet<>();

    }

    public static synchronized void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitHelper(context);
        }
    }

    public void setCookieJar(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public void setFactory(Converter.Factory factory) {
        this.factory = factory;
    }

    public Converter.Factory getFactory() {
        if (factory == null) {
            factory = GsonConverterFactory.create(new Gson());
        }
        return factory;
    }

    public static RetrofitHelper instance() {
        return INSTANCE;
    }

    public void addInterceptor(Interceptor interceptor) {
        if (defInterceptors == null) {
            synchronized (this) {
                defInterceptors = new HashSet<>();
            }
        }
        defInterceptors.add(interceptor);
    }

    public void addInterceptor(Set<Interceptor> interceptors) {
        if (defInterceptors == null) {
            synchronized (this) {
                defInterceptors = new HashSet<>();
            }
        }
        defInterceptors.addAll(interceptors);
    }


    /**
     * 注意:改方法每次都会创建一个okhttpClient，请谨慎使用
     */
    public OkHttpClient getOkHttpClient(int connectTime,int readTime) {
        OkHttpClient okHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //ssl
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        builder.sslSocketFactory(getSslSocketFactory());
        //cookieJar
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }
        if(defInterceptors != null) {
            for (Interceptor interceptor : defInterceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        builder.connectTimeout(connectTime, TimeUnit.SECONDS);// connect timeout
        builder.readTimeout(readTime, TimeUnit.SECONDS); // socket timeout
        okHttpClient = builder.build();
        return okHttpClient;
    }

    /**
     * create a OkHttpClient if it is null
     *
     * @return
     */
    private OkHttpClient getOkHttpClient() {
        if (defOkHttpClient == null) {
            synchronized (this) {
                if (defOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    //ssl
                    builder.hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                    builder.sslSocketFactory(getSslSocketFactory());
                    //cookieJar
                    if (cookieJar != null) {
                        builder.cookieJar(cookieJar);
                    }
                    defOkHttpClient = builder.build();

                }
            }
        }
        return defOkHttpClient;
    }

    public void setDefOkHttpClient(OkHttpClient defOkHttpClient) {
        this.defOkHttpClient = defOkHttpClient;
    }

    /**
     * create a Okhttp buider
     * use the def
     *
     * @return
     */
    private OkHttpClient.Builder newBuilder() {
        OkHttpClient okHttpClient = getOkHttpClient();
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        return builder;
    }

    /**
     * 创建okHttpClient实例
     *
     * @param interceptors 自定义拦截器
     * @return
     */
    public OkHttpClient createClient(Interceptor... interceptors) {
        if (interceptors == null || interceptors.length == 0) return getOkHttpClient();
        OkHttpClient.Builder builder = newBuilder();
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }


    private static SSLSocketFactory getSslSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            sslSocketFactory = sslContext.getSocketFactory();

        } catch (Exception e) {

        }
        return sslSocketFactory;
    }
}
