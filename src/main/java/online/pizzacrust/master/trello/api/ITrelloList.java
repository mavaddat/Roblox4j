package online.pizzacrust.master.trello.api;

import java.util.List;

public interface ITrelloList {

    List<ITrelloCard> getCards();

    ITrelloCard createNewCard(String name, String desc);

}
