package online.pizzacrust.roblox;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Optional;

import online.pizzacrust.roblox.group.Group;

public interface ProductAsset {

    enum Type {
        IMAGE(1),
        DECAL(13),
        GAME_PLACE(9),
        UNKNOWN(-1);

        private final int id;

        Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Type fromId(int id) {
            for (Type type : Type.values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return Type.UNKNOWN;
        }

        public static class Deserializer implements JsonDeserializer<Type> {
            @Override
            public Type deserialize(JsonElement jsonElement, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                int enumType = jsonElement.getAsInt();
                return fromId(enumType);
            }
        }
    }

    /**
     * Gets the type of product.
     * @return
     */
    Type getType();

    int getId();

    String getName();

    String getDescription();

    /**
     * Returns group owner, if the asset is owned by a group.
     * @return
     */
    Optional<Group> getGroupOwner();

    default boolean isOwnedByGroup() {
        return getGroupOwner().isPresent();
    }

    /**
     * Returns user owner, if the asset is owned by a user.
     * @return
     */
    Optional<Robloxian.LightReference> getOwner();

    /**
     * Date.
     * Can be compared between assets if the original asset is group owned.
     * {@link ProductAsset#getDescription()} will return Decal Image if the asset was uploaded
     * from a group.
     * Last updated should be the same.
     * @return
     */
    String getLastUpdatedDate();


}
