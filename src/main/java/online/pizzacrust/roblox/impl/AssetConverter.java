package online.pizzacrust.roblox.impl;

import java.util.Optional;

import online.pizzacrust.roblox.ProductAsset;

/**
 * (only supports decals (group or user owned) -> asset id, currently)
 * Converts a {@link ProductAsset} with a certain type to a defined {@link ProductAsset.Type}.
 * Some usages: converting decal into image asset ID.
 *
 * -1 the original id until it reaches the defined type with the necessary requirements.
 * Can't go under 0.
 *
 * If the group owns the asset, that means requirements are equal update times (day and minute) and
 * required "Decal Image" in the asset description and Image Asset Type, i.e.
 * If the user owns the asset, the type must match as Image Asset Type, and created by the same
 * user, same update time (day and minute).
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class AssetConverter {

    private ProductAsset originalAsset;
    private ProductAsset.Type convertTo;

    public AssetConverter(ProductAsset asset,
                          ProductAsset.Type convertTo) {
        if (asset.getType() != ProductAsset.Type.DECAL) {
            throw new UnsupportedOperationException("Only decal conversion operations are " +
                    "supported.");
        }
        if (convertTo != ProductAsset.Type.IMAGE) {
            throw new UnsupportedOperationException("Only decal -> asset id (image type) " +
                    "conversion " +
                    "operations" +
                    " are supported.");
        }
        if (asset.getType() == convertTo) {
            throw new UnsupportedOperationException("There is no point in casting the own type of" +
                    " the current type of the asset.");
        }
        this.originalAsset = asset;
        this.convertTo = convertTo;
    }

    private ProductAsset recursiveSearch(ProductAsset lastAsset) throws Exception {
        if (lastAsset.getId() == 1487) {
            throw new UnsupportedOperationException("Can't go under/on asset 1487.");
        }
        int attemptId = lastAsset.getId() - 1;
        ProductAsset attemptAsset = BasicProductAsset.getProductAsset(attemptId);
        if (attemptAsset.getType() == convertTo) {
            // could be
            // check if it is owned by group, if so, do group reqs for asset check
            // check if it is owned by user, if so, do user requirements for asset check
            String date = attemptAsset.getLastUpdatedDate().split("T")[0];
            String oriDate = originalAsset.getLastUpdatedDate().split("T")[0];
            String desc = attemptAsset.getDescription();
            if (originalAsset.isOwnedByGroup()) {
                // group
                if (desc.equalsIgnoreCase("Decal Image") && date.equalsIgnoreCase(oriDate)) {
                    return attemptAsset;
                }
            } else {
                // user
                if (attemptAsset.getOwner().get().getUserId() == originalAsset.getOwner().get()
                        .getUserId() && date.equalsIgnoreCase(oriDate)) {
                    return attemptAsset;
                }
            }
        }
        return recursiveSearch(attemptAsset);
    }

    public Optional<ProductAsset> cast() {
        try {
            return Optional.of(recursiveSearch(originalAsset));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static void main(String... args) throws Exception {
        AssetConverter assetConverter = new AssetConverter(BasicProductAsset.getProductAsset
                (1523992919), ProductAsset.Type.IMAGE);
        System.out.println(assetConverter.cast().get().getId());
    }

}
