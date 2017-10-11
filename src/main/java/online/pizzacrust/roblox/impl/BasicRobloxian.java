package online.pizzacrust.roblox.impl;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.roblox.Badge;
import online.pizzacrust.roblox.Roblox;
import online.pizzacrust.roblox.Robloxian;
import online.pizzacrust.roblox.errors.InvalidUserException;
import online.pizzacrust.roblox.group.Group;

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

    public static class BadgeResponse {
        public static class BadgeData {
            public String Name;
        }
        public BadgeData[] RobloxBadges;
    }

    @Override
    public List<String> getRobloxBadges() throws Exception{
        List<String> badges = new ArrayList<>();
        String url = "https://www.roblox" +
                ".com/badges/roblox?userId=261&imgWidth=110&imgHeight=110&imgFormat=png";
        BadgeResponse response = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true)
                .get().body().text(), BadgeResponse.class);
        for (BadgeResponse.BadgeData robloxBadge : response.RobloxBadges) {
            badges.add(robloxBadge.Name);
        }
        return badges;
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
    public boolean isInGroup(Group groupId) throws Exception{
        String url = "https://www.roblox.com/Game/LuaWebService/HandleSocialRequest" +
                ".ashx?method=IsInGroup&playerid=" + this.getUserId() + "&groupid=" + groupId.getId();
        return Boolean.parseBoolean(Jsoup.connect(url).ignoreContentType(true).get().body
                ().text());
    }

    @Override
    public String getProfileUrl() {
        return "https://www.roblox.com/users/" + this.getUserId() + "/profile";
    }

    public static class GroupsResponse {
        public static class GroupData {
            public int Id;
        }
        public GroupData[] Groups;
    }

    @Override
    public Group[] getGroups() throws Exception {
        String url = "https://www.roblox.com/users/profile/playergroups-json?userId=" + this
                .getUserId();
        GroupsResponse response = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true)
                .get().body().text(), GroupsResponse.class);
        List<Group> groups = new ArrayList<>();
        for (GroupsResponse.GroupData group : response.Groups) {
            groups.add(Roblox.get(group.Id));
        }
        return groups.toArray(new Group[groups.size()]);
    }

    public static class BadgesResponse {
        public static class BadgesData {
            public String nextPageCursor;
            public static class PlayerBadgeData {
                public static class ItemData {
                    public int AssetId;
                    public String Name;
                }
                public ItemData Item;
            }
            public PlayerBadgeData[] Items;
        }
        public BadgesData Data;
    }

    private List<Badge> recursiveBadgeRetrieve(BadgesResponse response) throws Exception {
        String url = "https://www.roblox" +
                ".com/users/inventory/list-json?assetTypeId=21&cursor=" + response
                .Data.nextPageCursor + "&itemsPerPage=100" +
                "&pageNumber=1&sortOrder=Desc&userId=" + this.getUserId();
        BadgesResponse response1 = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true)
                .get().body().text(), BadgesResponse.class);
        List<Badge> badges = new ArrayList<>();
        for (BadgesResponse.BadgesData.PlayerBadgeData item : response1.Data.Items) {
            badges.add(new Badge(item.Item.AssetId, item.Item.Name));
        }
        if (response1.Data.nextPageCursor != null) {
            badges.addAll(recursiveBadgeRetrieve(response1));
        }
        return badges;
    }

    @Override
    public Badge[] getBadges() throws Exception {
        List<Badge> badges = new ArrayList<>();
        String url = "https://www.roblox" +
                ".com/users/inventory/list-json?assetTypeId=21&cursor=&itemsPerPage=100" +
                "&pageNumber=1&sortOrder=Desc&userId=" + this.getUserId();
        BadgesResponse response = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true)
                .get().body().text(), BadgesResponse.class);
        for (BadgesResponse.BadgesData.PlayerBadgeData item : response.Data.Items) {
            badges.add(new Badge(item.Item.AssetId, item.Item.Name));
        }
        // next page
        if (response.Data.nextPageCursor != null) {
            badges.addAll(recursiveBadgeRetrieve(response));
        }
        return badges.toArray(new Badge[badges.size()]);
    }

    public static void main(String... args) throws Exception {
        BasicRobloxian robloxian = new BasicRobloxian("SurpriseParty");
        Badge[] badges = robloxian.getBadges();
        System.out.println("number of badges: " + badges.length);
        for (Badge badge : badges) {
            System.out.println("badge name: " + badge.getName());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BasicRobloxian)) {
            return false;
        }
        BasicRobloxian robloxian = (BasicRobloxian) obj;
        return robloxian.getUserId() == this.getUserId();
    }

}
