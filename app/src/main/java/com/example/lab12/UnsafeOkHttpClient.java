package com.example.lab12;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class UnsafeOkHttpClient {
    /**
     * 获取一个信任所有证书的OkHttpClient
     * Returns: OkHttpClient
     */
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 设置一个信任所有证书的X509TrustManager
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                            // 不对客户端进行验证，接受所有客户端证书
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                            // 不对服务器端进行验证，接受所有服务端证书
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            // 返回空数组表示接受所有证书
                            return new X509Certificate[]{};
                        }
                    }
            };

            // 创建 SSLContext 并将上面的 TrustManager 添加到初始化设置中
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            // 设置自定义的 OkHttpClient 以禁用证书验证和主机名验证
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

            return client;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

