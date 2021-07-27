package ar.com.signals.trading.util.rest;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.FactoryBean;

public class MyCustomHttpClientFactory implements FactoryBean<HttpClient>{

    @Override
    public HttpClient getObject() throws Exception {
    	SSLContext sslContext = SSLContext.getInstance("TLS");
       
        TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
     
            public void checkClientTrusted(X509Certificate[] certs, String t) {
            }
     
            public void checkServerTrusted(X509Certificate[] certs, String t) {
            }
        } };
        sslContext.init(null, certs, new SecureRandom());
        SSLSocketFactory f = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        
        //SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1.2" }, null,
                                         // SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);//habilito conexion TLSv1.2
   
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                          .register("http", PlainConnectionSocketFactory.getSocketFactory())
                          .register("https", f)
                          .build();
   
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);     
        
        RequestConfig config = RequestConfig.custom()
        		  .setConnectTimeout(5000)
        		  .setConnectionRequestTimeout(5000)
        		  .setSocketTimeout(40000).build();
        
        CloseableHttpClient client = HttpClients
                          .custom()
                          .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)//Si el servidor de destino devuelve en el header 'Connection: Keep-Alive' entonces puedo usar una estrategia al conectarme
                          .setSSLSocketFactory(f)
                          .setConnectionManager(cm)
                          .setDefaultRequestConfig(config)
                          .build();
        return client;
    }

    @Override
    public Class<HttpClient> getObjectType() {
        return HttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}