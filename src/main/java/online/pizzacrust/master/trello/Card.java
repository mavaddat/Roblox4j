package online.pizzacrust.master.trello;

import com.google.gson.Gson;

import com.mashape.unirest.http.Unirest;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Card {

    private String id;
    private boolean archived;
    private String desc;
    private String ownerListId;
    private String name;
    private String ownerBoardId;

    public String getOwnerBoardId() {
        return ownerBoardId;
    }

    public String getId() {
        return id;
    }

    /**
     * sets when not null
     * @param id
     */
    public void setId(String id) {
        if (this.id != null) {
            return;
        }
        this.id = id;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOwnerListId() {
        return ownerListId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class CardJson {
        public String name;
        public String desc;
        public boolean closed;
        public String idList;
        public String idBoard;
    }

    /**
     * Updates card data
     * @throws Exception
     */
    public void update(String apiKey, String clientToken) throws Exception {
        String root = "https://api.trello.com/1/cards/" + this.id;
        if (apiKey != null) {
            System.out.println("Using credentials to make request happen");
            root = root + "?token=" + clientToken + "&key=" + apiKey;
        }
        CardJson json = new Gson().fromJson(Jsoup.connect(root).ignoreContentType(true).get()
                .body().text(), CardJson.class);
        if (this.getName() == null) {
            this.setName(json.name);
            this.desc = json.desc;
            this.archived = json.closed;
            this.ownerListId = json.idList;
            this.ownerBoardId = json.idBoard;
        } else if (apiKey != null) {
            Map<String, Object> formData = new HashMap<>();
            if (!json.desc.equalsIgnoreCase(this.desc)) {
                formData.put("desc", this.desc);
            }
            if (!json.name.equalsIgnoreCase(this.name)) {
                formData.put("name", this.name);
            }
            if (json.closed != this.archived) {
                formData.put("closed", "" + this.archived);
            }
            Unirest.put(root).fields(formData).asString();
        }
    }

    private List<Map.Entry<String, String>> toList(Map<String, String> stringStringMap) {
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        entries.addAll(stringStringMap.entrySet());
        return entries;
    }

    public static void main(String... args) throws Exception {
        Card card = new Card();
        card.setId("RUsLFZFC");
        card.update(System.getProperty("api.key"), System.getProperty("api.token"));
        System.out.println(card.getName());
        System.out.println(card.getDesc());
        System.out.println(card.isArchived());
        System.out.println(card.ownerListId);
        card.setName("Ok");
        card.setDesc("Desc changing works");
        card.setArchived(true);
        card.update(System.getProperty("api.key"), System.getProperty("api.token"));
    }

}
