package online.pizzacrust.master.trello.api;

public class TrelloCheckListItem {

    private final boolean marked;
    private final String id;
    private final String name;
    private final ITrelloCheckList parent;

    public TrelloCheckListItem(boolean marked, String id, String name, ITrelloCheckList parent) {
        this.marked = marked;
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public boolean isMarked() {
        return marked;
    }

    public String getId() {
        return id;
    }

    public ITrelloCheckList getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }
}
