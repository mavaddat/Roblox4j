package online.pizzacrust.roblox.impl;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import online.pizzacrust.roblox.api.Profile;
import online.pizzacrust.roblox.api.errors.InvalidUserException;

public class BasicProfile implements Profile {

    private final int id;
    private final String username;

    public BasicProfile(String username) throws InvalidUserException {
        if (!doesUserExist(username)) {
            throw new InvalidUserException();
        }
        try {
            String baseUrl = "https://api.roblox" +
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
            String baseUrl = "https://api.roblox" +
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

    public static void main(String... args) throws Exception {
        System.out.println(doesUserExist("Swatcommader6"));
        System.out.println(doesUserExist("aksmdfkamdkfmakdmf"));
        BasicProfile basicProfile = new BasicProfile("Swatcommader6");
        System.out.println(basicProfile);
        new BasicProfile("aksmdfkamdkfmakdmf");
    }

    @Override
    public String toString() {
        return "{name: " + this.username + ", id: " + this.id + "}";
    }

}
