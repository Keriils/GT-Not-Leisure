package com.science.gtnl.common.machine.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.science.gtnl.ScienceNotLeisure.RESOURCE_ROOT_ID;
import static com.science.gtnl.common.block.Casings.BasicBlocks.MetaCasing;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.science.gtnl.Utils.StructureUtils;
import com.science.gtnl.Utils.item.TextLocalization;
import com.science.gtnl.common.machine.multiMachineClasses.MultiMachineBase;
import com.science.gtnl.common.recipe.RecipeRegister;
import com.science.gtnl.config.MainConfig;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings3;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

public class RareEarthCentrifugal extends MultiMachineBase<RareEarthCentrifugal> implements ISurvivalConstructable {

    public int mCasing;
    public static IStructureDefinition<RareEarthCentrifugal> STRUCTURE_DEFINITION = null;
    public static final String STRUCTURE_PIECE_MAIN = "main";
    public static final String REC_STRUCTURE_FILE_PATH = RESOURCE_ROOT_ID + ":" + "multiblock/rare_earth_centrifugal";
    public static String[][] shape = StructureUtils.readStructureFromFile(REC_STRUCTURE_FILE_PATH);
    public final int horizontalOffSet = 2;
    public final int verticalOffSet = 2;
    public final int depthOffSet = 0;
    public static final int CASING_INDEX = ((BlockCasings3) GregTechAPI.sBlockCasings3).getTextureIndex(12);

    public RareEarthCentrifugal(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public RareEarthCentrifugal(String aName) {
        super(aName);
    }

    @Override
    public boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    public float getSpeedBonus() {
        return 1;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new RareEarthCentrifugal(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialThermalCentrifugeActive)
                    .extFacing()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialThermalCentrifuge)
                    .extFacing()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()) };
    }

    @Override
    public int getCasingTextureID() {
        return CASING_INDEX;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeRegister.RareEarthCentrifugalRecipes;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.RareEarthCentrifugalRecipeType)
            .addInfo(TextLocalization.Tooltip_GTMMultiMachine_04)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(5, 5, 5, true)
            .addInputHatch(TextLocalization.Tooltip_RareEarthCentrifugal_Casing)
            .addOutputHatch(TextLocalization.Tooltip_RareEarthCentrifugal_Casing)
            .addInputBus(TextLocalization.Tooltip_RareEarthCentrifugal_Casing)
            .addOutputBus(TextLocalization.Tooltip_RareEarthCentrifugal_Casing)
            .addEnergyHatch(TextLocalization.Tooltip_RareEarthCentrifugal_Casing)
            .addMaintenanceHatch(TextLocalization.Tooltip_RareEarthCentrifugal_Casing)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<RareEarthCentrifugal> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<RareEarthCentrifugal>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement('A', ofBlock(MetaCasing, 4))
                .addElement('B', ofBlock(MetaCasing, 12))
                .addElement(
                    'C',
                    buildHatchAdder(RareEarthCentrifugal.class).casingIndex(CASING_INDEX)
                        .dot(1)
                        .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                        .buildAndChain(
                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockSpecialMultiCasings, 11))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet) && checkHatch()) {
            return false;
        }

        if (MainConfig.enableMachineAmpLimit) {
            for (MTEHatch hatch : getExoticEnergyHatches()) {
                if (hatch instanceof MTEHatchEnergyTunnel) {
                    return false;
                }
            }
            if (getMaxInputAmps() > 64) return false;
        }

        if (this.mEnergyHatches.size() > 2) return false;
        return mCasing >= 75;
    }

    @Override
    public int getMaxParallelRecipes() {
        int tier = Math.max(0, GTUtility.getTier(this.getMaxInputVoltage() - 6));
        return 4 + 4 * tier;
    }

    @Override
    public long getMaxInputAmps() {
        return getMaxInputAmpsHatch(getExoticAndNormalEnergyHatchList());
    }

    public static long getMaxInputAmpsHatch(Collection<? extends MTEHatch> hatches) {
        List<Long> ampsList = new ArrayList<>();
        for (MTEHatch tHatch : validMTEList(hatches)) {
            long currentAmp = tHatch.getBaseMetaTileEntity()
                .getInputAmperage();
            ampsList.add(currentAmp);
        }

        if (ampsList.isEmpty()) {
            return 0L;
        }

        return Collections.max(ampsList);
    }
}
