package com.science.gtnl.loader;

import com.science.gtnl.Utils.recipes.IRecipePool;
import com.science.gtnl.Utils.recipes.RecipeUtil;
import com.science.gtnl.common.recipe.AprilFool.ExtremeRecipeImporter;
import com.science.gtnl.common.recipe.GTNL.ExtremeExtremeEntityCrusherRecipes;
import com.science.gtnl.common.recipe.GregTech.ServerStart.AlloyBlastSmelterRecipes;
import com.science.gtnl.common.recipe.GregTech.ServerStart.CircuitAssemblerConvertRecipes;
import com.science.gtnl.common.recipe.GregTech.ServerStart.CircuitAssemblyLineRecipes;
import com.science.gtnl.common.recipe.GregTech.ServerStart.FormingPressRecipes;
import com.science.gtnl.common.recipe.GregTech.ServerStart.VacuumFurnaceRecipes;
import com.science.gtnl.common.recipe.RecipeRegister;

import bartworks.API.recipe.BartWorksRecipeMaps;
import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeMaps;

public class RecipeLoaderServerStart {

    private static boolean recipesAdded;

    public static void loadRecipesServerStart() {

        if (!recipesAdded) {

            if (Mods.MobsInfo.isModLoaded()) {
                ExtremeExtremeEntityCrusherRecipes.init();
            }

            IRecipePool[] recipePools = new IRecipePool[] { new FormingPressRecipes(),
                new CircuitAssemblerConvertRecipes(), new AlloyBlastSmelterRecipes(), new VacuumFurnaceRecipes(),
                new ExtremeRecipeImporter() };

            for (IRecipePool recipePool : recipePools) {
                recipePool.loadRecipes();
            }

            RecipeUtil.generateRecipesNotUsingCells(
                BartWorksRecipeMaps.bioLabRecipes,
                RecipeRegister.LargeBioLabRecipes,
                true);

        }

        RecipeUtil.copyAllRecipes(RecipeRegister.ConvertToCircuitAssembler, RecipeMaps.circuitAssemblerRecipes);

        new CircuitAssemblyLineRecipes().loadRecipes();

        recipesAdded = true;
    }

}
