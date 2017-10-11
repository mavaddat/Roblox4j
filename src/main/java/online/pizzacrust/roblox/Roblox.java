package online.pizzacrust.roblox;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import java.util.Optional;

import online.pizzacrust.roblox.errors.InvalidUserException;
import online.pizzacrust.roblox.group.Group;
import online.pizzacrust.roblox.impl.BasicGroup;
import online.pizzacrust.roblox.impl.BasicProfile;
import online.pizzacrust.roblox.impl.BasicRobloxian;

public class Roblox {

    public static Optional<Robloxian> get(String username) {
        try {
            return Optional.of(new BasicRobloxian(username));
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Group get(int groupId) {
        return new BasicGroup(groupId);
    }

    public static Optional<Robloxian> getUserFromId(int userId) {
        try {
            String url = "https://api.roblox.com/Users/" + userId;
            String response = Jsoup.connect(url).ignoreContentType(true).get().body().text();
            BasicProfile.Response response1 = new Gson().fromJson(response, BasicProfile.Response
                    .class);
            if (response1.Username != null) {
                return get(response1.Username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void main(String... args) throws Exception {
        System.out.println(getUserFromId(1921231347).isPresent());
        System.out.println(getUserFromId(38043848).get().getUsername());
    }

}
