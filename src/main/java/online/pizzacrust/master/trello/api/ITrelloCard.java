package online.pizzacrust.master.trello.api;

public interface ITrelloCard {

    String getName() throws Exception;

    String getId() throws Exception;

    void save() throws Exception;

    void refresh() throws Exception;

    void setName(String name) throws Exception;

    String getDescription() throws Exception;

    void setDescription(String description) throws Exception;


}
