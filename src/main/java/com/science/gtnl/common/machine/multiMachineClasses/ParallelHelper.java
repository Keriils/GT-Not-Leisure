package com.science.gtnl.common.machine.multiMachineClasses;

import static com.science.gtnl.Utils.Utils.setStackSize;
import static gregtech.api.util.GTRecipe.GTppRecipeHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.science.gtnl.Utils.Utils;
import com.science.gtnl.Utils.rewrites.GTNL_ItemID;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.VoidProtectionHelper;
import ic2.core.Ic2Items;

// spotless:off
public class ParallelHelper extends gregtech.api.util.ParallelHelper {

    // region Variables
    public static final double MAX_BATCH_MODE_TICK_TIME = 128;
    /**
     * Machine used for calculation
     */
    public IVoidable machine;
    /**
     * Machine used for single recipe locking calculation
     */
    public IRecipeLockable singleRecipeMachine;
    /**
     * Is locked to a single recipe?
     */
    public boolean isRecipeLocked;
    /**
     * Recipe used when trying to calculate parallels
     */
    public GTRecipe recipe;
    /**
     * EUt available to the multiblock (This should be the total eut available)
     */
    public long availableEUt;
    /**
     * The current parallel possible for the multiblock
     */
    public int currentParallel = 0;
    /**
     * The maximum possible parallel possible for the multiblock
     */
    public int maxParallel = 1;
    /**
     * The Batch Modifier applied when batch mode is enabled. 1 does nothing. 2 doubles max possible
     * parallel, but also duration
     */
    public int batchModifier = 1;
    /**
     * The inputs of the multiblock for the current recipe check
     */
    public ItemStack[] itemInputs;
    /**
     * The outputs of the recipe with the applied parallel
     */
    public ItemStack[] itemOutputs;
    /**
     * The inputs of the multiblock for the current recipe check
     */
    public FluidStack[] fluidInputs;
    /**
     * The outputs of the recipe with the applied parallel
     */
    public FluidStack[] fluidOutputs;
    /**
     * Does the multi have void protection enabled for items
     */
    public boolean protectExcessItem;
    /**
     * Does the multi have void protection enabled for fluids
     */
    public boolean protectExcessFluid;
    /**
     * Should the Parallel Helper automatically consume for the multi
     */
    public boolean consume;
    /**
     * Is batch mode turned on?
     */
    public boolean batchMode;
    /**
     * Should the Parallel Helper automatically calculate the outputs of the recipe with current parallel?
     */
    public boolean calculateOutputs;
    /**
     * Has the Parallel Helper been built?
     */
    public boolean built;
    /**
     * What is the duration multiplier with batch mode enabled
     */
    public double durationMultiplier;
    /**
     * Modifier which is applied on the recipe eut. Useful for GT++ machines
     */
    public float eutModifier = 1;
    /**
     * Method for calculating max parallel from given inputs.
     */
    public MaxParallelCalculator maxParallelCalculator = ParallelHelper::maxParallelCalculatedByInputs;
//    public MaxParallelCalculator maxParallelCalculator = GTRecipe::maxParallelCalculatedByInputs;
    /**
     * Method for consuming inputs after determining how many parallels it can execute.
     */
    public InputConsumer inputConsumer = ParallelHelper::consumeInput;

    /**
     * Calculator to use for overclocking
     */
    public OverclockCalculator calculator;

    public CheckRecipeResult result = CheckRecipeResultRegistry.NONE;

    public Function<Integer, ItemStack[]> customItemOutputCalculation;

    public Function<Integer, FluidStack[]> customFluidOutputCalculation;

    // endregion
    public ParallelHelper() {
    }

    // region Setters


    /**
     * Sets machine, with current configuration for void protection mode.
     */
    public gregtech.api.util.ParallelHelper setMachine(IVoidable machine) {
        return setMachine(machine, machine.protectsExcessItem(), machine.protectsExcessFluid());
    }

