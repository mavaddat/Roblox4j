package online.pizzacrust.master.trello.impl;

import com.google.gson.Gson;

import com.mashape.unirest.http.Unirest;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.master.trello.Card;
import online.pizzacrust.master.trello.api.ITrelloCard;
import online.pizzacrust.master.trello.api.ITrelloCheckList;
import online.pizzacrust.master.trello.api.TrelloCheckListItem;

public class TrelloCard implements ITrelloCard {

    private final Card card;
    private final String apiKey;
    private final String apiToken;

    public TrelloCard(String cardId, String apiKey, String apiToken) throws Exception {
        this.card = new Card();
        card.setId(cardId);
        card.update(apiKey, apiToken);
        this.apiKey = apiKey;
        this.apiToken = apiToken;
        HttpClient httpClient = HttpClients.custom().disableCookieManagement().build();
        Unirest.setHttpClient(httpClient);
    }

    @Override
    public String getName() throws Exception {
        return card.getName();
    }

    @Override
    public String getId() throws Exception {
        return card.getId();
    }

    @Override
    public void save() throws Exception {
        card.update(apiKey, apiToken);
    }

    @Override
    public void refresh() throws Exception {
        card.update(apiKey, apiToken);
    }

    @Override
    public void setName(String name) throws Exception {
        card.setName(name);
    }

    @Override
    public String getDescription() throws Exception {
        return card.getDesc();
    }

    @Override
    public void setDescription(String description) throws Exception {
        card.setDesc(description);
    }

    public static class CheckListResponseData {
        public String id;
    }

    @Override
    public List<ITrelloCheckList> getCheckLists() throws Exception {
        List<ITrelloCheckList> checkLists = new ArrayList<>();
        String url = "https://api.trello.com/1/cards/" + this.getId() + "/checklists";
        String response = Unirest.get(url).queryString("key", apiKey).queryString("token",
                apiToken).asString().getBody();
        CheckListResponseData[] checkListResponseData = new Gson().fromJson(response,
                CheckListResponseData[].class);
        for (CheckListResponseData checkListResponseDatum : checkListResponseData) {
            checkLists.add(new TrelloCheckList(checkListResponseDatum.id, apiToken, apiKey));
        }
        return checkLists;
    }

    @Override
    public void addCheckList(ITrelloCheckList checkList) {
        //TODO
    }

    @Override
    public void setCheckListItemState(TrelloCheckListItem item, boolean newState) {

    }

    public static void main(String... args) throws Exception {
        String key =  System.getProperty("api.key");
        String token = System.getProperty("api.token");
        TrelloCard trelloCard = new TrelloCard("xelRjgL8", key, token);
        System.out.println(trelloCard.getName());
        for (ITrelloCheckList iTrelloCheckList : trelloCard.getCheckLists()) {
            System.out.println(iTrelloCheckList.getName());
            for (TrelloCheckListItem trelloCheckListItem : iTrelloCheckList.getItems()) {
                System.out.println(" - " + trelloCheckListItem.getName() + " - " +
                        trelloCheckListItem.isMarked());
            }
        }
    }

}
