package online.pizzacrust.roblox.group;

import java.util.List;
import java.util.Optional;

import online.pizzacrust.roblox.Robloxian;

public interface Group {

    List<Roleset> getRolesets() throws Exception;

    String getName() throws Exception;

    /**
     * Gets the members of the group.
     * @return
     * @throws Exception
     */
    Robloxian.LightReference[] getMembersInRole(Roleset roleset) throws Exception;

    Optional<Roleset> getRole(Robloxian robloxian) throws Exception;

    int getId();

    List<Group> getAllies() throws Exception;

}
