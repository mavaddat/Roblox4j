package online.pizzacrust.roblox.auth;

import com.google.gson.Gson;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import java.util.List;
import java.util.Map;

public class AuthenticationInfo {

    private final String token;
    private final String verifyToken;

    private AuthenticationInfo(String token, String verifyToken) {
        this.token = token;
        this.verifyToken = verifyToken;
        Unirest.setObjectMapper(new ObjectMapper() {
            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                return new Gson().fromJson(s, aClass);
            }

            @Override
            public String writeValue(Object o) {
                return new Gson().toJson(o);
            }
        });
    }

    public static AuthenticationInfo authenticate(String username,
                                                  String password) throws AuthenticationException {
        try {
            Headers headers = Unirest.post("https://www.roblox.com/newlogin").field("username",
                    username).field
                    ("password", password).asString().getHeaders();
            String token = null;
            for (String s : headers.get("Set-Cookie")) {
                if (s.contains(".ROBLOSECURITY")) {
                    token = s.split(";")[0];
                }
            }
            if (token == null) throw new RuntimeException();
            String verifyToken = Unirest.post("https://api.roblox.com/sign-out/v1").header
                    ("Cookie",
                    token + ";").asString().getHeaders().getFirst("X-CSRF-TOKEN");
            return new AuthenticationInfo(token, verifyToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException();
        }
    }

    public HttpRequestWithBody authenticateUnirestRequest(HttpRequestWithBody httpRequestWithBody) {
        httpRequestWithBody.header("Cookie", getToken() + ";").header("X-CSRF-TOKEN",
                verifyToken);
        return httpRequestWithBody;
    }

    public static void main(String... args) throws Exception {
        AuthenticationInfo authenticationInfo = AuthenticationInfo.authenticate(args[0], args[1]);
        System.out.println(authenticationInfo.getToken());
        System.out.println(authenticationInfo.getVerifyToken());
    }

    public String getToken() {
        return token;
    }

    //csrf
    public String getVerifyToken() {
        return verifyToken;
    }
}
