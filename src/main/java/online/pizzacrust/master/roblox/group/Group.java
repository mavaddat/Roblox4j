package online.pizzacrust.master.roblox.group;

import java.util.List;
import java.util.Optional;

import online.pizzacrust.master.roblox.Robloxian;

public interface Group {

    List<Roleset> getRolesets();

    String getName();

    Robloxian.LightReference[] getMembers();

    Optional<Robloxian.LightReference> findByName(String name);

    Optional<Robloxian.LightReference> findById(int id);

}
