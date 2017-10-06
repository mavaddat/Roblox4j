package online.pizzacrust.roblox;

import java.util.Optional;

import online.pizzacrust.roblox.errors.InvalidUserException;
import online.pizzacrust.roblox.group.Group;
import online.pizzacrust.roblox.impl.BasicGroup;
import online.pizzacrust.roblox.impl.BasicRobloxian;

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
