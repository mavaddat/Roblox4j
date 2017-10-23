package online.pizzacrust.roblox.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.roblox.Place;

public class BasicPlace implements Place {

    private final int id;
    private final int visits;
    private final String name;
    private final String description;

    public BasicPlace(int id, int visits, String name, String description) {
        this.id = id;
        this.visits = visits;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getPlaceVisits() {
        return visits;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<String> getThumbnailURLs() throws Exception {
        String url = "https://www.roblox.com/games/" + id + "/none";
        Document document = Jsoup.connect(url).ignoreContentType(true).get();
        List<String> thumbnailLinks = new ArrayList<>();
        Element root = document.getElementsByClass("carousel-inner").first();
        Elements items = root.getElementsByClass("item");
        for (Element item : items) {
            Element imgTag = item.getElementsByTag("img").first();
            thumbnailLinks.add(imgTag.attr("src"));
        }
        return thumbnailLinks;
    }

}
