package online.pizzacrust.roblox;

public interface Asset {

    String getName();

    String getAbsoluteUrl();

    Robloxian.LightReference getOwner();

    int getId();

    String getThumbnail();

}
