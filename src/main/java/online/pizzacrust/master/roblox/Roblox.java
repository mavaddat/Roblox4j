package online.pizzacrust.master.roblox;

import java.util.Optional;

import online.pizzacrust.master.roblox.errors.InvalidUserException;
import online.pizzacrust.master.roblox.group.Group;
import online.pizzacrust.master.roblox.impl.BasicGroup;
import online.pizzacrust.master.roblox.impl.BasicRobloxian;

public class Roblox {

    public static Optional<Robloxian> get(String username) {
        try {
            return Optional.of(new BasicRobloxian(username));
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Group get(int groupId) {
        return new BasicGroup(groupId);
    }

}
