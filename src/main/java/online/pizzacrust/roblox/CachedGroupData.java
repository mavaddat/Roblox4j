package online.pizzacrust.roblox;

public class CachedGroupData {

    private int Id;
    private String Name;
    private String Rank;

    public CachedGroupData(int id, String name, String rank) {
        Id = id;
        Name = name;
        Rank = rank;
    }

    public int getId() {
        return Id;
    }

    public String getRank() {
        return Rank;
    }

    public String getName() {
        return Name;
    }

    public CachedGroupData() {}

    @Override
    public String toString() {
        return "CachedGroupData{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Rank='" + Rank + '\'' +
                '}';
    }
}
