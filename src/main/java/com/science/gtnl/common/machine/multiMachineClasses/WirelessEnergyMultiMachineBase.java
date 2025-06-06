package com.science.gtnl.common.machine.multiMachineClasses;

import static com.science.gtnl.Utils.Utils.NEGATIVE_ONE;
import static com.science.gtnl.Utils.Utils.mergeArray;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.science.gtnl.Utils.item.TextLocalization;
import com.science.gtnl.common.GTNLItemList;
import com.science.gtnl.common.machine.hatch.ParallelControllerHatch;
import com.science.gtnl.misc.OverclockType;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class WirelessEnergyMultiMachineBase<T extends WirelessEnergyMultiMachineBase<T>>
    extends MultiMachineBase<T> {

    protected int ParallelTier;

    public WirelessEnergyMultiMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public WirelessEnergyMultiMachineBase(String aName) {
        super(aName);
    }

    public static final String ZERO_STRING = "0";

    protected UUID ownerUUID;
    protected boolean isRecipeProcessing = false;
    protected boolean wirelessMode = getDefaultWirelessMode();
    protected BigInteger costingEU = BigInteger.ZERO;
    protected String costingEUText = ZERO_STRING;
    protected int cycleNum = 100_000;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("wirelessMode", wirelessMode);
        aNBT.setInteger("parallelTier", ParallelTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        wirelessMode = aNBT.getBoolean("wirelessMode");
        ParallelTier = aNBT.getInteger("parallelTier");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mParallelControllerHatches.size() == 1 && aTick % 20 == 0) {
                for (ParallelControllerHatch module : mParallelControllerHatches) {
                    setMaxParallel(module.getParallel());
                    ParallelTier = module.mTier;
                }
            } else {
                setMaxParallel(8);
            }
            if (mEfficiency < 0) mEfficiency = 0;
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("wirelessMode")) {
            currentTip.add(EnumChatFormatting.LIGHT_PURPLE + TextLocalization.Waila_WirelessMode);
            currentTip.add(
                EnumChatFormatting.AQUA + TextLocalization.Waila_CurrentEuCost
                    + EnumChatFormatting.RESET
                    + ": "
                    + EnumChatFormatting.GOLD
                    + tag.getString("costingEUText")
                    + EnumChatFormatting.RESET
                    + " EU");
        }
    }

    @Override
    public float getSpeedBonus() {
        return 1;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setBoolean("wirelessMode", wirelessMode);
            if (wirelessMode) tag.setString("costingEUText", costingEUText);
        }
    }

    @Override
    protected void startRecipeProcessing() {
        isRecipeProcessing = true;
        super.startRecipeProcessing();
    }

    @Override
    protected void endRecipeProcessing() {
        super.endRecipeProcessing();
        isRecipeProcessing = false;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTNLProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {

                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclockType(OverclockType.PerfectOverclock);
                return super.process();
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return wirelessMode ? OverclockCalculator.ofNoOverclock(recipe)
                    : super.createOverclockCalculator(recipe).setEUtDiscount(0.4 - (ParallelTier / 50.0))
                        .setSpeedBoost(1.0 / 10.0 * Math.pow(0.75, ParallelTier));
            }
        }.setMaxParallelSupplier(this::getLimitedMaxParallel);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        if (wirelessMode) {
            logic.setAvailableVoltage(getAverageInputVoltage());
            logic.setAvailableAmperage(getMaxInputAmps());
            logic.setAmperageOC(false);
        } else {
            boolean useSingleAmp = mEnergyHatches.size() == 1 && mExoticEnergyHatches.isEmpty();
            logic.setAvailableVoltage(getMachineVoltageLimit());
            logic.setAvailableAmperage(useSingleAmp ? 1 : getMaxInputAmps());
            logic.setAmperageOC(useSingleAmp);
        }
    }

    @Override
    public int getMaxParallelRecipes() {
        if (mParallelControllerHatches.size() == 1) {
            for (ParallelControllerHatch module : mParallelControllerHatches) {
                ParallelTier = module.mTier;
                return module.getParallel();
            }
        } else if (ParallelTier <= 1) {
            return 8;
        } else {
            return (int) Math.pow(4, ParallelTier - 2);
        }
        return 8;
    }

    public int getParallelTier(ItemStack inventory) {
        if (inventory == null) return 0;
        if (inventory.isItemEqual(GTNLItemList.LVParallelControllerCore.getInternalStack_unsafe())) {
            return 1;
        } else if (inventory.isItemEqual(GTNLItemList.MVParallelControllerCore.getInternalStack_unsafe())) {
            return 2;
        } else if (inventory.isItemEqual(GTNLItemList.HVParallelControllerCore.getInternalStack_unsafe())) {
            return 3;
        } else if (inventory.isItemEqual(GTNLItemList.EVParallelControllerCore.getInternalStack_unsafe())) {
            return 4;
        } else if (inventory.isItemEqual(GTNLItemList.IVParallelControllerCore.getInternalStack_unsafe())) {
            return 5;
        } else if (inventory.isItemEqual(GTNLItemList.LuVParallelControllerCore.getInternalStack_unsafe())) {
            return 6;
        } else if (inventory.isItemEqual(GTNLItemList.ZPMParallelControllerCore.getInternalStack_unsafe())) {
            return 7;
        } else if (inventory.isItemEqual(GTNLItemList.UVParallelControllerCore.getInternalStack_unsafe())) {
            return 8;
        } else if (inventory.isItemEqual(GTNLItemList.UHVParallelControllerCore.getInternalStack_unsafe())) {
            return 9;
        } else if (inventory.isItemEqual(GTNLItemList.UEVParallelControllerCore.getInternalStack_unsafe())) {
            return 10;
        } else if (inventory.isItemEqual(GTNLItemList.UIVParallelControllerCore.getInternalStack_unsafe())) {
            return 11;
        } else if (inventory.isItemEqual(GTNLItemList.UMVParallelControllerCore.getInternalStack_unsafe())) {
            return 12;
        } else if (inventory.isItemEqual(GTNLItemList.UXVParallelControllerCore.getInternalStack_unsafe())) {
            return 13;
        } else if (inventory.isItemEqual(GTNLItemList.MAXParallelControllerCore.getInternalStack_unsafe())) {
            return 14;
        }
        return 0;
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        ItemStack controllerItem = getControllerSlot();
        int ParallelTierItem = getParallelTier(controllerItem);
        ParallelTier = Math.max(ParallelTier, ParallelTierItem);
        costingEU = BigInteger.ZERO;
        costingEUText = ZERO_STRING;
        prepareProcessing();
        if (!wirelessMode) return super.checkProcessing();

        boolean succeeded = false;
        CheckRecipeResult finalResult = CheckRecipeResultRegistry.SUCCESSFUL;
        for (int i = 0; i < cycleNum; i++) {
            CheckRecipeResult r = wirelessModeProcessOnce();
            if (!r.wasSuccessful()) {
                finalResult = r;
                break;
            }
            succeeded = true;
        }

        updateSlots();
        if (!succeeded) return finalResult;
        costingEUText = GTUtility.formatNumbers(costingEU);

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = getWirelessModeProcessingTime();

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public CheckRecipeResult wirelessModeProcessOnce() {
        if (!isRecipeProcessing) startRecipeProcessing();
        setupProcessingLogic(processingLogic);
        setupWirelessProcessingPowerLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        if (!result.wasSuccessful()) {
            return result;
        }

        BigInteger costEU = BigInteger.valueOf(processingLogic.getCalculatedEut())
            .multiply(BigInteger.valueOf(processingLogic.getDuration()));

        int m = getExtraEUCostMultiplier();
        if (m > 1) {
            costEU = costEU.multiply(BigInteger.valueOf(m));
        }

        if (!addEUToGlobalEnergyMap(ownerUUID, costEU.multiply(NEGATIVE_ONE))) {
            return CheckRecipeResultRegistry.insufficientPower(costEU.longValue());
        }

        costingEU = costingEU.add(costEU);

        mOutputItems = mergeArray(mOutputItems, processingLogic.getOutputItems());
        mOutputFluids = mergeArray(mOutputFluids, processingLogic.getOutputFluids());

        endRecipeProcessing();
        return result;
    }

    protected void prepareProcessing() {}

    protected void setupWirelessProcessingPowerLogic(ProcessingLogic logic) {
        // wireless mode ignore voltage limit
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(false);
    }

    public int getExtraEUCostMultiplier() {
        return 1;
    }

    public abstract int getWirelessModeProcessingTime();

    public boolean getDefaultWirelessMode() {
        return false;
    }

    @Override
    public void checkMaintenance() {}

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean shouldCheckMaintenance() {
        return false;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

}
