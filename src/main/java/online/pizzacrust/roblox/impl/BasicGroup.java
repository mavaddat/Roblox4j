package online.pizzacrust.roblox.impl;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import online.pizzacrust.roblox.Robloxian;
import online.pizzacrust.roblox.group.Group;
import online.pizzacrust.roblox.group.Roleset;

public class BasicGroup implements Group{

    private final int groupId;

    public BasicGroup(int groupId) {
        this.groupId = groupId;
    }

    public static class RolesetData {
        public int Id;
        public String Name;
        public int Rank;
    }

    @Override
    public List<Roleset> getRolesets() throws Exception {
        List<Roleset> rolesets = new ArrayList<>();
        String url = "https://www.roblox.com/api/groups/" + groupId + "/RoleSets/";
        RolesetData[] data = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true).get()
                .body().text(), RolesetData[].class);
        for (RolesetData datum : data) {
            rolesets.add(new BasicRoleset(datum.Rank, datum.Id, datum.Name));
        }
        return rolesets;
    }

    public static class NameData {
        public String Name;
    }

    @Override
    public String getName() throws Exception {
        String url = "https://api.roblox.com/groups/" + groupId;
        NameData data = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true).get().body
                ().text(), NameData.class);
        return data.Name;
    }

    public static class PlayersData {
        public static class PlayerData {
            public int userId;
            public String username;
        }
        public PlayerData[] data;
    }

    @Override
    public Robloxian.LightReference[] getMembersInRole(Roleset roleset) throws Exception {
        String url = "https://groups.roblox" +
                ".com/v1/groups/" + groupId + "/roles/" + roleset.getId() +
        "/users?sortOrder=Asc&limit=100";
        PlayersData data = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true).get()
                .body().text(), PlayersData.class);
        List<Robloxian.LightReference> references = new ArrayList<>();
        for (PlayersData.PlayerData datum : data.data) {
            references.add(new BasicReference(datum.userId, datum.username));
        }
        return references.toArray(new Robloxian.LightReference[references.size()]);
    }

    @Override
    public Optional<Roleset> getRole(Robloxian robloxian) throws Exception {
        String url = "https://www.roblox.com/Game/LuaWebService/HandleSocialRequest.ashx" +
                "?method=GetGroupRank" +
                "&playerid=" + robloxian.getUserId() +
                "&groupid=" + this.groupId;
        int rankIndex = Integer.parseInt(Jsoup.connect(url).ignoreContentType(true).get().body()
                .text());
        List<Roleset> rolesets = getRolesets();
        for (Roleset roleset : rolesets) {
            if (roleset.getRankIndex() == rankIndex) {
                return Optional.of(roleset);
            }
        }
        return Optional.empty();
    }

    @Override
    public int getId() {
        return groupId;
    }

    public static void main(String... args) throws Exception {
        BasicGroup group = new BasicGroup(860594);
        BasicRobloxian robloxian = new BasicRobloxian("Swatcommader6");
        System.out.println(group.getRole(robloxian).get().getName());
        /*
        for (Roleset roleset : group.getRolesets()) {
            System.out.println("role name: " + roleset.getName());
            System.out.println("role id: " + roleset.getId());
            for (Robloxian.LightReference lightReference : group.getMembersInRole(roleset)) {
                System.out.println(lightReference);
            }
        }
        */

    }

}
