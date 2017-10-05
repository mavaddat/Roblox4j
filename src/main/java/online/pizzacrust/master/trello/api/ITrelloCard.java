package online.pizzacrust.master.trello.api;

import java.util.List;

public interface ITrelloCard {

    String getName() throws Exception;

    String getId() throws Exception;

    void save() throws Exception;

    void refresh() throws Exception;

    void setName(String name) throws Exception;

    String getDescription() throws Exception;

    void setDescription(String description) throws Exception;

    List<ITrelloCheckList> getCheckLists();

    void addCheckList(ITrelloCheckList checkList);

    void setCheckListItemState(TrelloCheckListItem item, boolean newState);

}
