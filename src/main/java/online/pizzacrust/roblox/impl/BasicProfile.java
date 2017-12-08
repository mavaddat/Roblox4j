package online.pizzacrust.roblox.impl;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import online.pizzacrust.roblox.Profile;
import online.pizzacrust.roblox.errors.InvalidUserException;

public class BasicProfile implements Profile {

    private final int id;
    private final String username;

    public BasicProfile(String username) throws InvalidUserException {
        if (!doesUserExist(username)) {
            throw new InvalidUserException();
        }
        try {
            String baseUrl = "http://api.roblox" +
                    ".com/users/get-by-username?username=" + username;
            Response response = new Gson().fromJson(Jsoup.connect(baseUrl).ignoreContentType(true)
                    .get().body().text(), Response.class);
            this.id = response.Id;
            this.username = response.Username;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public int getUserId() {
        return this.id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public static  class Response {
        public String Username;
        public Integer Id;
    }

    public static boolean doesUserExist(String username) {
        try {
            String baseUrl = "http://api.roblox" +
                    ".com/users/get-by-username?username=" + username;
            String response = new Gson().fromJson(Jsoup.connect(baseUrl).ignoreContentType(true)
                    .get().body().text(), Response.class).Username;
            if (response != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    public static void main(String... args) throws Exception {
        enableSSLSocket();
        System.out.println(new BasicRobloxian("TGSCommander"));
    }

    @Override
    public String toString() {
        return "{name: " + this.username + ", id: " + this.id + "}";
    }

}
