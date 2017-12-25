package online.pizzacrust.roblox.impl;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import online.pizzacrust.roblox.forum.ForumResponse;
import online.pizzacrust.roblox.forum.ForumThread;

public class BasicDiscourseForumThread implements ForumThread {

    private final String coreUrl; //ex: https://forums.robloxnusa.org/
    private final String postId;
    private final String postUrl;

    public String getCoreUrl() {
        return coreUrl;
    }

    public String getPostId() {
        return postId;
    }

    public String getPostUrl() {
        return postUrl;
    }

    private final String username;
    private final String message;
    private final List<ForumResponse> responses;

    public BasicDiscourseForumThread(String coreUrl, String postId) throws IOException {
        this.coreUrl = coreUrl;
        this.postId = postId;
        this.postUrl = coreUrl + "/t/" + postId + ".json";

        ThreadJsonResponse response = new Gson().fromJson(Jsoup.connect(postUrl)
                .ignoreContentType(true).get().body().text(), ThreadJsonResponse.class);
        List<ThreadJsonResponse.PostsStreamJson.ThreadReplyJson> list = new ArrayList<>(Arrays.asList(response.post_stream.posts));
        ThreadJsonResponse.PostsStreamJson.ThreadReplyJson origin = list.get(0);
        list.remove(0);
        this.username = origin.username;
        this.message = origin.cooked;
        List<ForumResponse> responses = new ArrayList<>();
        for (ThreadJsonResponse.PostsStreamJson.ThreadReplyJson threadReplyJson : list) {
            responses.add(new BasicForumResponse(threadReplyJson.username, threadReplyJson.cooked));
        }
        this.responses = responses;
    }

    @Override
    public List<ForumResponse> getForumResponses() {
        return responses;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static void main(String... args) throws Exception {
        BasicDiscourseForumThread discourseForumThread = new BasicDiscourseForumThread
                ("https://forum.robloxnusa.org/", "109");
        for (ForumResponse forumResponse : discourseForumThread.getForumResponses()) {
            System.out.println(forumResponse.getMessage() + " from " + forumResponse.getUsername());
        }
    }

    public static class ThreadJsonResponse {
        public String title;
        public static class PostsStreamJson {
            public static class ThreadReplyJson {
                public String username;
                public String cooked;
            }
            public ThreadReplyJson[] posts;
        }
        public PostsStreamJson post_stream;
    }

}
