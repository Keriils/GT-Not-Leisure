package com.science.gtnl.common.machine.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.science.gtnl.ScienceNotLeisure.RESOURCE_ROOT_ID;
import static com.science.gtnl.common.block.Casings.BasicBlocks.LaserBeacon;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Mods.*;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.science.gtnl.Utils.item.TextLocalization;
import com.science.gtnl.common.GTNLItemList;
import com.science.gtnl.common.block.blocks.laserBeacon.TileEntityLaserBeacon;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.BlockCasings8;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MeteorMiner extends MTEEnhancedMultiBlockBase<MeteorMiner> implements ISurvivalConstructable {

    public static final String STRUCTURE_PIECE_MAIN = "main";
    public static final String STRUCTURE_PIECE_TIER2 = "tier2";
    public static IStructureDefinition<MeteorMiner> STRUCTURE_DEFINITION = null;
    public TileEntityLaserBeacon renderer;
    public static final int MAX_RADIUS = 34;
    public int currentRadius = MAX_RADIUS - 2;
    public int xDrill, yDrill, zDrill;
    public int xStart, yStart, zStart;
    public int fortuneTier = 0;
    public boolean isStartInitialized = false;
    public boolean hasFinished = true;
    public boolean isWaiting = false;
    public boolean isResetting = false;
    Collection<ItemStack> res = new HashSet<>();
    public int multiTier = 0;

    @Override
    public IStructureDefinition<MeteorMiner> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MeteorMiner>builder()
                // spotless:off
                .addShape(STRUCTURE_PIECE_MAIN, (transpose(new String[][] {
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         D         ","        D D        ","         D         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         D         ","        D D        ","       D   D       ","        D D        ","         D         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","         D         ","       D   D       ","                   ","      D     D      ","                   ","       D   D       ","         D         ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","         D         ","      D     D      ","                   ","                   ","     D   G   D     ","                   ","                   ","      D     D      ","         D         ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","         D         ","     D       D     ","                   ","                   ","    D              ","    D    B    D    ","                   ","                   ","                   ","     D       D     ","         D         ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","         D         ","    D         D    ","                   ","                   ","                   ","         C         ","   D    CBC    D   ","         C         ","                   ","                   ","                   ","    D         D    ","         D         ","                   ","                   ","                   "},
                            {"                   ","                   ","    DDDDDDDDDDD    ","   DDFFFFFFFFFDD   ","  DDFF       FFDD  ","  DFF         FFD  ","  DF           FD  ","  DF           FD  ","  DF     C     FD  ","  DF    CBC    FD  ","  DF     C     FD  ","  DF           FD  ","  DF           FD  ","  DFF         FFD  ","  DDFF       FFDD  ","   DDFFFFFFFFFDD   ","    DDDDDDDDDDD    ","                   ","                   "},
                            {"                   ","                   ","                   ","         D         ","      FFFFFFF      ","     FF     FF     ","    FF       FF    ","    F         F    ","    F    C    F    ","   DF   CBC   FD   ","    F    C    F    ","    F         F    ","    FF       FF    ","     FF     FF     ","      FFFFFFF      ","         D         ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","         D         ","       FFFFF       ","      FF   FF      ","     FF  C  FF     ","     F  CCC  F     ","    DF CCBCC FD    ","     F  CCC  F     ","     FF  C  FF     ","      FF   FF      ","       FFFFF       ","         D         ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","         D         ","        FFF        ","       FFFFF       ","      FFFFFFF      ","     DFFFBFFFD     ","      FFFFFFF      ","       FFFFF       ","        FFF        ","         D         ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","         D         ","        DDD        ","       DEAED       ","      DDABADD      ","       DEAED       ","        DDD        ","         D         ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","        EAE        ","        ABA        ","        EAE        ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","        EAE        ","        ABA        ","        EAE        ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","        H~H        ","       HEEEH       ","       HEBEH       ","       HEEEH       ","        HHH        ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","        EIE        ","       EEEEE       ","      EEBBBEE      ","      EEBBBEE      ","      EEBBBEE      ","       EEEEE       ","        EEE        ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","        E E        ","       E   E       ","      A     A      ","     E       E     ","    E         E    ","                   ","    E         E    ","     E       E     ","      A     A      ","       E   E       ","        E E        ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","        E E        ","                   ","       E   E       ","      A     A      ","     E       E     ","   E           E   ","                   ","   E           E   ","     E       E     ","      A     A      ","       E   E       ","                   ","        E E        ","                   ","                   ","                   "},
                            {"                   ","                   ","        E E        ","                   ","                   ","       E   E       ","      A     A      ","     E       E     ","  E             E  ","                   ","  E             E  ","     E       E     ","      A     A      ","       E   E       ","                   ","                   ","        E E        ","                   ","                   "},
                            {"         E         ","        E E        ","       E   E       ","       E   E       ","       E   E       ","      E     E      ","     EE     EE     ","  EEE         EEE  "," E               E ","E                 E"," E               E ","  EEE         EEE  ","     EE     EE     ","      E     E      ","       E   E       ","       E   E       ","       E   E       ","        E E        ","         E         "}
                        })))
                .addShape(STRUCTURE_PIECE_TIER2, (transpose(new String[][] {
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         G         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         B         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         B         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         e         ","        eBe        ","         e         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         e         ","        eBe        ","         e         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         e         ","        eBe        ","         e         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         e         ","        ece        ","       ecBce       ","        ece        ","         e         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         e         ","        ece        ","       ecBce       ","        ece        ","         e         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","                   ","         e         ","        ccc        ","       ecBce       ","        ccc        ","         e         ","                   ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","         e         ","         e         ","        c c        ","      ee B ee      ","        c c        ","         e         ","         e         ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","         e         ","        heh        ","       hc ch       ","      ee B ee      ","       hc ch       ","        heh        ","         e         ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","                   ","        heh        ","       h c h       ","      h c c h      ","      ec B ce      ","      h c c h      ","       h c h       ","        heh        ","                   ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","         e         ","        AeA        ","       e c e       ","      A     A      ","     eec B cee     ","      A     A      ","       e c e       ","        AeA        ","         e         ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","                   ","         e         ","        A A        ","       e c e       ","      A     A      ","     e c B c e     ","      A     A      ","       e c e       ","        A A        ","         e         ","                   ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","                   ","         e         ","         e         ","       eA Ae       ","      ee   ee      ","      A     A      ","    ee       ee    ","      A     A      ","      ee   ee      ","       eA Ae       ","         e         ","         e         ","                   ","                   ","                   ","                   "},
                            {"                   ","                   ","                   ","         ~         ","       dd dd       ","      d     d      ","     d  fff  d     ","    d  f   f  d    ","    d f     f d    ","   e  f     f  e   ","    d f     f d    ","    d  f   f  d    ","     d  fff  d     ","      d     d      ","       dd dd       ","         e         ","                   ","                   ","                   "},
                            {"                   ","                   ","         d         ","        e e        ","                   ","                   ","                   ","                   ","   e           e   ","  d             d  ","   e           e   ","                   ","                   ","                   ","                   ","        e e        ","         d         ","                   ","                   "},
                            {"                   ","         d         ","        d d        ","      ggg ggg      ","     gg     gg     ","    gg       gg    ","   gg         gg   ","   g           g   ","  dg           gd  "," d               d ","  dg           gd  ","   g           g   ","   gg         gg   ","    gg       gg    ","     gg     gg     ","      ggg ggg      ","        d d        ","         d         ","                   "},
                            {"         d         ","        j j        ","       d   d       ","                   ","                   ","                   ","                   ","  d             d  "," d               d ","d                 d"," d               d ","  d             d  ","                   ","                   ","                   ","                   ","       d   d       ","        d d        ","         d         "},
                            {"         d         ","        j j        ","       d   d       ","                   ","                   ","                   ","                   ","  d             d  "," d               d ","d                 d"," d               d ","  d             d  ","                   ","                   ","                   ","                   ","       d   d       ","        d d        ","         d         "},
                            {"         d         ","        j j        ","                   ","                   ","                   ","                   ","                   ","                   "," d               d ","d                 d"," d               d ","                   ","                   ","                   ","                   ","                   ","                   ","        d d        ","         d         "},
                            {"         d         ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","d                 d","                   ","                   ","                   ","                   ","                   ","                   ","                   ","                   ","         d         "}
                        })))
                // spotless:on
                .addElement('A', Glasses.chainAllGlasses())
                .addElement('B', ofBlock(GregTechAPI.sBlockCasings1, 15)) // Superconducting Coil
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings5, 5)) // Naquadah Coil
                .addElement('D', ofFrame(Materials.StainlessSteel))
                .addElement('E', ofBlock(ModBlocks.blockSpecialMultiCasings, 6)) // Structural Solar Casings
                .addElement('F', ofBlock(ModBlocks.blockSpecialMultiCasings, 8)) // Thermally Insulated Casing
                .addElement('G', ofBlock(LaserBeacon, 0))
                .addElement(
                    'H',
                    buildHatchAdder(MeteorMiner.class).atLeast(OutputBus, Energy, Maintenance)
                        .casingIndex(TAE.getIndexFromPage(0, 10))
                        .dot(1)
                        .buildAndChain(
                            onElementPass(MeteorMiner::onCasingAdded, ofBlock(ModBlocks.blockSpecialMultiCasings, 6))))
                .addElement(
                    'I',
                    buildHatchAdder(MeteorMiner.class)
                        .atLeast(ImmutableMap.of(InputBus.withAdder(MeteorMiner::addInjector), 1))
                        .casingIndex(TAE.getIndexFromPage(1, 10))
                        .dot(2)
                        .buildAndChain(
                            onElementPass(MeteorMiner::onCasingAdded, ofBlock(ModBlocks.blockSpecialMultiCasings, 6))))
                .addElement('c', ofBlock(GregTechAPI.sBlockCasings4, 7)) // Fusion Coil Block
                .addElement('d', ofBlock(GregTechAPI.sBlockCasings8, 2)) // Mining Neutronium Casing
                .addElement('e', ofBlock(GregTechAPI.sBlockCasings8, 3)) // Mining Black Plutonium Casing
                .addElement('f', ofBlock(GregTechAPI.sBlockCasings9, 11)) // Heat-Resistant Trinium Plated Casing
                .addElement('g', ofFrame(Materials.Neutronium)) // Neutronium Frame
                .addElement('h', ofFrame(Materials.BlackPlutonium)) // Black Plutonium Frame
                .addElement(
                    'j',
                    buildHatchAdder(MeteorMiner.class).atLeast(OutputBus, Energy, Maintenance)
                        .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2))
                        .dot(3)
                        .buildAndChain(
                            onElementPass(MeteorMiner::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 2))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    public MeteorMiner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MeteorMiner(String aName) {
        super(aName);
    }

    public int aCasingAmount;

    @Override
    public void clearHatches() {
        super.clearHatches();

        aCasingAmount = 0;
    }

    @Override
    public void onDisableWorking() {
        if (renderer != null) renderer.setShouldRender(false);
        super.onDisableWorking();
    }

    @Override
    public void onBlockDestroyed() {
        if (renderer != null) renderer.setShouldRender(false);
        super.onBlockDestroyed();
    }

    public void onCasingAdded() {
        aCasingAmount++;
    }

    public boolean addInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof MTEHatchInputBus bus)) return false;
        if (bus.getTierForStructure() > 0) return false;
        bus.updateTexture(aBaseCasingIndex);
        return mInputBusses.add(bus);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (stackSize.stackSize < 2) {
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 9, 13, 7);
        } else buildPiece(STRUCTURE_PIECE_TIER2, stackSize, hintsOnly, 9, 15, 3);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return stackSize.stackSize < 2
            ? survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 9, 13, 7, elementBudget, env, false, true)
            : survivialBuildPiece(STRUCTURE_PIECE_TIER2, stackSize, 9, 15, 3, elementBudget, env, false, true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MeteorMiner(this.mName);
    }

    public static final String TEXTURE_OVERLAY_FRONT_METEOR_MINER_GLOW = RESOURCE_ROOT_ID + ":"
        + "iconsets/OVERLAY_FRONT_METEOR_MINER_GLOW";
    public static final String TEXTURE_OVERLAY_FRONT_METEOR_MINER = RESOURCE_ROOT_ID + ":"
        + "iconsets/OVERLAY_FRONT_METEOR_MINER";
    public static final String TEXTURE_OVERLAY_FRONT_METEOR_MINER_ACTIVE = RESOURCE_ROOT_ID + ":"
        + "iconsets/OVERLAY_FRONT_METEOR_MINER_ACTIVE";
    public static final String TEXTURE_OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW = RESOURCE_ROOT_ID + ":"
        + "iconsets/OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW";

    public static Textures.BlockIcons.CustomIcon OVERLAY_FRONT_METEOR_MINER_GLOW = new Textures.BlockIcons.CustomIcon(
        TEXTURE_OVERLAY_FRONT_METEOR_MINER_GLOW);
    public static Textures.BlockIcons.CustomIcon OVERLAY_FRONT_METEOR_MINER = new Textures.BlockIcons.CustomIcon(
        TEXTURE_OVERLAY_FRONT_METEOR_MINER);
    public static Textures.BlockIcons.CustomIcon OVERLAY_FRONT_METEOR_MINER_ACTIVE = new Textures.BlockIcons.CustomIcon(
        TEXTURE_OVERLAY_FRONT_METEOR_MINER_ACTIVE);
    public static Textures.BlockIcons.CustomIcon OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW = new Textures.BlockIcons.CustomIcon(
        TEXTURE_OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW);

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_METEOR_MINER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(0, 8)) };
        }
        return rTexture;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(TextLocalization.MeteorMinerRecipeType)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_00)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_01)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_02)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_03)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_04)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_05)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_06)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_07)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_08)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_09)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_10)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_11)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_12)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_13)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_14)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_15)
            .addInfo(TextLocalization.Tooltip_MeteorMiner_16)
            .addSeparator()
            .addInfo(TextLocalization.StructureTooComplex)
            .addInfo(TextLocalization.BLUE_PRINT_INFO)
            .addStructureInfo(TextLocalization.Tooltip_MeteorMiner_07)
            .addController(TextLocalization.Tooltip_MeteorMiner_Casing_01_01)
            .addOutputBus(TextLocalization.Tooltip_MeteorMiner_Casing_01_02, 1)
            .addEnergyHatch(TextLocalization.Tooltip_MeteorMiner_Casing_01_02, 1)
            .addMaintenanceHatch(TextLocalization.Tooltip_MeteorMiner_Casing_01_02, 1)
            .addInputBus(TextLocalization.Tooltip_MeteorMiner_Casing_01_03, 2)
            .addStructureInfo(TextLocalization.Tooltip_MeteorMiner_13)
            .addController(TextLocalization.Tooltip_MeteorMiner_Casing_02_01)
            .addOutputBus(TextLocalization.Tooltip_MeteorMiner_Casing_02_02, 3)
            .addEnergyHatch(TextLocalization.Tooltip_MeteorMiner_Casing_02_02, 3)
            .addMaintenanceHatch(TextLocalization.Tooltip_MeteorMiner_Casing_02_02, 3)
            .toolTipFinisher();
        return tt;
    }

    public boolean findLaserRenderer(World w) {
        this.setStartCoords();
        if (w.getTileEntity(
            xStart,
            getBaseMetaTileEntity().getYCoord() + (this.multiTier == 1 ? 10 : 15),
            zStart) instanceof TileEntityLaserBeacon laser) {
            renderer = laser;
            renderer.setRotationFields(getDirection(), getRotation(), getFlip());
            return true;
        }
        return false;
    }

    public boolean stopAllRendering = false;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        stopAllRendering = !stopAllRendering;
        if (stopAllRendering) {
            PlayerUtils.messagePlayer(aPlayer, "Rendering off");
            if (renderer != null) renderer.setShouldRender(false);
        } else PlayerUtils.messagePlayer(aPlayer, "Rendering on");
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        aCasingAmount = 0;
        this.multiTier = 0;
        if (aStack != null) {
            if (checkPiece(STRUCTURE_PIECE_MAIN, 9, 13, 7)) this.multiTier = getMultiTier(aStack);
            if (checkPiece(STRUCTURE_PIECE_TIER2, 9, 15, 3)) this.multiTier = getMultiTier(aStack);
        }
        if (mEnergyHatches.isEmpty() || (mInputBusses.isEmpty() && this.multiTier == 1)
            || mMaintenanceHatches.size() != 1
            || !findLaserRenderer(getBaseMetaTileEntity().getWorld())) return false;
        return this.multiTier > 0;
    }

    public int getMultiTier(ItemStack inventory) {
        if (inventory == null) return 0;
        return inventory.isItemEqual(GTNLItemList.MeteorMinerSchematic2.get(1)) ? 2
            : inventory.isItemEqual(GTNLItemList.MeteorMinerSchematic1.get(1)) ? 1 : 0;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public int getLaserToEndHeight() {
        return (this.multiTier == 1 ? 3 : 0);
    }

    private void setFortuneTier() {
        this.fortuneTier = 0;

        if (this.multiTier == 2) {
            this.fortuneTier = 3;
            return;
        }

        if (!mInputBusses.isEmpty()) {
            Optional<ItemStack> input = Optional.ofNullable(
                mInputBusses.get(0)
                    .getInventoryHandler()
                    .getStackInSlot(0));
            if (input.isPresent()) {
                ItemStack stack = input.get();
                this.fortuneTier = getFortuneTierForItem(stack);
            }
        }
    }

    private int getFortuneTierForItem(ItemStack stack) {
        if (isSpecificItem(stack, Botania.ID, "terraPick")) {
            return 3;
        } else if (isSpecificItem(stack, BloodMagic.ID, "boundPickaxe")) {
            return 2;
        } else if (isSpecificItem(stack, Thaumcraft.ID, "ItemPickaxeElemental")) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean isSpecificItem(ItemStack stack, String modId, String itemName) {
        ItemStack specificItem = getModItem(modId, itemName, 1, 0);
        return stack.getItem() == specificItem.getItem() && stack.getItemDamage() == specificItem.getItemDamage();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("currentRadius", currentRadius);
        aNBT.setInteger("xDrill", xDrill);
        aNBT.setInteger("yDrill", yDrill);
        aNBT.setInteger("zDrill", zDrill);
        aNBT.setInteger("xStart", xStart);
        aNBT.setInteger("yStart", yStart);
        aNBT.setInteger("zStart", zStart);
        aNBT.setBoolean("isStartInitialized", isStartInitialized);
        aNBT.setBoolean("hasFinished", hasFinished);
        aNBT.setBoolean("isWaiting", isWaiting);
        aNBT.setBoolean("stopAllRendering", stopAllRendering);
        aNBT.setInteger("multiTier", multiTier);
        aNBT.setInteger("fortuneTier", fortuneTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        currentRadius = aNBT.getInteger("currentRadius");
        xDrill = aNBT.getInteger("xDrill");
        yDrill = aNBT.getInteger("yDrill");
        zDrill = aNBT.getInteger("zDrill");
        xStart = aNBT.getInteger("xStart");
        yStart = aNBT.getInteger("yStart");
        zStart = aNBT.getInteger("zStart");
        isStartInitialized = aNBT.getBoolean("isStartInitialized");
        hasFinished = aNBT.getBoolean("hasFinished");
        isWaiting = aNBT.getBoolean("isWaiting");
        stopAllRendering = aNBT.getBoolean("stopAllRendering");
        multiTier = aNBT.getInteger("multiTier");
        fortuneTier = aNBT.getInteger("fortuneTier");
    }

    public void reset() {
        this.isResetting = false;
        this.hasFinished = true;
        this.isWaiting = false;
        currentRadius = MAX_RADIUS;
        this.initializeDrillPos();
    }

    public void startReset() {
        this.isResetting = true;
        stopMachine(ShutDownReasonRegistry.NONE);
        enableWorking();
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (this.multiTier != this.getMultiTier(mInventory[1])) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return SimpleCheckRecipeResult.ofFailure("missing_schematic");
        }
        if (renderer != null) {
            renderer.setColors(1, 0, 0);
        }
        if (isResetting) {
            this.reset();
            return SimpleCheckRecipeResult.ofSuccess("meteor_reset");
        }

        setElectricityStats();
        if (!isEnergyEnough()) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return SimpleCheckRecipeResult.ofFailure("not_enough_energy");
        }

        if (!isStartInitialized) {
            this.setStartCoords();
            this.findBestRadius();
            this.initializeDrillPos();
        }

        if (!hasFinished) {
            renderer.setShouldRender(true);
            renderer.setRange(this.currentRadius + 32.5 + this.getLaserToEndHeight());
            this.setFortuneTier();
            this.startMining(this.multiTier);
            mOutputItems = res.toArray(new ItemStack[0]);
            res.clear();
        } else {
            renderer.setShouldRender(false);
            this.isWaiting = true;
            this.setElectricityStats();
            boolean isReady = checkCenter();
            if (isReady) {
                this.isWaiting = false;
                this.setElectricityStats();
                this.setReady();
                this.hasFinished = false;
            } else return SimpleCheckRecipeResult.ofSuccess("meteor_waiting");
        }

        return SimpleCheckRecipeResult.ofSuccess("meteor_mining");
    }

    public void startMining(int tier) {
        switch (tier) {
            case 1 -> this.mineSingleBlock();
            case 2 -> this.mineRow();
            default -> throw new IllegalArgumentException("Invalid Multiblock Tier");
        }
    }

    private static final Set<ItemStack> blacklist = new HashSet<>();

    public static void addToBlacklist(ItemStack item) {
        blacklist.add(item);
    }

    public static void initializeBlacklist() {
        ItemStack CasingCoilNaquadah = ItemList.Casing_Coil_Naquadah.get(1);
        addToBlacklist(CasingCoilNaquadah);
        ItemStack CasingSolarTower = GregtechItemList.Casing_SolarTower_Structural.get(1);
        addToBlacklist(CasingSolarTower);
        ItemStack CasingCoilSuperconductor = ItemList.Casing_Coil_Superconductor.get(1);
        addToBlacklist(CasingCoilSuperconductor);
        ItemStack CasingSolarTowerHeatContainment = GregtechItemList.Casing_SolarTower_HeatContainment.get(1);
        addToBlacklist(CasingSolarTowerHeatContainment);
        ItemStack HatchInputBusULV = ItemList.Hatch_Input_Bus_ULV.get(1);
        addToBlacklist(HatchInputBusULV);
        ItemStack LaserBeacon = GTNLItemList.LaserBeacon.get(1);
        addToBlacklist(LaserBeacon);
        ItemStack MeteorMiner = GTNLItemList.MeteorMiner.get(1);
        addToBlacklist(MeteorMiner);
        ItemStack CasingMiningNeutronium = ItemList.Casing_MiningNeutronium.get(1);
        addToBlacklist(CasingMiningNeutronium);
        ItemStack CasingFusionCoil = ItemList.Casing_Fusion_Coil.get(1);
        addToBlacklist(CasingFusionCoil);
        ItemStack CasingMiningBlackPlutonium = ItemList.Casing_MiningBlackPlutonium.get(1);
        addToBlacklist(CasingMiningBlackPlutonium);
        ItemStack BlockPlasmaHeatingCasing = ItemList.BlockPlasmaHeatingCasing.get(1);
        addToBlacklist(BlockPlasmaHeatingCasing);
        ItemStack BlackPlutoniumFrame = GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 1);
        addToBlacklist(BlackPlutoniumFrame);
        ItemStack StainlessSteelFrame = GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1);
        addToBlacklist(StainlessSteelFrame);
        ItemStack NeutroniumFrame = GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1);
        addToBlacklist(NeutroniumFrame);
        Block AlloyGlass = GameRegistry.findBlock(IndustrialCraft2.ID, "blockAlloyGlass");
        ItemStack AlloyGlassBlock = new ItemStack(AlloyGlass, 1, 0);
        addToBlacklist(AlloyGlassBlock);
    }

    private boolean isInBlacklist(int x, int y, int z) {
        Block target = getBaseMetaTileEntity().getBlock(x, y, z);
        int meta = getBaseMetaTileEntity().getMetaID(x, y, z);
        ItemStack itemStack = new ItemStack(target, 1, meta);
        return blacklist.contains(itemStack);
    }

    public void mineSingleBlock() {
        while (getBaseMetaTileEntity().getWorld()
            .isAirBlock(this.xDrill, this.yDrill, this.zDrill)) {
            this.moveToNextBlock();
            if (this.hasFinished) return;
        }
        if (!isInBlacklist(this.xDrill, this.yDrill, this.zDrill)) {
            this.mineBlock(this.xDrill, this.yDrill, this.zDrill);
        }
        this.moveToNextBlock();
    }

    public void mineRow() {
        int currentX = this.xDrill;
        int currentY = this.yDrill;
        while (getBaseMetaTileEntity().getWorld() // Skips empty rows
            .isAirBlock(currentX, currentY, this.zStart)) {
            this.moveToNextColumn();
            if (this.hasFinished) return;
            currentX = this.xDrill;
            currentY = this.yDrill;
        }

        int opposite = 0;
        for (int z = -currentRadius; z <= (currentRadius - opposite); z++) {
            int currentZ = this.zStart + z;
            if (!getBaseMetaTileEntity().getWorld()
                .isAirBlock(this.xDrill, this.yDrill, currentZ) && !isInBlacklist(this.xDrill, this.yDrill, currentZ)) {
                this.mineBlock(this.xDrill, this.yDrill, currentZ);
            } else opposite++;
        }
        this.moveToNextColumn();
    }

    public void mineBlock(int currentX, int currentY, int currentZ) {
        Block target = getBaseMetaTileEntity().getBlock(currentX, currentY, currentZ);
        if (target.getBlockHardness(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ) > 0) {
            final int targetMeta = getBaseMetaTileEntity().getMetaID(currentX, currentY, currentZ);
            Collection<ItemStack> drops = target
                .getDrops(getBaseMetaTileEntity().getWorld(), currentX, currentY, currentZ, targetMeta, 0);
            if (GTUtility.isOre(target, targetMeta)) {
                res.addAll(getOutputByDrops(drops));
            } else res.addAll(drops);
            getBaseMetaTileEntity().getWorld()
                .setBlockToAir(currentX, currentY, currentZ);
        }
    }

    public Collection<ItemStack> getOutputByDrops(Collection<ItemStack> oreBlockDrops) {
        long voltage = getMaxInputVoltage();
        Collection<ItemStack> outputItems = new HashSet<>();
        oreBlockDrops.forEach(currentItem -> {
            if (!doUseMaceratorRecipe(currentItem)) {
                outputItems.add(multiplyStackSize(currentItem));
                return;
            }
            GTRecipe tRecipe = RecipeMaps.maceratorRecipes.findRecipeQuery()
                .items(currentItem)
                .voltage(voltage)
                .find();
            if (tRecipe == null) {
                outputItems.add(currentItem);
                return;
            }
            for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                ItemStack recipeOutput = tRecipe.mOutputs[i].copy();
                if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(i))
                    multiplyStackSize(recipeOutput);
                outputItems.add(recipeOutput);
            }
        });
        return outputItems;
    }

    public ItemStack multiplyStackSize(ItemStack itemStack) {
        itemStack.stackSize *= getBaseMetaTileEntity().getRandomNumber(this.fortuneTier + 1) + 1;
        return itemStack;
    }

    public boolean doUseMaceratorRecipe(ItemStack currentItem) {
        ItemData itemData = GTOreDictUnificator.getItemData(currentItem);
        return itemData == null || itemData.mPrefix != OrePrefixes.crushed && itemData.mPrefix != OrePrefixes.dustImpure
            && itemData.mPrefix != OrePrefixes.dust
            && itemData.mPrefix != OrePrefixes.gem
            && itemData.mPrefix != OrePrefixes.gemChipped
            && itemData.mPrefix != OrePrefixes.gemExquisite
            && itemData.mPrefix != OrePrefixes.gemFlawed
            && itemData.mPrefix != OrePrefixes.gemFlawless
            && itemData.mMaterial.mMaterial != Materials.Oilsands;
    }

    public void moveToNextBlock() {
        if (this.zDrill <= this.zStart + currentRadius) {
            this.zDrill++;
        } else {
            this.zDrill = this.zStart - currentRadius;
            this.moveToNextColumn();
        }
    }

    public void moveToNextColumn() {
        if (this.xDrill <= this.xStart + currentRadius) {
            this.xDrill++;
        } else if (this.yDrill <= this.yStart + currentRadius) {
            this.xDrill = this.xStart - currentRadius;
            this.yDrill++;
        } else {
            this.hasFinished = true;
        }
    }

    /**
     * Sets the coordinates of the center to the max range meteor center
     *
     */
    public void setStartCoords() {
        ForgeDirection facing = getBaseMetaTileEntity().getBackFacing();
        if (facing == ForgeDirection.NORTH || facing == ForgeDirection.SOUTH) {
            xStart = getBaseMetaTileEntity().getXCoord();
            zStart = (this.multiTier == 1 ? 2 : 6) * getExtendedFacing().getRelativeBackInWorld().offsetZ
                + getBaseMetaTileEntity().getZCoord();
        } else {
            xStart = (this.multiTier == 1 ? 2 : 6) * getExtendedFacing().getRelativeBackInWorld().offsetX
                + getBaseMetaTileEntity().getXCoord();
            zStart = getBaseMetaTileEntity().getZCoord();
        }
        yStart = (this.multiTier == 1 ? 45 : 47) + getBaseMetaTileEntity().getYCoord();
    }

    public void setReady() {
        this.findBestRadius();
        this.initializeDrillPos();
    }

    public void initializeDrillPos() {
        this.xDrill = this.xStart - currentRadius;
        this.yDrill = this.yStart - currentRadius;
        this.zDrill = this.zStart - currentRadius;

        this.isStartInitialized = true;
        this.hasFinished = false;
    }

    public boolean checkCenter() {
        return !getBaseMetaTileEntity().getWorld()
            .isAirBlock(xStart, yStart + 1, zStart);
    }

    public void findBestRadius() {
        currentRadius = MAX_RADIUS;
        int delta = 0;
        for (int zCoord = zStart - currentRadius; delta < MAX_RADIUS - 1; zCoord++) {
            if (!getBaseMetaTileEntity().getWorld()
                .isAirBlock(xStart, yStart, zCoord)) {
                break;
            }
            delta++;
        }
        currentRadius -= delta;
    }

    public void setElectricityStats() {
        this.mOutputItems = new ItemStack[0];

        this.mEfficiency = 10000;
        this.mEfficiencyIncrease = 10000;

        OverclockCalculator calculator = new OverclockCalculator().setEUt(getAverageInputVoltage())
            .setAmperage(getMaxInputAmps())
            .setRecipeEUt(128)
            .setDuration(12 * 20)
            .setAmperageOC(mEnergyHatches.size() != 1);
        calculator.calculate();
        this.mMaxProgresstime = (isWaiting) ? 200 : calculator.getDuration();
        this.mEUt = (int) (calculator.getConsumption() / ((isWaiting) ? 8 : 1));
    }

    public boolean isEnergyEnough() {
        long requiredEnergy = 512 + getMaxInputVoltage() * 4;
        for (MTEHatchEnergy energyHatch : mEnergyHatches) {
            requiredEnergy -= energyHatch.getEUVar();
            if (requiredEnergy <= 0) return true;
        }
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);

        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> this.startReset())
                .setPlayClickSound(true)
                .setBackground(
                    () -> {
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_CYCLIC };
                    })
                .setPos(new Pos2d(174, 112))
                .setSize(16, 16));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("fortune", this.fortuneTier);
        tag.setInteger("tier", this.multiTier);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            (this.multiTier > 0 ? "Current Tier: " : "") + EnumChatFormatting.WHITE
                + StatCollector.translateToLocal("GT5U.METEOR_MINER_CONTROLLER.tier." + tag.getInteger("tier"))
                + EnumChatFormatting.RESET);
        currentTip.add(
            "Augment: " + EnumChatFormatting.WHITE
                + StatCollector.translateToLocal("GT5U.METEOR_MINER_CONTROLLER.fortune." + tag.getInteger("fortune"))
                + EnumChatFormatting.RESET);
    }
}
