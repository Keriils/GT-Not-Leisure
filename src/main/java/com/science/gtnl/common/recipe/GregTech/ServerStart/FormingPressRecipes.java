package com.science.gtnl.common.recipe.GregTech.ServerStart;

import com.science.gtnl.Utils.recipes.IRecipePool;
import com.science.gtnl.common.materials.MaterialPool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;

public class FormingPressRecipes implements IRecipePool {

    final RecipeMap<?> FPR = RecipeMaps.formingPressRecipes;

    @Override
    public void loadRecipes() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Titanium, 1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.YttriumBariumCuprate, 1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.NickelZincFerrite, 1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.NaquadahAlloy, 1),
                MaterialPool.Darmstadtium.get(OrePrefixes.bolt, 4))
            .itemOutputs(ItemList.Optical_Cpu_Containment_Housing.get(1))
            .duration(290)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(FPR);
    }
}
