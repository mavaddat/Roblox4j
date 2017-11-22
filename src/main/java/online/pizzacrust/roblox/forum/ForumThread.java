package online.pizzacrust.roblox.forum;

import java.util.List;

public interface ForumThread extends ForumResponse {

    List<ForumResponse> getForumResponses() throws Exception;

}
