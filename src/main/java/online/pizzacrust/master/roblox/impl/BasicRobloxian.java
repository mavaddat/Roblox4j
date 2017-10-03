package online.pizzacrust.master.roblox.impl;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.master.roblox.Robloxian;
import online.pizzacrust.master.roblox.errors.InvalidUserException;

public class BasicRobloxian extends BasicProfile implements Robloxian {
    public BasicRobloxian(String username) throws InvalidUserException {
        super(username);
    }

    @Override
    public LightReference toReference() {
        return new BasicReference(this.getUserId(), this.getUsername());
    }

    public static class FriendsResponse {
        public static class FriendData {
            public int UserId;
            public String Username;
        }
        public FriendData[] Friends;
    }

    @Override
    public List<LightReference> getBestFriends() throws Exception {
        List<LightReference> references = new ArrayList<>();
        String url = "https://www.roblox" +
                ".com/friends/json?userId=" + getUserId() +
        "&currentPage=0&pageSize=1000&imgWidth=110&imgHeight=110&imgFormat=jpeg&friendsType" +
                "=BestFriends";
        FriendsResponse response = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true)
                .get().body().text(), FriendsResponse.class);
        for (FriendsResponse.FriendData friend : response.Friends) {
            references.add(new BasicReference(friend.UserId, friend.Username));
        }
        return references;
    }

    @Override
    public List<String> getRobloxBadges() {
        return null;
    }

    @Override
    public List<String> getPastUsernames() throws Exception {
        List<String> names = new ArrayList<>();
        Document document = Jsoup.connect(getProfileUrl()).ignoreContentType(true).get();
        Element rootNode = document.getElementsByClass("profile-name-history").first();
        Element pastNamesElement = rootNode.getElementsByClass("tooltip-pastnames").first();
        String pastNamesUnparsed = pastNamesElement.attr("title");
        String[] splitted = pastNamesUnparsed.split(",");
        for (String s : splitted) {
            names.add(s.trim());
        }
        return names;
    }

    @Override
    public boolean isInGroup(int groupId) {
        return false;
    }

    @Override
    public String getRankInGroup(int groupId) {
        return null;
    }

    @Override
    public String getProfileUrl() {
        return "https://www.roblox.com/users/" + this.getUserId() + "/profile";
    }

    public static void main(String... args) throws Exception {
        BasicRobloxian robloxian = new BasicRobloxian("Matthew_Castellan");
        System.out.println(robloxian.getBestFriends().size());
        robloxian.getBestFriends().forEach(System.out::println);
    }

}
