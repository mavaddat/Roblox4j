package online.pizzacrust.master.roblox;

public class Badge {

    private final int assetId;
    private final String name;

    public Badge(int assetId, String name) {
        this.assetId = assetId;
        this.name = name;
    }

    public int getAssetId() {
        return assetId;
    }

    public String getName() {
        return name;
    }

}
