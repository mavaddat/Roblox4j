package online.pizzacrust.roblox.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.roblox.Roblox;
import online.pizzacrust.roblox.Robloxian;
import online.pizzacrust.roblox.forum.ForumResponse;
import online.pizzacrust.roblox.forum.ForumThread;

public class BasicForumThread implements ForumThread {

    private final int id;

    public BasicForumThread(int id) throws Exception {
        this.id = id;
    }

    @Override
    public List<ForumResponse> getForumResponses() throws Exception {
        String url = "https://forum.roblox.com/Forum/ShowPost.aspx?PostID=" + id;
        // first is get, second is post and the rest
        // first u need to get __VIEWSTATE, __VIEWSTATEGENERATOR, __EVENTVALIDATION
        Document document = Jsoup.connect(url).ignoreContentType(true).get();
        String viewStateGenerator = (document.getElementById("__VIEWSTATEGENERATOR").attr
                ("value"));
        String eventValidation = (document.getElementById("__EVENTVALIDATION").attr("value"));
        String viewState = (document.getElementById("__VIEWSTATE").attr("value"));
        // check if there are more pages
        if (document.getElementById("ctl00_cphRoblox_PostView1_ctl00_Pager_Next")
                 == null) {
            System.out.println("No more pages");
        }
        // now post to the link w/ __viewstate, __viewstategenerator, __eventvalidation, and also
        // target
        //Document otherDoc = Jsoup.connect(url).ignoreContentType(true).data
        //        ("__VIEWSTATEGENERATOR", viewStateGenerator).data("__VIEWSTATE", viewState)
        //        .data("__EVENTVALIDATION", eventValidation).data("__EVENTTARGET",
        //        "ctl00$cphRoblox$PostView1$ctl00$Pager$Next").post();
        //System.out.println(otherDoc.body());
        List<Document> documents = recursiveGet(viewState, eventValidation, viewStateGenerator,
                url);
        documents.add(document);
        return process(documents);
    }

    private String getUsername(Element element) {
        Elements elements = element.getElementsByTag("a");
        for (Element element1 : elements) {
            if (element1.attr("href").startsWith("https://www.roblox.com/users/")) {
                return element1.text();
            }
        }
        throw new RuntimeException();
    }

    private List<ForumResponse> process(List<Document> documents) {
        List<ForumResponse> forumResponses = new ArrayList<>();
        for (Document document : documents) {
            for (Element element : document.getElementsByClass("forum-post")) {
                BasicForumResponse response = new BasicForumResponse(getUsername(element),
                        element.getElementsByClass("normalTextSmall").first().text());
                forumResponses.add(response);
            }
        }
        return forumResponses;
    }

    private List<Document> recursiveGet(String viewState, String eventValidation, String
            viewStateGenerator, String url) throws Exception {
        List<Document> documents = new ArrayList<>();
        Document document = Jsoup.connect(url).ignoreContentType(true).data
                ("__VIEWSTATEGENERATOR", viewStateGenerator).data("__VIEWSTATE", viewState)
                .data("__EVENTVALIDATION", eventValidation).data("__EVENTTARGET", "ctl00$cphRoblox$PostView1$ctl00$Pager$Next").post();
        documents.add(document);
        if (document.getElementById("ctl00_cphRoblox_PostView1_ctl00_Pager_Next")
                == null) {
            return documents;
        }
        String nviewStateGenerator = (document.getElementById("__VIEWSTATEGENERATOR").attr
                ("value"));
        String neventValidation = (document.getElementById("__EVENTVALIDATION").attr("value"));
        String nviewState = (document.getElementById("__VIEWSTATE").attr("value"));
        documents.addAll(recursiveGet(nviewState, neventValidation, nviewStateGenerator, url));
        return documents;
    }

    public static void main(String... args) throws Exception {
        int large = 0;
        int acid = 0;
        int tricor = 0;
        for (ForumResponse forumResponse : new BasicForumThread(227830461).getForumResponses()) {
            if (forumResponse.getMessage().contains("largeTitanic2")) {
                large++;
            } else if (forumResponse.getMessage().contains("AcidRaps")) {
                acid++;
            } else if (forumResponse.getMessage().contains("LordTricor")) {
                tricor++;
            }
        }
        System.out.println("Total: " + (large + acid + tricor));
        System.out.println("AcidRaps: " + acid);
        System.out.println("largeTitanic2: " + large);
        System.out.println("LordTricor: " + tricor);
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
