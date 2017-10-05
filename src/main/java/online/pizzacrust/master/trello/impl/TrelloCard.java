package online.pizzacrust.master.trello.impl;

import java.util.List;

import online.pizzacrust.master.trello.Card;
import online.pizzacrust.master.trello.api.ITrelloCard;
import online.pizzacrust.master.trello.api.ITrelloCheckList;
import online.pizzacrust.master.trello.api.TrelloCheckListItem;

public class TrelloCard implements ITrelloCard {

    private final Card card;
    private final String apiKey;
    private final String apiToken;

    public TrelloCard(Card card, String apiKey, String apiToken) {
        this.card = card;
        this.apiKey = apiKey;
        this.apiToken = apiToken;
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

    @Override
    public List<ITrelloCheckList> getCheckLists() {
        return null;
    }

    @Override
    public void addCheckList(ITrelloCheckList checkList) {
        //TODO
    }

    @Override
    public void setCheckListItemState(TrelloCheckListItem item, boolean newState) {

    }
}
