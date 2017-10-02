package online.pizzacrust.master.roblox.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

    @Override
    public List<LightReference> getBestFriends() {
        return null;
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
        robloxian.getPastUsernames().forEach(System.out::println);
    }

}