    /**
     * Sets machine, with void protection mode forcibly.
     */
    public gregtech.api.util.ParallelHelper setMachine(IVoidable machine, boolean protectExcessItem, boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.machine = machine;
        return this;
    }

    /**
     * Sets the recipe, which will be used for the parallel calculation
     */
    public gregtech.api.util.ParallelHelper setRecipe(@Nonnull GTRecipe aRecipe) {
        recipe = Objects.requireNonNull(aRecipe);
        return this;
    }

    public gregtech.api.util.ParallelHelper setRecipeLocked(IRecipeLockable singleRecipeMachine, boolean isRecipeLocked) {
        this.singleRecipeMachine = singleRecipeMachine;
        this.isRecipeLocked = isRecipeLocked;
        return this;
    }

    /**
     * Sets the items available for the recipe check
     */
    public gregtech.api.util.ParallelHelper setItemInputs(ItemStack... aItemInputs) {
        this.itemInputs = aItemInputs;
        return this;
    }

    /**
     * Sets the fluid inputs available for the recipe check
     */
    public gregtech.api.util.ParallelHelper setFluidInputs(FluidStack... aFluidInputs) {
        this.fluidInputs = aFluidInputs;
        return this;
    }

    /**
     * Sets the available eut when trying for more parallels
     */
    public gregtech.api.util.ParallelHelper setAvailableEUt(long aAvailableEUt) {
        this.availableEUt = aAvailableEUt;
        return this;
    }

    /**
     * Sets the modifier for recipe eut. 1 does nothing 0.9 is 10% less. 1.1 is 10% more
     */
    public gregtech.api.util.ParallelHelper setEUtModifier(float aEUtModifier) {
        this.eutModifier = aEUtModifier;
        return this;
    }

    public gregtech.api.util.ParallelHelper setCalculator(OverclockCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    /**
     * Use {@link #setConsumption(boolean)}
     */
    @Deprecated
    public gregtech.api.util.ParallelHelper enableConsumption() {
        return setConsumption(true);
    }

    /**
     * Set if we should consume inputs or not when trying for parallels
     *
     * @param consume Should we consume inputs
     */
    public gregtech.api.util.ParallelHelper setConsumption(boolean consume) {
        this.consume = consume;
        return this;
    }

    /**
     * Sets the MaxParallel a multi can handle
     */
    public gregtech.api.util.ParallelHelper setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
        return this;
    }

    /**
     * Enables Batch mode. Can do up to an additional processed recipes of mCurrentParallel * mBatchModifier A batch
     * modifier of 1 does nothing
     */
    public gregtech.api.util.ParallelHelper enableBatchMode(int batchModifier) {
        this.batchMode = batchModifier > 1;
        this.batchModifier = batchModifier;
        return this;
    }

    /**
     * Use {@link #setOutputCalculation(boolean)}
     */
    @Deprecated
    public gregtech.api.util.ParallelHelper enableOutputCalculation() {
        return setOutputCalculation(true);
    }

    /**
     * Sets if we should calculate outputs with the parallels we found or not
     *
     * @param calculateOutputs Should we calculate outputs with the helper or not
     */
    public gregtech.api.util.ParallelHelper setOutputCalculation(boolean calculateOutputs) {
        this.calculateOutputs = calculateOutputs;
        return this;
    }

    /**
     * Set a custom way to calculate item outputs. You are given the amount of parallels and must return an ItemStack
     * array
     */
    public gregtech.api.util.ParallelHelper setCustomItemOutputCalculation(Function<Integer, ItemStack[]> custom) {
        customItemOutputCalculation = custom;
        return this;
    }

    /**
     * Set a custom way to calculate item outputs. You are given the amount of parallels and must return a FluidStack
     * array
     */
    public gregtech.api.util.ParallelHelper setCustomFluidOutputCalculation(Function<Integer, FluidStack[]> custom) {
        customFluidOutputCalculation = custom;
        return this;
    }

