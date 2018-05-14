package online.pizzacrust.roblox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import online.pizzacrust.roblox.auth.AuthenticationInfo;

public class Presence {

    Presence() {}

    public static Map<Robloxian, Presence> getPresenceForUsers(List<Robloxian> robloxianList,
                                            AuthenticationInfo authenticationInfo) throws Exception {
        Map<Robloxian, Presence> map = new HashMap<>();
        List<Integer> integers = new ArrayList<>();
        robloxianList.forEach((r) -> integers.add(r.getUserId()));
        String url = "https://presence.roblox.com/v1/presence/users";
        String raw = authenticationInfo.authenticateUnirestRequest(Unirest.post(url)).header
                ("Content-Type", "application/json").body(new Gson().toJson(new
                PresenceRequest(integers
                .toArray
                (new Integer[integers.size()])))).asString().getBody();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Status.class, new StatusDeserializer());
        PresenceResponse response = gsonBuilder.create().fromJson(raw, PresenceResponse.class);
        for (int i = 0; i < response.userPresences.length; i++) {
            map.put(robloxianList.get(i), response.userPresences[i]);
        }
        return map;
    }

    public static void main(String... args) throws Exception {
        AuthenticationInfo authenticationInfo = AuthenticationInfo.authenticate(args[0], args[1]);
        getPresenceForUsers(Arrays.asList(Roblox.get("TGSCommander").get(), Roblox.get("TimGeithner")
                .get()), authenticationInfo).forEach((r, p) -> System.out.println(p.getStatus()
                .get().name()
                + " - Last seen: " + p.getDate()));
    }

    public static class PresenceRequest {
        public Integer[] userIds;
        public PresenceRequest() {}
        public PresenceRequest(Integer[] userIds) { this.userIds = userIds;}
    }

    public static class PresenceResponse {
        public Presence[] userPresences;
    }

    public enum Status {
        OFFLINE(0),
        WEBSITE(1),
        GAME(2),
        STUDIO(3),
        UNKNOWN(-1);

        private final int id;

        Status(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Status getFromId(int id) {
            for (Status status : Status.values()) {
                if (status.getId() == id) {
                    return status;
                }
            }
            return Status.UNKNOWN;
        }
    }

    public static class StatusDeserializer implements JsonDeserializer<Status> {
        @Override
        public Status deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            int statusType = jsonElement.getAsInt();
            return Status.getFromId(statusType);
        }
    }

    @SerializedName("userPresenceType")
    private Integer status;

    public Optional<Status> getStatus() {
        if (status != null) {
            Optional.of(Status.getFromId(status));
        }
        return Optional.empty();
    }

    private String lastLocation;

    private Integer placeId;

    private Integer rootPlaceId;

    @SerializedName("gameId")
    private String serverId;

    private int userId;

    public String lastOnline;

    public static final DateFormat ROBLOX_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Date getDate() {
        try {
            return ROBLOX_DATE_FORMAT.parse(lastOnline.split("T")[0]);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getUserId() {
        return userId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public Integer getRootPlaceId() {
        return rootPlaceId;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public String getCurrentPlaceName() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> list = new ArrayList<>(Arrays.asList(getLastLocation().split(" ")));
        list.remove(0);
        for (String s : list) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public String getCurrentPlaceLink() {
        return "https://www.roblox.com/games/" + getRootPlaceId() + "/redirect";
    }
}
