package online.pizzacrust.roblox.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import com.mashape.unirest.http.Unirest;

import java.util.Optional;

import online.pizzacrust.roblox.ProductAsset;
import online.pizzacrust.roblox.Robloxian;
import online.pizzacrust.roblox.group.Group;

public class BasicProductAsset implements ProductAsset {

    @SerializedName("AssetTypeId")
    private Type type;

    @SerializedName("Name")
    private String name;

    @SerializedName("Description")
    private String description;

    @SerializedName("AssetId")
    private int id;

    @SerializedName("Updated")
    private String lastUpdated;

    public static class CreatorMetadata {
        public int Id;
        public String Name;
        public String CreatorType;
    }

    @SerializedName("Creator")
    private CreatorMetadata creatorData;

    public BasicProductAsset() {}

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Optional<Group> getGroupOwner() {
        if (creatorData.CreatorType.equalsIgnoreCase("Group")) {
            return Optional.of(new BasicGroup(creatorData.Id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Robloxian.LightReference> getOwner() {
        if (creatorData.CreatorType.equalsIgnoreCase("User")) {
            return Optional.of(new BasicReference(creatorData.Id, creatorData.Name));
        }
        return Optional.empty();
    }

    @Override
    public String getLastUpdatedDate() {
        return lastUpdated;
    }

    public static ProductAsset getProductAsset(int assetId) throws Exception{
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Type.class,  new ProductAsset.Type.Deserializer());
        return gsonBuilder.create().fromJson(Unirest.get("https://api.roblox" +
                ".com/Marketplace/ProductInfo?assetId=" + assetId).asString().getBody(),
                BasicProductAsset.class);
    }

    public static void main(String... args) throws Exception {
        System.out.println(getProductAsset(1523992919).getType().name());
    }

}