    // endregion

    /**
     * Finishes the ParallelHelper. Anything changed after this will not effect anything
     */
    public gregtech.api.util.ParallelHelper build() {
        if (built) {
            throw new IllegalStateException("Tried to build twice");
        }
        if (recipe == null) {
            throw new IllegalStateException("Recipe is not set");
        }
        built = true;
        determineParallel();
        return this;
    }

    /**
     * @return The current parallels possible by the multiblock
     */
    public int getCurrentParallel() {
        if (!built) {
            throw new IllegalStateException("Tried to get parallels before building");
        }
        return currentParallel;
    }

    /**
     * @return The duration multiplier if batch mode was enabled for the multiblock
     */
    public double getDurationMultiplierDouble() {
        if (!built) {
            throw new IllegalStateException("Tried to get duration multiplier before building");
        }
        if (batchMode && durationMultiplier > 0) {
            return durationMultiplier;
        }
        return 1;
    }

    /**
     * @deprecated Use {@link #getDurationMultiplierDouble()}
     */
    @Deprecated
    public float getDurationMultiplier() {
        return (float) getDurationMultiplierDouble();
    }

    /**
     * @return The ItemOutputs from the recipe
     */
    @Nonnull
    public ItemStack[] getItemOutputs() {
        if (!built || !calculateOutputs) {
            throw new IllegalStateException(
                "Tried to get item outputs before building or without enabling calculation of outputs");
        }
        return itemOutputs;
    }

    /**
     * @return The FluidOutputs from the recipe
     */
    @Nonnull
    public FluidStack[] getFluidOutputs() {
        if (!built || !calculateOutputs) {
            throw new IllegalStateException(
                "Tried to get fluid outputs before building or without enabling calculation of outputs");
        }
        return fluidOutputs;
    }

    /**
     * @return The result of why a recipe could've failed or succeeded
     */
    @Nonnull
    public CheckRecipeResult getResult() {
        if (!built) {
            throw new IllegalStateException("Tried to get recipe result before building");
        }
        return result;
    }

    /**
     * Use {@link #tryConsumeRecipeInputs(GTRecipe, FluidStack[], ItemStack[], int)}
     */
    @Deprecated
    protected boolean tryConsumeRecipeInputs(GTRecipe recipe, FluidStack[] fluids, ItemStack[] items) {
        return tryConsumeRecipeInputs(recipe, fluids, items, 1);
    }

    /**
     * Try to consume the inputs of the recipe
     *
     * @param recipe      Processed recipe
     * @param fluids      fluid inputs that will be consumed
     * @param items       item inputs that will be consumed
     * @param minParallel minimum amount of parallels to do with this check
     * @return True if recipe was satisfied, else false
     */
    protected boolean tryConsumeRecipeInputs(GTRecipe recipe, FluidStack[] fluids, ItemStack[] items,
                                             int minParallel) {
        return recipe.isRecipeInputEqual(true, false, minParallel, fluids, items);
    }


