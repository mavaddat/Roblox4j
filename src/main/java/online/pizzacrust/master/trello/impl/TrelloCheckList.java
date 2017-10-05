package online.pizzacrust.master.trello.impl;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.master.trello.api.ITrelloCheckList;
import online.pizzacrust.master.trello.api.TrelloCheckListItem;

public class TrelloCheckList implements ITrelloCheckList {

    private final String id;
    private final String name;
    private final List<TrelloCheckListItem> trelloCheckListItems = new ArrayList<>();

    public TrelloCheckList(String id, String apiToken, String apiKey) throws IOException {
        this.id = id;
        String root = "https://api.trello.com/1/checklists/" + id;
        if (apiKey != null) {
            System.out.println("Using credentials to make request happen");
            root = root + "?token=" + apiToken + "&key=" + apiKey;
        }
        CheckListResponse response = new Gson().fromJson(Jsoup.connect(root).ignoreContentType(true)
                .get()
                .body().text(), CheckListResponse.class);
        this.name = response.name;
        for (CheckListResponse.CheckItemData checkItem : response.checkItems) {
            trelloCheckListItems.add(new TrelloCheckListItem(checkItem.state.equalsIgnoreCase
                    ("completed"), checkItem.id, checkItem.name, this));
        }
    }


    public static class CheckListResponse {
        public String name;
        public static class CheckItemData {
            public String state;
            public String id;
            public String name;
        }
        public CheckItemData[] checkItems;
    }

    @Override
    public List<TrelloCheckListItem> getItems() {
        return trelloCheckListItems;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

}
