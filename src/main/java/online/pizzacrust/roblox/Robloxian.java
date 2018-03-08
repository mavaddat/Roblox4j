package online.pizzacrust.roblox;

import java.util.List;
import java.util.Optional;

import online.pizzacrust.roblox.group.Group;
import online.pizzacrust.roblox.group.Roleset;

/**
 * Represents a user on ROBLOX.com
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public interface Robloxian extends Profile {

    /**
     * Represents a reference to a ROBLOX user, but details are not grabbed since it will be
     * time consuming. A light reference allows the developer to save time and grab the user when
     * he/she wants to.
     *
     * @since 1.0-SNAPSHOT
     * @author PizzaCrust
     */
    interface LightReference extends Profile {

        /**
         * Grabs the robloxian object, will take time and can return null.
         * @return
         */
        Robloxian grab() throws Exception;

    }

    /**
     * Converts the object to a reference, not really useful but you can do it..
     * @return
     */
    LightReference toReference();

    /**
     * Retrieves the best friends (200 max) of a player.
     * @return
     */
    List<LightReference> getBestFriends() throws Exception;

    /**
     * Retrieves the ROBLOX badges of the player.
     * @return
     */
    List<String> getRobloxBadges() throws Exception;

    /**
     * Retrieves the past usernames of the player.
     * @return
     */
    List<String> getPastUsernames() throws Exception;

    /**
     * Retrieves whether the current user in the specified group.
     * @return
     */
    boolean isInGroup(Group group) throws Exception;

    /**
     * Retrieves the role of the person.
     * @param group
     * @return
     * @throws Exception
     */
    default Optional<Roleset> getRoleInGroup(Group group) throws Exception {
        if (isInGroup(group)) {
            return Optional.ofNullable(group.getRole(this).orElse(null));
        }
        return Optional.empty();
    }

    /**
     * Retrieves profile URL
     * @return
     */
    String getProfileUrl();

    /**
     * Gets groups of a person. May take time to parse if at maximum 100.
     * @return
     */
    Group[] getGroups() throws Exception;

    /**
     * Takes some time. Maximum is 100 million.
     * @return
     * @throws Exception
     */
    Badge[] getBadges() throws Exception;

    Place[] getPlaces() throws Exception;

    Asset[] getShirts() throws Exception;

    Asset[] getPants() throws Exception;

    Asset[] getTshirts() throws Exception;

    int getJoinTimeInDays() throws Exception;

    String getDescription() throws Exception;

    ClubType getClub() throws Exception ;

    String getProfileImageUrl() throws Exception;

    CachedGroupData[] getCachedGroupData() throws Exception;

}
