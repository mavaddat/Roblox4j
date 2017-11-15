package online.pizzacrust.roblox.impl;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.roblox.api.Asset;
import online.pizzacrust.roblox.api.Robloxian;
import online.pizzacrust.roblox.impl.access.Roblox;

public class BasicAsset implements Asset {

    private final String name;
    private final String absoluteUrl;
    private final int ownerId;
    private final String ownerUsername;
    private final int assetId;
    private final String thumbnail;

    public BasicAsset(String name, String absoluteUrl, int ownerId, String ownerUsername, int assetId, String thumbnail) {
        this.name = name;
        this.absoluteUrl = absoluteUrl;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.assetId = assetId;
        this.thumbnail = thumbnail;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    @Override
    public Robloxian.LightReference getOwner() {
        return new BasicReference(ownerId, ownerUsername);
    }

    @Override
    public int getId() {
        return assetId;
    }

    @Override
    public String getThumbnail() {
        return thumbnail;
    }

    public static class AssetResponse {
        public static class AssetsData {
            public static class ItemData {
                public static class Metadata {
                    public int AssetId;
                    public String Name;
                    public String AbsoluteUrl;
                }
                public Metadata Item;
                public static class Thumbnail {
                    public String Url;
                }
                public Thumbnail Thumbnail;
                public static class Creator {
                    public int Id;
                    public String Name;
                }
                public Creator Creator;
            }
            public ItemData[] Items;
        }
        public AssetsData Data;
    }

    public static List<Asset> getAssets(List<String> jsons) {
        List<Asset> assets = new ArrayList<>();
        for (String json : jsons) {
            AssetResponse response = new Gson().fromJson(json, AssetResponse.class);
            for (AssetResponse.AssetsData.ItemData item : response.Data.Items) {
                assets.add(new BasicAsset(item.Item.Name, item.Item.AbsoluteUrl, item.Creator.Id,
                        item.Creator.Name, item.Item.AssetId, item.Thumbnail.Url));
            }
        }
        return assets;
    }

    public static Asset[] getAssetsToArray(List<String> jsons) {
        List<Asset> assets = getAssets(jsons);
        return assets.toArray(new Asset[assets.size()]);
    }

    public static void main(String... args ) throws Exception{
        Robloxian robloxian = Roblox.get("TGSCommander").get();
        Asset[] shirts = robloxian.getShirts();
        System.out.println(shirts.length);
        for (Asset asset : shirts) {
            System.out.println(asset.getName() + ": " + asset.getId() + ", owned by: " + asset
                    .getOwner().getUsername() + " aka. " + asset.getOwner().getUserId());
        }
    }

}