    /**
     * Called by build(). Determines the parallels and everything else that needs to be done at build time
     */
    protected void determineParallel() {
        if (itemInputs == null) {
            itemInputs = new ItemStack[0];
        }
        if (fluidInputs == null) {
            fluidInputs = new FluidStack[0];
        }

        if (!consume) {
            copyInputs();
        }

        if (calculator == null) {
            calculator = new OverclockCalculator().setEUt(availableEUt)
                                                     .setRecipeEUt(recipe.mEUt)
                                                     .setDuration(recipe.mDuration)
                                                     .setEUtDiscount(eutModifier);
        }

        final int tRecipeEUt = (int) Math.ceil(recipe.mEUt * eutModifier);
        if (availableEUt < tRecipeEUt) {
            result = CheckRecipeResultRegistry.insufficientPower(tRecipeEUt);
            return;
        }

        // Save the original max parallel before calculating our overclocking under 1 tick
        int originalMaxParallel = maxParallel;
        double tickTimeAfterOC = calculator.setParallel(originalMaxParallel)
                                           .calculateDurationUnderOneTick();
        if (tickTimeAfterOC < 1) {
            maxParallel = Utils.safeInt((long) (maxParallel / tickTimeAfterOC), 1);
        }

        int maxParallelBeforeBatchMode = maxParallel;
        if (batchMode) {
            maxParallel = Utils.safeInt((long) maxParallel * batchModifier, 1);
        }

        final ItemStack[] truncatedItemOutputs = recipe.mOutputs != null
                                                     ? Arrays.copyOfRange(recipe.mOutputs, 0, Math.min(machine.getItemOutputLimit(), recipe.mOutputs.length))
                                                     : new ItemStack[0];
        final FluidStack[] truncatedFluidOutputs = recipe.mFluidOutputs != null ? Arrays
                                                                                      .copyOfRange(recipe.mFluidOutputs, 0, Math.min(machine.getFluidOutputLimit(), recipe.mFluidOutputs.length))
                                                       : new FluidStack[0];

        SingleRecipeCheck recipeCheck = null;
        SingleRecipeCheck.Builder tSingleRecipeCheckBuilder = null;
        if (isRecipeLocked && singleRecipeMachine != null) {
            recipeCheck = singleRecipeMachine.getSingleRecipeCheck();
            if (recipeCheck == null) {
                // Machine is configured to lock to a single recipe, but haven't built the recipe checker yet.
                // Build the checker on next successful recipe.
                RecipeMap<?> recipeMap = singleRecipeMachine.getRecipeMap();
                if (recipeMap != null) {
                    tSingleRecipeCheckBuilder = SingleRecipeCheck.builder(recipeMap)
                                                                 .setBefore(itemInputs, fluidInputs);
                }
            }
        }

        // Let's look at how many parallels we can get with void protection
        if (protectExcessItem || protectExcessFluid) {
            if (machine == null) {
                throw new IllegalStateException("Tried to calculate void protection, but machine is not set");
            }
            VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper();
            voidProtectionHelper.setMachine(machine)
                                .setItemOutputs(truncatedItemOutputs)
                                .setFluidOutputs(truncatedFluidOutputs)
                                .setMaxParallel(maxParallel)
                                .build();
            maxParallel = Math.min(voidProtectionHelper.getMaxParallel(), maxParallel);
            if (maxParallel <= 0) {
                result = CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                return;
            }
        }

        maxParallelBeforeBatchMode = Math.min(maxParallel, maxParallelBeforeBatchMode);

        // determine normal parallel
        int actualMaxParallel = tRecipeEUt > 0 ? (int) Math.min(maxParallelBeforeBatchMode, availableEUt / tRecipeEUt)
                                    : maxParallelBeforeBatchMode;
        if (recipeCheck != null) {
            currentParallel = recipeCheck.checkRecipeInputs(true, actualMaxParallel, itemInputs, fluidInputs);
        } else {
            currentParallel = (int) maxParallelCalculator.calculate(recipe, actualMaxParallel, fluidInputs, itemInputs);
            if (currentParallel > 0) {
                if (tSingleRecipeCheckBuilder != null) {
                    // If recipe checker is not built yet, build and set it
                    inputConsumer.consume(recipe, 1, fluidInputs, itemInputs);
                    SingleRecipeCheck builtCheck = tSingleRecipeCheckBuilder.setAfter(itemInputs, fluidInputs)
                                                                            .setRecipe(recipe)
                                                                            .build();
                    singleRecipeMachine.setSingleRecipeCheck(builtCheck);
                    inputConsumer.consume(recipe, currentParallel - 1, fluidInputs, itemInputs);
                } else {
                    inputConsumer.consume(recipe, currentParallel, fluidInputs, itemInputs);
                }
            }
        }

        if (currentParallel <= 0) {
            result = CheckRecipeResultRegistry.INTERNAL_ERROR;
            return;
        }


        long eutUseAfterOC = calculator.setCurrentParallel(currentParallel).setParallel(originalMaxParallel).calculate().getConsumption();
        calculator.setParallel(Math.min(currentParallel, originalMaxParallel))
                  .calculate();
        if (currentParallel > originalMaxParallel) {
            calculator.setRecipeEUt(eutUseAfterOC);
        }
        // If Batch Mode is enabled determine how many extra parallels we can get
        if (batchMode && currentParallel > 0 && calculator.getDuration() < MAX_BATCH_MODE_TICK_TIME) {
            int tExtraParallels;
            double batchMultiplierMax = MAX_BATCH_MODE_TICK_TIME / calculator.getDuration();
            final int maxExtraParallels = (int) Math.floor(
                Math.min(
                    currentParallel * Math.min(batchMultiplierMax - 1, batchModifier - 1),
                    maxParallel - currentParallel));
            if (recipeCheck != null) {
                tExtraParallels = recipeCheck.checkRecipeInputs(true, maxExtraParallels, itemInputs, fluidInputs);
            } else {
                tExtraParallels = (int) maxParallelCalculator
                                            .calculate(recipe, maxExtraParallels, fluidInputs, itemInputs);
                inputConsumer.consume(recipe, tExtraParallels, fluidInputs, itemInputs);
            }
            durationMultiplier = 1.0f + (float) tExtraParallels / currentParallel;
            currentParallel += tExtraParallels;
        }

        // If we want to calculate outputs we do it here
        if (calculateOutputs && currentParallel > 0) {
            if (recipe.mOutputs != null) {
                calculateItemOutputs();
            }
            if (recipe.mFluidOutputs != null) {
                calculateFluidOutputs();
            }
        }
        result = CheckRecipeResultRegistry.SUCCESSFUL;
    }

