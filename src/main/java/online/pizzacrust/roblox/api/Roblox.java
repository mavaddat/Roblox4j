package online.pizzacrust.roblox.api;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import java.util.Optional;

import online.pizzacrust.roblox.api.errors.InvalidUserException;
import online.pizzacrust.roblox.api.group.Group;
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

    private static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static Optional<Robloxian> format(String ori) {
        if (!ori.contains(":")) {
            // either name or id
            if (!isInteger(ori)) {
                // this name
                return Roblox.get(ori);
            } else {
                // this num
                return Roblox.getUserFromId(Integer.parseInt(ori));
            }
        } else {
            // we have diff
            String[] splitted = ori.split(":");
            // check if 1 or 2
            if (splitted.length == 1) {
                return format(splitted[0]);
            }
            // else we parse
            return Roblox.get(splitted[0]);

        }
    }

    /**
     * tag example: TGSCommander:myid
     * @param tag
     * @return
     */
    public static Optional<Robloxian> getFromTag(String tag) {
        return format(tag);
    }

}
