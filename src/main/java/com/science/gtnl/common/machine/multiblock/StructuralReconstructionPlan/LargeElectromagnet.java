package com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.science.gtnl.ScienceNotLeisure.RESOURCE_ROOT_ID;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gtPlusPlus.core.block.ModBlocks.blockCasingsMisc;
import static gtnhlanth.common.register.LanthItemList.ELECTRODE_CASING;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.science.gtnl.Utils.StructureUtils;
import com.science.gtnl.Utils.item.TextLocalization;
import com.science.gtnl.common.machine.multiMachineClasses.GTMMultiMachineBase;
import com.science.gtnl.config.MainConfig;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

public class LargeElectromagnet extends GTMMultiMachineBase<LargeElectromagnet> implements ISurvivalConstructable {

    public static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<LargeElectromagnet> STRUCTURE_DEFINITION = null;
    public static final String LEM_STRUCTURE_FILE_PATH = RESOURCE_ROOT_ID + ":" + "multiblock/large_electromagnet";
    public static final int CASING_INDEX = TAE.GTPP_INDEX(5);
    private static final int MACHINEMODE_ELECTROMAGNETIC = 0;
    private static final int MACHINEMODE_POLARIZER = 1;
    public final int horizontalOffSet = 2;
    public final int verticalOffSet = 1;
    public final int depthOffSet = 0;
    public static String[][] shape = StructureUtils.readStructureFromFile(LEM_STRUCTURE_FILE_PATH);

    public LargeElectromagnet(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public LargeElectromagnet(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new LargeElectromagnet(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EMS_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EMS_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureID()),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EMS)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EMS_GLOW)
                    .extFacing()
                    .glow()
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
        return (machineMode == MACHINEMODE_ELECTROMAGNETIC) ? RecipeMaps.electroMagneticSeparatorRecipes
            : RecipeMaps.polarizerRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.electroMagneticSeparatorRecipes, RecipeMaps.polarizerRecipes);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.LargeElectromagnetRecipeType)
            .addInfo(TextLocalization.Tooltip_GTMMultiMachine_00)
            .addInfo(TextLocalization.Tooltip_GTMMultiMachine_01)
            .addInfo(TextLocalization.Tooltip_GTMMultiMachine_02)
            .addInfo(TextLocalization.Tooltip_GTMMultiMachine_03)
            .addInfo(TextLocalization.Tooltip_GTMMultiMachine_04)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .beginStructureBlock(5, 3, 4, true)
            .addInputBus(TextLocalization.Tooltip_LargeElectromagnet_Casing)
            .addOutputBus(TextLocalization.Tooltip_LargeElectromagnet_Casing)
            .addEnergyHatch(TextLocalization.Tooltip_LargeElectromagnet_Casing)
            .addMaintenanceHatch(TextLocalization.Tooltip_LargeElectromagnet_Casing)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<LargeElectromagnet> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<LargeElectromagnet>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement('A', ofBlockAnyMeta(ELECTRODE_CASING))
                .addElement(
                    'B',
                    buildHatchAdder(LargeElectromagnet.class).casingIndex(CASING_INDEX)
                        .dot(1)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(blockCasingsMisc, 5))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        ParallelTier = 0;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet) && checkHatch()) {
            return false;
        }

        energyHatchTier = checkEnergyHatchTier();
        if (MainConfig.enableMachineAmpLimit) {
            for (MTEHatch hatch : getExoticEnergyHatches()) {
                if (hatch instanceof MTEHatchEnergyTunnel) {
                    return false;
                }
            }
            if (getMaxInputAmps() > 64) return false;
        }

        ParallelTier = getParallelTier(aStack);
        return mCasing >= 30;
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
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.clear();
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_METAL);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mode", machineMode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getInteger("mode");
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        setMachineModeIcons();
        builder.widget(createModeSwitchButton(builder));
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.machineMode = (byte) ((this.machineMode + 1) % 2);
        GTUtility
            .sendChatToPlayer(aPlayer, StatCollector.translateToLocal("LargeElectromagnet_Mode_" + this.machineMode));
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("LargeElectromagnet_Mode_" + machineMode);
    }

}
