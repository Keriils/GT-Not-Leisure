package com.science.gtnl.Utils.recipes;

import static gregtech.api.util.GTUtility.trans;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Whether recipe requires compact or not
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SteamFusionTierKey extends RecipeMetadataKey<Integer> {

    public static final SteamFusionTierKey INSTANCE = new SteamFusionTierKey();

    private SteamFusionTierKey() {
        super(Integer.class, "steam_fusion_key");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 0);
        if (tier != 0) recipeInfo.drawText(trans("708", "Requires compact HPR"));
    }
}