    protected void copyInputs() {
        ItemStack[] itemInputsToUse;
        FluidStack[] fluidInputsToUse;
        itemInputsToUse = new ItemStack[itemInputs.length];
        for (int i = 0; i < itemInputs.length; i++) {
            itemInputsToUse[i] = itemInputs[i].copy();
        }
        fluidInputsToUse = new FluidStack[fluidInputs.length];
        for (int i = 0; i < fluidInputs.length; i++) {
            fluidInputsToUse[i] = fluidInputs[i].copy();
        }
        itemInputs = itemInputsToUse;
        fluidInputs = fluidInputsToUse;
    }

    protected void calculateItemOutputs() {
        if (customItemOutputCalculation != null) {
            itemOutputs = customItemOutputCalculation.apply(currentParallel);
            return;
        }
        ArrayList<ItemStack> toOutput = new ArrayList<>();
        for (int i = 0; i < recipe.mOutputs.length; i++) {
            int outputChance = recipe.getOutputChance(i);
            ItemStack origin = recipe.getOutput(i).copy();
            if (outputChance < 10000) {
                // parameter of this item final amount
                long outputs = (long) currentParallel * origin.stackSize * outputChance / 10000;
                long remain = (long) currentParallel * origin.stackSize * outputChance % 10000;

                if (remain > 0) {
                    if (remain > XSTR.XSTR_INSTANCE.nextInt(10000)) {
                        outputs += origin.stackSize;
                    }
                }

                while (outputs >= Integer.MAX_VALUE) {
                    toOutput.add(setStackSize(origin.copy(), Integer.MAX_VALUE));
                    outputs -= Integer.MAX_VALUE;
                }

                if (outputs > 0) {
                    toOutput.add(setStackSize(origin.copy(), (int) outputs));
                }

            } else {
                long outputs = (long) currentParallel * origin.stackSize;
                while (outputs > Integer.MAX_VALUE) {
                    toOutput.add(setStackSize(origin.copy(), Integer.MAX_VALUE));
                    outputs -= Integer.MAX_VALUE;
                }
                toOutput.add(setStackSize(origin.copy(), (int) outputs));
            }

        }

        itemOutputs = toOutput.toArray(new ItemStack[0]);
    }

