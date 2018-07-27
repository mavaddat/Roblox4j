package online.pizzacrust.roblox.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import online.pizzacrust.roblox.Asset;
import online.pizzacrust.roblox.Badge;
import online.pizzacrust.roblox.CachedGroupData;
import online.pizzacrust.roblox.ClubType;
import online.pizzacrust.roblox.Place;
import online.pizzacrust.roblox.Presence;
import online.pizzacrust.roblox.Robloxian;
import online.pizzacrust.roblox.auth.AuthenticationInfo;
import online.pizzacrust.roblox.errors.InvalidUserException;
import online.pizzacrust.roblox.group.Group;

/**
 * Cached version for more speed.
 * @param <HANDLE>
 */
public class CachedRobloxian<HANDLE extends Robloxian> implements Robloxian {

    public static final List<CachedRobloxian<?>> CACHED = new ArrayList<>();

    private final HANDLE handle;

    private final Map<String, Object> cacheMap = new HashMap<>();
    private final Map<String, ExceptionFunction<HANDLE, ?, Exception>> methodMap = new HashMap<>();

    public void refresh() {
        cacheMap.clear();
    }

    interface ExceptionFunction<P, R, E extends Exception> {

        R unsafeApply(P parameter) throws E;

        default R apply(P parameter) {
            try {
                return unsafeApply(parameter);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return null;
        }

    }

    public <T> T getEntry(String string, ExceptionFunction<HANDLE, T, Exception> backup) {
        T object =  (T) cacheMap.get(string);
        if (object == null) {
            methodMap.put(string, backup);
            T backupObject = backup.apply(handle);
            cacheMap.put(string, backupObject);
            return backupObject;
        }
        return object;
    }

    public static CachedRobloxian getRobloxian(String username) {
        try {
            return new CachedRobloxian(new BasicRobloxian(username));
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> T exec(String methodName, CachedRobloxian cachedRobloxian) {
        AtomicReference<Object> obj = null;
        cachedRobloxian.methodMap.forEach((key, value) -> {
            if (key.equals(methodName)) {
                obj.set(((ExceptionFunction) value).apply(cachedRobloxian));
            }
        });
        return (T) obj.get();
    }

    public static long refreshCache() {
        long time = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (CachedRobloxian<?> cachedRobloxian : CACHED) {
            cachedRobloxian.refresh();
            executorService.submit(() -> {
                Map<String, Object> newCacheMap = new HashMap<>();
                cachedRobloxian.cacheMap.forEach((method, value) -> {
                    newCacheMap.put(method, exec(method, cachedRobloxian));
                });
                cachedRobloxian.cacheMap.clear();
                cachedRobloxian.cacheMap.putAll(newCacheMap);
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() - time;
    }

    public static CachedRobloxian getRobloxian(int id) {
        try {
            return new CachedRobloxian(new BasicRobloxian(id));
        } catch (InvalidUserException e) {
            e.printStackTrace();
            return null;
        }
    }

    CachedRobloxian(HANDLE handle) {
        this.handle = handle;
    }

    @Override
    public LightReference toReference() {
        return handle.toReference();
    }

    @Override
    public List<LightReference> getBestFriends() throws Exception {
        return getEntry("friends", Robloxian::getBestFriends);
    }

    @Override
    public List<String> getRobloxBadges() throws Exception {
        return getEntry("badges", Robloxian::getRobloxBadges);
    }

    @Override
    public List<String> getPastUsernames() throws Exception {
        return getEntry("usernames", Robloxian::getPastUsernames);
    }

    @Override
    public boolean isInGroup(Group group) throws Exception {
        for (Group group1 : getGroups()) {
            if (group1.getId() == group.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getProfileUrl() {
        return getEntry("url", Robloxian::getProfileUrl);
    }

    @Override
    public Group[] getGroups() throws Exception {
        return getEntry("groups", Robloxian::getGroups);
    }

    @Override
    public Badge[] getBadges() throws Exception {
        return getEntry("gbadges", Robloxian::getBadges);
    }

    @Override
    public Place[] getPlaces() throws Exception {
        return getEntry("places", Robloxian::getPlaces);
    }

    @Override
    public Asset[] getShirts() throws Exception {
        return getEntry("shirts", Robloxian::getShirts);
    }

    @Override
    public Asset[] getPants() throws Exception {
        return getEntry("pants", Robloxian::getPants);
    }

    @Override
    public Asset[] getTshirts() throws Exception {
        return getEntry("tShirts", Robloxian::getTshirts);
    }

    @Override
    public int getJoinTimeInDays() throws Exception {
        return getEntry("joinTime", Robloxian::getJoinTimeInDays);
    }

    @Override
    public String getDescription() throws Exception {
        return getEntry("desc", Robloxian::getDescription);
    }

    @Override
    public ClubType getClub() throws Exception {
        return getEntry("club", Robloxian::getClub);
    }

    @Override
    public String getProfileImageUrl() throws Exception {
        return getEntry("profileImage", Robloxian::getProfileImageUrl);
    }

    @Override
    public CachedGroupData[] getCachedGroupData() throws Exception {
        return getEntry("groupCacheData", Robloxian::getCachedGroupData);
    }

    @Override
    public Presence getPresence(AuthenticationInfo authenticationInfo) throws Exception {
        return handle.getPresence(authenticationInfo);
    }

    @Override
    public int getUserId() {
        return getEntry("id", Robloxian::getUserId);
    }

    @Override
    public String getUsername() {
        return getEntry("username", Robloxian::getUsername);
    }
}
