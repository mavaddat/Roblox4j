package online.pizzacrust.roblox.api;

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