    protected void calculateFluidOutputs() {
        if (customFluidOutputCalculation != null) {
            fluidOutputs = customFluidOutputCalculation.apply(currentParallel);
            return;
        }
        ArrayList<FluidStack> toOutput = new ArrayList<>();
        fluidOutputs = new FluidStack[recipe.mFluidOutputs.length];
        for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
            if (recipe.getFluidOutput(i) != null) {
                FluidStack tFluid = recipe.getFluidOutput(i)
                    .copy();
                long amount = (long) tFluid.amount * currentParallel;
                while (amount >= Integer.MAX_VALUE) {
                    FluidStack fluid = tFluid.copy();
                    fluid.amount = Integer.MAX_VALUE;
                    toOutput.add(fluid);
                    amount -= Integer.MAX_VALUE;
                }
                FluidStack fluid = tFluid.copy();
                fluid.amount = (int) amount;
                toOutput.add(fluid);
            }
        }
        fluidOutputs = toOutput.toArray(new FluidStack[0]);
    }

    /**
     * Rewrite from {@link GTRecipe#consumeInput(int, FluidStack[], ItemStack...)}
     */
    public static void consumeInput(GTRecipe recipe, int amountMultiplier, FluidStack[] aFluidInputs, ItemStack... aInputs) {
        if (amountMultiplier <= 0) return;

        long remainingCost;

        if (aFluidInputs != null) {
            for (FluidStack recipeFluidCost : recipe.mFluidInputs) {
                if (recipeFluidCost != null) {
                    remainingCost = (long) recipeFluidCost.amount * amountMultiplier;

                    for (FluidStack providedFluid : aFluidInputs) {
                        if (providedFluid != null && providedFluid.isFluidEqual(recipeFluidCost)) {
                            if (providedFluid.amount >= remainingCost) {
                                providedFluid.amount -= (int) remainingCost;
                                break;
                            } else {
                                remainingCost -= providedFluid.amount;
                                providedFluid.amount = 0;
                            }
                        }
                    }
                }
            }
        }

        if (aInputs != null) {
            for (ItemStack recipeItemCost : recipe.mInputs) {
                if (recipeItemCost.stackSize == 0) continue;
                ItemStack unifiedItemCost = GTOreDictUnificator.get_nocopy(recipeItemCost);
                if (unifiedItemCost != null) {
                    remainingCost = (long) recipeItemCost.stackSize * amountMultiplier;

                    for (ItemStack providedItem : aInputs) {
                        if (recipe.isNBTSensitive && !GTUtility.areStacksEqual(providedItem, unifiedItemCost, false)) {
                            continue;
                        } else if (!recipe.isNBTSensitive
                                       && !GTOreDictUnificator.isInputStackEqual(providedItem, unifiedItemCost)) {
                            continue;
                        }

                        if (GTppRecipeHelper) { // Please see JavaDoc on GTppRecipeHelper for why this is here.
                            if (GTUtility.areStacksEqual(providedItem, Ic2Items.FluidCell.copy(), true)
                                    || GTUtility.areStacksEqual(providedItem, ItemList.Tool_DataStick.get(1L), true)
                                    || GTUtility.areStacksEqual(providedItem, ItemList.Tool_DataOrb.get(1L), true)) {
                                if (!GTUtility.areStacksEqual(providedItem, recipeItemCost, false)) continue;
                            }
                        }

                        if (providedItem.stackSize >= remainingCost) {
                            providedItem.stackSize -= (int) remainingCost;
                            break;
                        } else {
                            remainingCost -= providedItem.stackSize;
                            providedItem.stackSize = 0;
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the number of parallel recipes, or 0 if recipe is not satisfied at all. 0 < number < 1 means that inputs
     * are found but not enough. Refer to SingleRecipeCheck#checkRecipeInputs.
     */
    public static double maxParallelCalculatedByInputs(GTRecipe recipe, int maxParallel, FluidStack[] aFluidInputs, ItemStack... aInputs) {
        if (recipe.mInputs.length > 0 && aInputs == null) return 0;
        if (recipe.mFluidInputs.length > 0 && aFluidInputs == null) return 0;


        double currentParallel = maxParallel;

        if (recipe.mFluidInputs.length > 0) {
            // Create map for fluid -> stored amount
            Map<Fluid, Long> fluidMap = new HashMap<>();
            Map<Fluid, Long> fluidCost = new HashMap<>();
            for (FluidStack fluidStack : aFluidInputs) {
                if (fluidStack == null) continue;
                fluidMap.merge(fluidStack.getFluid(), (long) fluidStack.amount, Long::sum);
            }
            for (FluidStack fluidStack : recipe.mFluidInputs) {
                if (fluidStack == null) continue;
                fluidCost.merge(fluidStack.getFluid(), (long) fluidStack.amount, Long::sum);
            }

            // Check how many parallels can it perform for each fluid
            for (Map.Entry<Fluid, Long> costEntry : fluidCost.entrySet()) {
                if (costEntry.getValue() > 0) {
                    currentParallel = Math.min(
                        currentParallel,
                        (double) fluidMap.getOrDefault(costEntry.getKey(), 0L) / costEntry.getValue());
                }
                if (currentParallel <= 0) {
                    return 0;
                }
            }
        }

        boolean isNBTSensitive = recipe.isNBTSensitive;
        double remainingCost;
        long providedAmount;
        if (recipe.mInputs.length > 0) {
            Map<GTNL_ItemID, Long> itemCost = new HashMap<>();
            if (isNBTSensitive) {
                for (ItemStack recipeItemCost : recipe.mInputs) {
                    // for non-consumed input
                    if (recipeItemCost.stackSize == 0) continue;
                    itemCost.merge(GTNL_ItemID.create(recipeItemCost), (long) recipeItemCost.stackSize, Long::sum);
                }
            } else {
                for (ItemStack recipeItemCost : recipe.mInputs) {
                    // for non-consumed input
                    if (recipeItemCost.stackSize == 0) continue;
                    itemCost.merge(GTNL_ItemID.createNoNBT(recipeItemCost), (long) recipeItemCost.stackSize, Long::sum);
                }
            }

            nextRecipeItemCost: for (Map.Entry<GTNL_ItemID, Long> itemID : itemCost.entrySet()) {
                ItemStack unifiedItemCost;
                if (isNBTSensitive) {
                    unifiedItemCost = GTOreDictUnificator.get_nocopy(itemID.getKey().getItemStackWithNBT());
                } else {
                    unifiedItemCost = GTOreDictUnificator.get_nocopy(itemID.getKey().getItemStack());
                }
                remainingCost = itemID.getValue() * currentParallel;
                providedAmount = 0;
                for (ItemStack providedItem : aInputs) {
                    if (isNBTSensitive && !GTUtility.areStacksEqual(providedItem, unifiedItemCost, false)) {
                        continue;
                    } else if (!isNBTSensitive
                                   && !GTOreDictUnificator.isInputStackEqual(providedItem, unifiedItemCost)) {
                        continue;
                    }

                    providedAmount += providedItem.stackSize;

                    if (providedAmount >= remainingCost) continue nextRecipeItemCost;
                }
                if (providedAmount == 0) return 0;
                currentParallel = Math.min(currentParallel, (double) providedAmount / itemID.getValue());

            }
        }

        return currentParallel;

    }




}
// spotless:on
