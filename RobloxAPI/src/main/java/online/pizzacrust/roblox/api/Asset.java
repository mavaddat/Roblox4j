package online.pizzacrust.roblox.api;

public interface Asset {

    String getName();

    String getAbsoluteUrl();

    Robloxian.LightReference getOwner();

    int getId();

    String getThumbnail();

}
