package online.pizzacrust.roblox.api;

import java.util.List;

public interface Place {

    String getName();

    String getDescription();

    int getPlaceVisits();

    int getId();

    List<String> getThumbnailURLs() throws Exception;

}
