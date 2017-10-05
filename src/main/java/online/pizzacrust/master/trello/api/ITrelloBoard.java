package online.pizzacrust.master.trello.api;

import java.util.List;

public interface ITrelloBoard {

    List<ITrelloList> getLists();

    ITrelloList createNewList(String name);

}
