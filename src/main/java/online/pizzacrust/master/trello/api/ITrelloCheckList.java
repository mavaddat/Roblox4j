package online.pizzacrust.master.trello.api;

import java.util.List;

public interface ITrelloCheckList {

    List<TrelloCheckListItem> getItems();

    String getId();

    String getName();

}
