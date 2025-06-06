package com.science.gtnl.loader;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_VALVE;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.google.common.collect.ImmutableSet;
import com.science.gtnl.Mods;
import com.science.gtnl.Utils.AnimatedText;
import com.science.gtnl.Utils.MoreMaterialToolUtil;
import com.science.gtnl.Utils.item.TextLocalization;
import com.science.gtnl.common.GTNLItemList;
import com.science.gtnl.common.block.Casings.BasicBlocks;
import com.science.gtnl.common.machine.basicMachine.ManaTank;
import com.science.gtnl.common.machine.basicMachine.SteamAssemblerBronze;
import com.science.gtnl.common.machine.basicMachine.SteamAssemblerSteel;
import com.science.gtnl.common.machine.basicMachine.SteamTurbine;
import com.science.gtnl.common.machine.hatch.CustomFluidHatch;
import com.science.gtnl.common.machine.hatch.DebugEnergyHatch;
import com.science.gtnl.common.machine.hatch.DualInputHatch;
import com.science.gtnl.common.machine.hatch.DualOutputHatch;
import com.science.gtnl.common.machine.hatch.HumongousInputBus;
import com.science.gtnl.common.machine.hatch.HumongousNinefoldInputHatch;
import com.science.gtnl.common.machine.hatch.HumongousSolidifierHatch;
import com.science.gtnl.common.machine.hatch.ManaDynamoHatch;
import com.science.gtnl.common.machine.hatch.ManaEnergyHatch;
import com.science.gtnl.common.machine.hatch.NinefoldInputHatch;
import com.science.gtnl.common.machine.hatch.ParallelControllerHatch;
import com.science.gtnl.common.machine.hatch.SuperCraftingInputHatchME;
import com.science.gtnl.common.machine.hatch.SuperCraftingInputProxy;
import com.science.gtnl.common.machine.hatch.SuperDataAccessHatch;
import com.science.gtnl.common.machine.multiblock.AdvancedCircuitAssemblyLine;
import com.science.gtnl.common.machine.multiblock.AdvancedInfiniteDriller;
import com.science.gtnl.common.machine.multiblock.AdvancedPhotovoltaicPowerStation;
import com.science.gtnl.common.machine.multiblock.AprilFool.HighPressureSteamFusionReactor;
import com.science.gtnl.common.machine.multiblock.AprilFool.MegaSolarBoiler;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamCactusWonder;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamCarpenter;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamExtractinator;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamFusionReactor;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamGateAssembler;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamInfernalCokeOven;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamLavaMaker;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamManufacturer;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamMegaCompressor;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamRockBreaker;
import com.science.gtnl.common.machine.multiblock.AprilFool.SteamWoodcutter;
import com.science.gtnl.common.machine.multiblock.AprilFool.Steamgate;
import com.science.gtnl.common.machine.multiblock.BioengineeringModule;
import com.science.gtnl.common.machine.multiblock.BloodSoulSacrificialArray;
import com.science.gtnl.common.machine.multiblock.BrickedBlastFurnace;
import com.science.gtnl.common.machine.multiblock.CheatOreProcessingFactory;
import com.science.gtnl.common.machine.multiblock.ComponentAssembler;
import com.science.gtnl.common.machine.multiblock.CrackerHub;
import com.science.gtnl.common.machine.multiblock.DecayHastener;
import com.science.gtnl.common.machine.multiblock.Desulfurizer;
import com.science.gtnl.common.machine.multiblock.DraconicFusionCrafting;
import com.science.gtnl.common.machine.multiblock.EdenGarden;
import com.science.gtnl.common.machine.multiblock.ElementCopying;
import com.science.gtnl.common.machine.multiblock.EnergeticPhotovoltaicPowerStation;
import com.science.gtnl.common.machine.multiblock.FuelRefiningComplex;
import com.science.gtnl.common.machine.multiblock.GenerationEarthEngine;
import com.science.gtnl.common.machine.multiblock.GrandAssemblyLine;
import com.science.gtnl.common.machine.multiblock.HandOfJohnDavisonRockefeller;
import com.science.gtnl.common.machine.multiblock.IndustrialArcaneAssembler;
import com.science.gtnl.common.machine.multiblock.IntegratedAssemblyFacility;
import com.science.gtnl.common.machine.multiblock.LapotronChip;
import com.science.gtnl.common.machine.multiblock.LargeBioLab;
import com.science.gtnl.common.machine.multiblock.LargeBrewer;
import com.science.gtnl.common.machine.multiblock.LargeCircuitAssembler;
import com.science.gtnl.common.machine.multiblock.LargeGasCollector;
import com.science.gtnl.common.machine.multiblock.LargeIncubator;
import com.science.gtnl.common.machine.multiblock.LargeNaquadahReactor;
import com.science.gtnl.common.machine.multiblock.LargeSteamAlloySmelter;
import com.science.gtnl.common.machine.multiblock.LargeSteamChemicalBath;
import com.science.gtnl.common.machine.multiblock.LargeSteamCircuitAssembler;
import com.science.gtnl.common.machine.multiblock.LargeSteamCrusher;
import com.science.gtnl.common.machine.multiblock.LargeSteamExtractor;
import com.science.gtnl.common.machine.multiblock.LargeSteamExtruder;
import com.science.gtnl.common.machine.multiblock.LargeSteamFormingPress;
import com.science.gtnl.common.machine.multiblock.LargeSteamFurnace;
import com.science.gtnl.common.machine.multiblock.LargeSteamSifter;
import com.science.gtnl.common.machine.multiblock.LargeSteamThermalCentrifuge;
import com.science.gtnl.common.machine.multiblock.LibraryOfRuina;
import com.science.gtnl.common.machine.multiblock.MagneticEnergyReactionFurnace;
import com.science.gtnl.common.machine.multiblock.MatterFabricator;
import com.science.gtnl.common.machine.multiblock.MeteorMiner;
import com.science.gtnl.common.machine.multiblock.NanitesIntegratedProcessingCenter;
import com.science.gtnl.common.machine.multiblock.NanoPhagocytosisPlant;
import com.science.gtnl.common.machine.multiblock.NeutroniumWireCutting;
import com.science.gtnl.common.machine.multiblock.NineIndustrialMultiMachine;
import com.science.gtnl.common.machine.multiblock.OreExtractionModule;
import com.science.gtnl.common.machine.multiblock.PetrochemicalPlant;
import com.science.gtnl.common.machine.multiblock.PlatinumBasedTreatment;
import com.science.gtnl.common.machine.multiblock.PolymerTwistingModule;
import com.science.gtnl.common.machine.multiblock.PrimitiveDistillationTower;
import com.science.gtnl.common.machine.multiblock.ProcessingArray;
import com.science.gtnl.common.machine.multiblock.RareEarthCentrifugal;
import com.science.gtnl.common.machine.multiblock.ReactionFurnace;
import com.science.gtnl.common.machine.multiblock.RealArtificialStar;
import com.science.gtnl.common.machine.multiblock.ResourceCollectionModule;
import com.science.gtnl.common.machine.multiblock.ShallowChemicalCoupling;
import com.science.gtnl.common.machine.multiblock.SmeltingMixingFurnace;
import com.science.gtnl.common.machine.multiblock.SteamCracking;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.AlloyBlastSmelter;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.BlazeBlastFurnace;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.ChemicalPlant;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.ColdIceFreezer;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.Digester;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.ElectricBlastFurnace;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.ElectricImplosionCompressor;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.EnergyInfuser;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.FishingGround;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.FlotationCellRegulator;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.Incubator;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.IsaMill;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeAlloySmelter;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeArcSmelter;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeAssembler;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeAutoclave;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeBender;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeBoiler;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeCanning;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeCentrifuge;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeChemicalBath;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeCutter;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeDistillery;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeElectrolyzer;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeElectromagnet;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeEngravingLaser;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeExtractor;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeExtruder;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeForming;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeHammer;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeIndustrialLathe;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeMacerationTower;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeMaterialPress;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeMixer;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargePacker;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargePyrolyseOven;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeSiftingFunnel;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeSolidifier;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeSteamCentrifuge;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeSteamCompressor;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeSteamHammer;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeSteamMixer;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeSteamOreWasher;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LargeWiremill;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.LuvKuangBiaoOneGiantNuclearFusionReactor;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.MegaAlloyBlastSmelter;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.MegaBlastFurnace;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.MolecularTransformer;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.PrecisionAssembler;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.UevKuangBiaoFiveGiantNuclearFusionReactor;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.UhvKuangBiaoFourGiantNuclearFusionReactor;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.UvKuangBiaoThreeGiantNuclearFusionReactor;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.VacuumDryingFurnace;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.VacuumFreezer;
import com.science.gtnl.common.machine.multiblock.StructuralReconstructionPlan.ZpmKuangBiaoTwoGiantNuclearFusionReactor;
import com.science.gtnl.common.machine.multiblock.SuperSpaceElevator;
import com.science.gtnl.common.machine.multiblock.TeleportationArrayToAlfheim;
import com.science.gtnl.common.machine.multiblock.VibrantPhotovoltaicPowerStation;
import com.science.gtnl.common.machine.multiblock.WhiteNightGenerator;
import com.science.gtnl.common.machine.multiblock.WoodDistillation;
import com.science.gtnl.common.materials.MaterialPool;
import com.science.gtnl.common.recipe.RecipeRegister;

import bartworks.API.BorosilicateGlass;
import goodgenerator.util.CrackRecipeAdder;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.render.TextureFactory;
import gregtech.common.covers.CoverConveyor;
import gregtech.common.covers.CoverFluidRegulator;
import gregtech.common.covers.CoverPump;
import gregtech.common.covers.CoverSteamRegulator;
import gregtech.common.covers.CoverSteamValve;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class MachineLoader {

    public static ItemStack ResourceCollectionModule;
    public static ItemStack SuperSpaceElevator;

    public static void loadMachines() {

        GTNLItemList.EdenGarden.set(new EdenGarden(21004, "EdenGarden", TextLocalization.NameEdenGarden));
        addItemTooltip(GTNLItemList.EdenGarden.get(1), AnimatedText.SNL_EDEN_GARDEN);

        GTNLItemList.LargeSteamCircuitAssembler.set(
            new LargeSteamCircuitAssembler(
                21005,
                "LargeSteamCircuitAssembler",
                TextLocalization.NameLargeSteamCircuitAssembler));
        addItemTooltip(GTNLItemList.LargeSteamCircuitAssembler.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.GenerationEarthEngine
            .set(new GenerationEarthEngine(21006, "GenerationEarthEngine", TextLocalization.NameGenerationEarthEngine));
        addItemTooltip(GTNLItemList.GenerationEarthEngine.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.BloodSoulSacrificialArray.set(
            new BloodSoulSacrificialArray(
                21007,
                "BloodSoulSacrificialArray",
                TextLocalization.NameBloodSoulSacrificialArray));
        addItemTooltip(GTNLItemList.BloodSoulSacrificialArray.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.RealArtificialStar
            .set(new RealArtificialStar(21008, "RealArtificialStar", TextLocalization.NameRealArtificialStar));
        addItemTooltip(GTNLItemList.RealArtificialStar.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.TeleportationArrayToAlfheim.set(
            new TeleportationArrayToAlfheim(
                21009,
                "TeleportationArrayToAlfheim",
                TextLocalization.NameTeleportationArrayToAlfheim));
        addItemTooltip(GTNLItemList.TeleportationArrayToAlfheim.get(1), AnimatedText.SNL_NLXCJH);

        GTNLItemList.LapotronChip.set(new LapotronChip(21010, "LapotronChip", TextLocalization.NameLapotronChip));
        addItemTooltip(GTNLItemList.LapotronChip.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NeutroniumWireCutting
            .set(new NeutroniumWireCutting(21011, "NeutroniumWireCutting", TextLocalization.NameNeutroniumWireCutting));
        addItemTooltip(GTNLItemList.NeutroniumWireCutting.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeSteamCrusher
            .set(new LargeSteamCrusher(21012, "LargeSteamCrusher", TextLocalization.NameLargeSteamCrusher));
        addItemTooltip(GTNLItemList.LargeSteamCrusher.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.ComponentAssembler
            .set(new ComponentAssembler(21013, "ComponentAssembler", TextLocalization.NameComponentAssembler));
        addItemTooltip(GTNLItemList.ComponentAssembler.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeSteamFurnace
            .set(new LargeSteamFurnace(21014, "LargeSteamFurnace", TextLocalization.NameLargeSteamFurnace));
        addItemTooltip(GTNLItemList.LargeSteamFurnace.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeSteamAlloySmelter.set(
            new LargeSteamAlloySmelter(21015, "LargeSteamAlloySmelter", TextLocalization.NameLargeSteamAlloySmelter));
        addItemTooltip(GTNLItemList.LargeSteamAlloySmelter.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeSteamThermalCentrifuge.set(
            new LargeSteamThermalCentrifuge(
                21016,
                "LargeSteamThermalCentrifuge",
                TextLocalization.NameLargeSteamThermalCentrifuge));
        addItemTooltip(GTNLItemList.LargeSteamThermalCentrifuge.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.SteamCracking.set(new SteamCracking(21017, "SteamCracking", TextLocalization.NameSteamCracking));
        addItemTooltip(GTNLItemList.SteamCracking.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeSteamChemicalBath.set(
            new LargeSteamChemicalBath(21018, "LargeSteamChemicalBath", TextLocalization.NameLargeSteamChemicalBath));
        addItemTooltip(GTNLItemList.LargeSteamChemicalBath.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.PrimitiveDistillationTower.set(
            new PrimitiveDistillationTower(
                21019,
                "PrimitiveDistillationTower",
                TextLocalization.NamePrimitiveDistillationTower));
        addItemTooltip(GTNLItemList.PrimitiveDistillationTower.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.MeteorMiner.set(new MeteorMiner(21020, "MeteorMiner", TextLocalization.NameMeteorMiner));
        addItemTooltip(GTNLItemList.MeteorMiner.get(1), AnimatedText.SNL_TOTTO);

        GTNLItemList.Desulfurizer.set(new Desulfurizer(21021, "Desulfurizer", TextLocalization.NameDesulfurizer));
        addItemTooltip(GTNLItemList.Desulfurizer.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeCircuitAssembler
            .set(new LargeCircuitAssembler(21022, "LargeCircuitAssembler", TextLocalization.NameLargeCircuitAssembler));
        addItemTooltip(GTNLItemList.LargeCircuitAssembler.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.PetrochemicalPlant
            .set(new PetrochemicalPlant(21023, "PetrochemicalPlant", TextLocalization.NamePetrochemicalPlant));
        addItemTooltip(GTNLItemList.PetrochemicalPlant.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.SmeltingMixingFurnace
            .set(new SmeltingMixingFurnace(21024, "SmeltingMixingFurnace", TextLocalization.NameSmeltingMixingFurnace));
        addItemTooltip(GTNLItemList.SmeltingMixingFurnace.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.WhiteNightGenerator
            .set(new WhiteNightGenerator(21025, "WhiteNightGenerator", TextLocalization.NameWhiteNightGenerator));
        addItemTooltip(GTNLItemList.WhiteNightGenerator.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ProcessingArray
            .set(new ProcessingArray(21026, "ProcessingArray", TextLocalization.NameProcessingArrayGTNL));
        addItemTooltip(GTNLItemList.ProcessingArray.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.MegaBlastFurnace
            .set(new MegaBlastFurnace(21027, "MegaBlastFurnace", TextLocalization.NameMegaBlastFurnace));
        addItemTooltip(GTNLItemList.MegaBlastFurnace.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.BrickedBlastFurnace
            .set(new BrickedBlastFurnace(21028, "BrickedBlastFurnace", TextLocalization.NameBrickedBlastFurnace));
        addItemTooltip(GTNLItemList.BrickedBlastFurnace.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.RareEarthCentrifugal
            .set(new RareEarthCentrifugal(21029, "RareEarthCentrifugal", TextLocalization.NameRareEarthCentrifugal));
        addItemTooltip(GTNLItemList.RareEarthCentrifugal.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.ColdIceFreezer
            .set(new ColdIceFreezer(21030, "ColdIceFreezer", TextLocalization.NameColdIceFreezer));
        addItemTooltip(GTNLItemList.ColdIceFreezer.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.BlazeBlastFurnace
            .set(new BlazeBlastFurnace(21031, "BlazeBlastFurnace", TextLocalization.NameBlazeBlastFurnace));
        addItemTooltip(GTNLItemList.BlazeBlastFurnace.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.ChemicalPlant.set(new ChemicalPlant(21032, "ChemicalPlant", TextLocalization.NameChemicalPlant));
        addItemTooltip(GTNLItemList.ChemicalPlant.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.VacuumFreezer.set(new VacuumFreezer(21033, "VacuumFreezer", TextLocalization.NameVacuumFreezer));
        addItemTooltip(GTNLItemList.VacuumFreezer.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.IndustrialArcaneAssembler.set(
            new IndustrialArcaneAssembler(
                21034,
                "IndustrialArcaneAssembler",
                TextLocalization.NameIndustrialArcaneAssembler));
        addItemTooltip(GTNLItemList.IndustrialArcaneAssembler.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.EnergeticPhotovoltaicPowerStation.set(
            new EnergeticPhotovoltaicPowerStation(
                21035,
                "EnergeticPhotovoltaicPowerStation",
                TextLocalization.NameEnergeticPhotovoltaicPowerStation));
        addItemTooltip(GTNLItemList.EnergeticPhotovoltaicPowerStation.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.AdvancedPhotovoltaicPowerStation.set(
            new AdvancedPhotovoltaicPowerStation(
                21036,
                "AdvancedPhotovoltaicPowerStation",
                TextLocalization.NameAdvancedPhotovoltaicPowerStation));
        addItemTooltip(GTNLItemList.AdvancedPhotovoltaicPowerStation.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.VibrantPhotovoltaicPowerStation.set(
            new VibrantPhotovoltaicPowerStation(
                21037,
                "VibrantPhotovoltaicPowerStation",
                TextLocalization.NameVibrantPhotovoltaicPowerStation));
        addItemTooltip(GTNLItemList.VibrantPhotovoltaicPowerStation.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeMacerationTower
            .set(new LargeMacerationTower(21038, "LargeMacerationTower", TextLocalization.NameLargeMacerationTower));
        addItemTooltip(GTNLItemList.LargeMacerationTower.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.HandOfJohnDavisonRockefeller.set(
            new HandOfJohnDavisonRockefeller(
                21039,
                "HandOfJohnDavisonRockefeller",
                TextLocalization.NameHandOfJohnDavisonRockefeller));
        addItemTooltip(GTNLItemList.HandOfJohnDavisonRockefeller.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeSiftingFunnel
            .set(new LargeSiftingFunnel(21040, "LargeSiftingFunnel", TextLocalization.NameLargeSiftingFunnel));
        addItemTooltip(GTNLItemList.LargeSiftingFunnel.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeCutter.set(new LargeCutter(21041, "LargeCutter", TextLocalization.NameLargeCutter));
        addItemTooltip(GTNLItemList.LargeCutter.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeBrewer.set(new LargeBrewer(21042, "LargeBrewer", TextLocalization.NameLargeBrewer));
        addItemTooltip(GTNLItemList.LargeBrewer.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeIndustrialLathe
            .set(new LargeIndustrialLathe(21043, "LargeIndustrialLathe", TextLocalization.NameLargeIndustrialLathe));
        addItemTooltip(GTNLItemList.LargeIndustrialLathe.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeMaterialPress
            .set(new LargeMaterialPress(21044, "LargeMaterialPress", TextLocalization.NameLargeMaterialPress));
        addItemTooltip(GTNLItemList.LargeMaterialPress.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeWiremill.set(new LargeWiremill(21045, "LargeWiremill", TextLocalization.NameLargeWiremill));
        addItemTooltip(GTNLItemList.LargeWiremill.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeBender.set(new LargeBender(21046, "LargeBender", TextLocalization.NameLargeBender));
        addItemTooltip(GTNLItemList.LargeBender.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.ElectricImplosionCompressor.set(
            new ElectricImplosionCompressor(
                21047,
                "ElectricImplosionCompressor",
                TextLocalization.NameElectricImplosionCompressor));
        addItemTooltip(GTNLItemList.ElectricImplosionCompressor.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.LargeExtruder.set(new LargeExtruder(21048, "LargeExtruder", TextLocalization.NameLargeExtruder));
        addItemTooltip(GTNLItemList.LargeExtruder.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeArcSmelter
            .set(new LargeArcSmelter(21049, "LargeArcSmelter", TextLocalization.NameLargeArcSmelter));
        addItemTooltip(GTNLItemList.LargeArcSmelter.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeForming.set(new LargeForming(21050, "LargeForming", TextLocalization.NameLargeForming));
        addItemTooltip(GTNLItemList.LargeForming.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.MatterFabricator
            .set(new MatterFabricator(21051, "MatterFabricator", TextLocalization.NameMatterFabricator));
        addItemTooltip(GTNLItemList.MatterFabricator.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeElectrolyzer
            .set(new LargeElectrolyzer(21052, "LargeElectrolyzer", TextLocalization.NameLargeElectrolyzer));
        addItemTooltip(GTNLItemList.LargeElectrolyzer.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeElectromagnet
            .set(new LargeElectromagnet(21053, "LargeElectromagnet", TextLocalization.NameLargeElectromagnet));
        addItemTooltip(GTNLItemList.LargeElectromagnet.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeAssembler
            .set(new LargeAssembler(21054, "LargeAssembler", TextLocalization.NameLargeAssembler));
        addItemTooltip(GTNLItemList.LargeAssembler.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeMixer.set(new LargeMixer(21055, "LargeMixer", TextLocalization.NameLargeMixer));
        addItemTooltip(GTNLItemList.LargeMixer.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeCentrifuge
            .set(new LargeCentrifuge(21056, "LargeCentrifuge", TextLocalization.NameLargeCentrifuge));
        addItemTooltip(GTNLItemList.LargeCentrifuge.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LibraryOfRuina
            .set(new LibraryOfRuina(21057, "LibraryOfRuina", TextLocalization.NameLibraryOfRuina));
        addItemTooltip(GTNLItemList.LibraryOfRuina.get(1), AnimatedText.SNL_NLXCJH);

        GTNLItemList.LargeChemicalBath
            .set(new LargeChemicalBath(21058, "LargeChemicalBath", TextLocalization.NameLargeChemicalBath));
        addItemTooltip(GTNLItemList.LargeChemicalBath.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeAutoclave
            .set(new LargeAutoclave(21059, "LargeAutoclave", TextLocalization.NameLargeAutoclave));
        addItemTooltip(GTNLItemList.LargeAutoclave.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeSolidifier
            .set(new LargeSolidifier(21060, "LargeSolidifier", TextLocalization.NameLargeSolidifier));
        addItemTooltip(GTNLItemList.LargeSolidifier.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeExtractor
            .set(new LargeExtractor(21061, "LargeExtractor", TextLocalization.NameLargeExtractor));
        addItemTooltip(GTNLItemList.LargeExtractor.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.ReactionFurnace
            .set(new ReactionFurnace(21062, "ReactionFurnace", TextLocalization.NameReactionFurnace));
        addItemTooltip(GTNLItemList.ReactionFurnace.get(1), AnimatedText.SNL_NLXCJH);

        GTNLItemList.EnergyInfuser.set(new EnergyInfuser(21063, "EnergyInfuser", TextLocalization.NameEnergyInfuser));
        addItemTooltip(GTNLItemList.EnergyInfuser.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.LargeCanning.set(new LargeCanning(21064, "LargeCanning", TextLocalization.NameLargeCanning));
        addItemTooltip(GTNLItemList.LargeCanning.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.Digester.set(new Digester(21065, "Digester", TextLocalization.NameDigester));
        addItemTooltip(GTNLItemList.Digester.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.AlloyBlastSmelter
            .set(new AlloyBlastSmelter(21066, "AlloyBlastSmelter", TextLocalization.NameAlloyBlastSmelter));
        addItemTooltip(GTNLItemList.AlloyBlastSmelter.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeSteamExtractor
            .set(new LargeSteamExtractor(21067, "LargeSteamExtractor", TextLocalization.NameLargeSteamExtractor));
        addItemTooltip(GTNLItemList.LargeSteamExtractor.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeSteamOreWasher
            .set(new LargeSteamOreWasher(21068, "LargeSteamOreWasher", TextLocalization.NameLargeSteamOreWasher));
        addItemTooltip(GTNLItemList.LargeSteamOreWasher.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.LargeHammer.set(new LargeHammer(21069, "LargeHammer", TextLocalization.NameLargeHammer));
        addItemTooltip(GTNLItemList.LargeHammer.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.IsaMill.set(new IsaMill(21070, "IsaMill", TextLocalization.NameIsaMill));
        addItemTooltip(GTNLItemList.IsaMill.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.FlotationCellRegulator.set(
            new FlotationCellRegulator(21071, "FlotationCellRegulator", TextLocalization.NameFlotationCellRegulator));
        addItemTooltip(GTNLItemList.FlotationCellRegulator.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.VacuumDryingFurnace
            .set(new VacuumDryingFurnace(21072, "VacuumDryingFurnace", TextLocalization.NameVacuumDryingFurnace));
        addItemTooltip(GTNLItemList.VacuumDryingFurnace.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeDistillery
            .set(new LargeDistillery(21073, "LargeDistillery", TextLocalization.NameLargeDistillery));
        addItemTooltip(GTNLItemList.LargeDistillery.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.Incubator.set(new Incubator(21074, "Incubator", TextLocalization.NameIncubator));
        addItemTooltip(GTNLItemList.Incubator.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.LargeIncubator
            .set(new LargeIncubator(21075, "LargeIncubator", TextLocalization.NameLargeIncubator));
        addItemTooltip(GTNLItemList.LargeIncubator.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeEngravingLaser
            .set(new LargeEngravingLaser(21076, "LargeEngravingLaser", TextLocalization.NameLargeEngravingLaser));
        addItemTooltip(GTNLItemList.LargeEngravingLaser.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.FishingGround.set(new FishingGround(21077, "FishingGround", TextLocalization.NameFishingGround));
        addItemTooltip(GTNLItemList.FishingGround.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.ElementCopying
            .set(new ElementCopying(21078, "ElementCopying", TextLocalization.NameElementCopying));
        addItemTooltip(GTNLItemList.ElementCopying.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.WoodDistillation
            .set(new WoodDistillation(21079, "WoodDistillation", TextLocalization.NameWoodDistillation));
        addItemTooltip(GTNLItemList.WoodDistillation.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargePacker.set(new LargePacker(21080, "LargePacker", TextLocalization.NameLargePacker));
        addItemTooltip(GTNLItemList.LargePacker.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeAlloySmelter
            .set(new LargeAlloySmelter(21081, "LargeAlloySmelter", TextLocalization.NameLargeAlloySmelter));
        addItemTooltip(GTNLItemList.LargeAlloySmelter.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.MolecularTransformer
            .set(new MolecularTransformer(21082, "MolecularTransformer", TextLocalization.NameMolecularTransformer));
        addItemTooltip(GTNLItemList.MolecularTransformer.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.LargePyrolyseOven
            .set(new LargePyrolyseOven(21083, "LargePyrolyseOven", TextLocalization.NameLargePyrolyseOven));
        addItemTooltip(GTNLItemList.LargePyrolyseOven.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.LargeNaquadahReactor
            .set(new LargeNaquadahReactor(21084, "LargeNaquadahReactor", TextLocalization.NameLargeNaquadahReactor));
        addItemTooltip(GTNLItemList.LargeNaquadahReactor.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.DraconicFusionCrafting.set(
            new DraconicFusionCrafting(21085, "DraconicFusionCrafting", TextLocalization.NameDraconicFusionCrafting));
        addItemTooltip(GTNLItemList.DraconicFusionCrafting.get(1), AnimatedText.SNL_NLXCJH);

        GTNLItemList.LargeSteamExtruder
            .set(new LargeSteamExtruder(21086, "LargeSteamExtruder", TextLocalization.NameLargeSteamExtruder));
        addItemTooltip(GTNLItemList.LargeSteamExtruder.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.DecayHastener.set(new DecayHastener(21087, "DecayHastener", TextLocalization.NameDecayHastener));
        addItemTooltip(GTNLItemList.DecayHastener.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.PreciseAssembler
            .set(new PrecisionAssembler(21088, "PrecisionAssembler", TextLocalization.NamePrecisionAssembler));
        addItemTooltip(GTNLItemList.PreciseAssembler.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.MegaAlloyBlastSmelter
            .set(new MegaAlloyBlastSmelter(21089, "MegaAlloyBlastSmelter", TextLocalization.NameMegaAlloyBlastSmelter));
        addItemTooltip(GTNLItemList.MegaAlloyBlastSmelter.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.GrandAssemblyLine
            .set(new GrandAssemblyLine(21090, "GrandAssemblyLine", TextLocalization.NameGrandAssemblyLine));
        addItemTooltip(GTNLItemList.GrandAssemblyLine.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.FuelRefiningComplex
            .set(new FuelRefiningComplex(21091, "FuelRefiningComplex", TextLocalization.NameFuelRefiningComplex));
        addItemTooltip(GTNLItemList.FuelRefiningComplex.get(1), AnimatedText.SNL_QYZG);

        /**
         * ResourceCollectionModule used 21092
         *
         * @see #loadMachinesPostInit()
         */

        GTNLItemList.LuvKuangBiaoOneGiantNuclearFusionReactor.set(
            new LuvKuangBiaoOneGiantNuclearFusionReactor(
                21093,
                "KuangBiaoOneGiantNuclearFusionReactor",
                TextLocalization.NameLuvKuangBiaoOneGiantNuclearFusionReactor));
        addItemTooltip(GTNLItemList.LuvKuangBiaoOneGiantNuclearFusionReactor.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.ZpmKuangBiaoTwoGiantNuclearFusionReactor.set(
            new ZpmKuangBiaoTwoGiantNuclearFusionReactor(
                21094,
                "KuangBiaoTwoGiantNuclearFusionReactor",
                TextLocalization.NameZpmKuangBiaoTwoGiantNuclearFusionReactor));
        addItemTooltip(GTNLItemList.ZpmKuangBiaoTwoGiantNuclearFusionReactor.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.UvKuangBiaoThreeGiantNuclearFusionReactor.set(
            new UvKuangBiaoThreeGiantNuclearFusionReactor(
                21095,
                "KuangBiaoThreeGiantNuclearFusionReactor",
                TextLocalization.NameUvKuangBiaoThreeGiantNuclearFusionReactor));
        addItemTooltip(GTNLItemList.UvKuangBiaoThreeGiantNuclearFusionReactor.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.UhvKuangBiaoFourGiantNuclearFusionReactor.set(
            new UhvKuangBiaoFourGiantNuclearFusionReactor(
                21096,
                "KuangBiaoFourGiantNuclearFusionReactor",
                TextLocalization.NameUhvKuangBiaoFourGiantNuclearFusionReactor));
        addItemTooltip(GTNLItemList.UhvKuangBiaoFourGiantNuclearFusionReactor.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.UevKuangBiaoFiveGiantNuclearFusionReactor.set(
            new UevKuangBiaoFiveGiantNuclearFusionReactor(
                21097,
                "KuangBiaoFiveGiantNuclearFusionReactor",
                TextLocalization.NameUevKuangBiaoFiveGiantNuclearFusionReactor));
        addItemTooltip(GTNLItemList.UevKuangBiaoFiveGiantNuclearFusionReactor.get(1), AnimatedText.SNL_QYZG_SRP);

        GTNLItemList.LargeSteamCentrifuge
            .set(new LargeSteamCentrifuge(21098, "LargeSteamCentrifuge", TextLocalization.NameLargeSteamCentrifuge));
        addItemTooltip(GTNLItemList.LargeSteamCentrifuge.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeSteamHammer
            .set(new LargeSteamHammer(21099, "LargeSteamHammer", TextLocalization.NameLargeSteamHammer));
        addItemTooltip(GTNLItemList.LargeSteamHammer.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeSteamCompressor
            .set(new LargeSteamCompressor(21100, "LargeSteamCompressor", TextLocalization.NameLargeSteamCompressor));
        addItemTooltip(GTNLItemList.LargeSteamCompressor.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeSteamSifter
            .set(new LargeSteamSifter(21101, "LargeSteamSifter", TextLocalization.NameLargeSteamSifter));
        addItemTooltip(GTNLItemList.LargeSteamSifter.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.LargeBoilerBronze
            .set(new LargeBoiler.LargeBoilerBronze(21102, "LargeBoilerBronze", TextLocalization.NameLargeBoilerBronze));
        addItemTooltip(GTNLItemList.LargeBoilerBronze.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeBoilerSteel
            .set(new LargeBoiler.LargeBoilerSteel(21103, "LargeBoilerSteel", TextLocalization.NameLargeBoilerSteel));
        addItemTooltip(GTNLItemList.LargeBoilerSteel.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeBoilerTitanium.set(
            new LargeBoiler.LargeBoilerTitanium(
                21104,
                "LargeBoilerTitanium",
                TextLocalization.NameLargeBoilerTitanium));
        addItemTooltip(GTNLItemList.LargeBoilerTitanium.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeBoilerTungstenSteel.set(
            new LargeBoiler.LargeBoilerTungstenSteel(
                21105,
                "LargeBoilerTungstenSteel",
                TextLocalization.NameLargeBoilerTungstenSteel));
        addItemTooltip(GTNLItemList.LargeBoilerTungstenSteel.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.LargeSteamFormingPress.set(
            new LargeSteamFormingPress(21106, "LargeSteamFormingPress", TextLocalization.NameLargeSteamFormingPress));
        addItemTooltip(GTNLItemList.LargeSteamFormingPress.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.LargeSteamMixer
            .set(new LargeSteamMixer(21107, "LargeSteamMixer", TextLocalization.NameLargeSteamMixer));
        addItemTooltip(GTNLItemList.LargeSteamMixer.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.CrackerHub.set(new CrackerHub(21108, "CrackerHub", TextLocalization.NameCrackerHub));
        addItemTooltip(GTNLItemList.CrackerHub.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.AdvancedInfiniteDriller.set(
            new AdvancedInfiniteDriller(
                21109,
                "AdvancedInfiniteDriller",
                TextLocalization.NameAdvancedInfiniteDriller));
        addItemTooltip(GTNLItemList.AdvancedInfiniteDriller.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.ElectricBlastFurnace
            .set(new ElectricBlastFurnace(21110, "ElectricBlastFurnace", TextLocalization.NameElectricBlastFurnace));
        addItemTooltip(GTNLItemList.ElectricBlastFurnace.get(1), AnimatedText.SNL_SRP);

        GTNLItemList.PlatinumBasedTreatment.set(
            new PlatinumBasedTreatment(21111, "PlatinumBasedTreatment", TextLocalization.NamePlatinumBasedTreatment));
        addItemTooltip(GTNLItemList.PlatinumBasedTreatment.get(1), AnimatedText.SNL_PBTR);

        GTNLItemList.ShallowChemicalCoupling.set(
            new ShallowChemicalCoupling(
                21112,
                "ShallowChemicalCoupling",
                TextLocalization.NameShallowChemicalCoupling));
        addItemTooltip(GTNLItemList.ShallowChemicalCoupling.get(1), AnimatedText.SNL_SCCR);

        GTNLItemList.Steamgate.set(new Steamgate(21113, "Steamgate", TextLocalization.NameSteamgate));
        addItemTooltip(GTNLItemList.Steamgate.get(1), AnimatedText.SCIENCE_NOT_LEISURE);
        addItemTooltip(GTNLItemList.Steamgate.get(1), AnimatedText.SteamgateCredits);

        GTNLItemList.SteamGateAssembler
            .set(new SteamGateAssembler(21114, "SteamGateAssembler", TextLocalization.NameSteamGateAssembler));
        addItemTooltip(GTNLItemList.SteamGateAssembler.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamMegaCompressor
            .set(new SteamMegaCompressor(21115, "SteamMegaCompressor", TextLocalization.NameSteamMegaCompressor));
        addItemTooltip(GTNLItemList.SteamMegaCompressor.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.MegaSolarBoiler
            .set(new MegaSolarBoiler(21116, "MegaSolarBoiler", TextLocalization.NameMegaSolarBoiler));
        addItemTooltip(GTNLItemList.MegaSolarBoiler.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamCactusWonder
            .set(new SteamCactusWonder(21117, "SteamCactusWonder", TextLocalization.NameSteamCactusWonder));
        addItemTooltip(GTNLItemList.SteamCactusWonder.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamCarpenter
            .set(new SteamCarpenter(21118, "SteamCarpenter", TextLocalization.NameSteamCarpenter));
        addItemTooltip(GTNLItemList.SteamCarpenter.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamLavaMaker
            .set(new SteamLavaMaker(21119, "SteamLavaMaker", TextLocalization.NameSteamLavaMaker));
        addItemTooltip(GTNLItemList.SteamLavaMaker.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamManufacturer
            .set(new SteamManufacturer(21120, "SteamManufacturer", TextLocalization.NameSteamManufacturer));
        addItemTooltip(GTNLItemList.SteamManufacturer.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamRockBreaker
            .set(new SteamRockBreaker(21121, "SteamRockBreaker", TextLocalization.NameSteamRockBreaker));
        addItemTooltip(GTNLItemList.SteamRockBreaker.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamWoodcutter
            .set(new SteamWoodcutter(21122, "SteamWoodcutter", TextLocalization.NameSteamWoodcutter));
        addItemTooltip(GTNLItemList.SteamWoodcutter.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamExtractinator
            .set(new SteamExtractinator(21123, "SteamExtractinator", TextLocalization.NameSteamExtractinator));
        addItemTooltip(GTNLItemList.SteamExtractinator.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamFusionReactor
            .set(new SteamFusionReactor(21124, "SteamFusionReactor", TextLocalization.NameSteamFusionReactor));
        addItemTooltip(GTNLItemList.SteamFusionReactor.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HighPressureSteamFusionReactor.set(
            new HighPressureSteamFusionReactor(
                21125,
                "HighPressureSteamFusionReactor",
                TextLocalization.NameHighPressureSteamFusionReactor));
        addItemTooltip(GTNLItemList.HighPressureSteamFusionReactor.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamInfernalCokeOven
            .set(new SteamInfernalCokeOven(21126, "SteamInfernalCokeOven", TextLocalization.NameSteamInfernalCokeOven));
        addItemTooltip(GTNLItemList.SteamInfernalCokeOven.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.IntegratedAssemblyFacility.set(
            new IntegratedAssemblyFacility(
                21127,
                "IntegratedAssemblyFacility",
                TextLocalization.NameIntegratedAssemblyFacility));
        addItemTooltip(GTNLItemList.IntegratedAssemblyFacility.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.AdvancedCircuitAssemblyLine.set(
            new AdvancedCircuitAssemblyLine(
                21128,
                "AdvancedCircuitAssemblyLine",
                TextLocalization.NameAdvancedCircuitAssemblyLine));
        addItemTooltip(GTNLItemList.AdvancedCircuitAssemblyLine.get(1), AnimatedText.SNL_SCCR);

        GTNLItemList.NanoPhagocytosisPlant
            .set(new NanoPhagocytosisPlant(21129, "NanoPhagocytosisPlant", TextLocalization.NameNanoPhagocytosisPlant));
        addItemTooltip(GTNLItemList.NanoPhagocytosisPlant.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.MagneticEnergyReactionFurnace.set(
            new MagneticEnergyReactionFurnace(
                21130,
                "MagneticEnergyReactionFurnace",
                TextLocalization.NameMagneticEnergyReactionFurnace));
        addItemTooltip(GTNLItemList.MagneticEnergyReactionFurnace.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.NanitesIntegratedProcessingCenter.set(
            new NanitesIntegratedProcessingCenter(
                21131,
                "NanitesIntegratedProcessingCenter",
                TextLocalization.NameNanitesIntegratedProcessingCenter));
        addItemTooltip(GTNLItemList.NanitesIntegratedProcessingCenter.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.BioengineeringModule
            .set(new BioengineeringModule(21132, "BioengineeringModule", TextLocalization.NameBioengineeringModule));
        addItemTooltip(GTNLItemList.BioengineeringModule.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.PolymerTwistingModule
            .set(new PolymerTwistingModule(21133, "PolymerTwistingModule", TextLocalization.NamePolymerTwistingModule));
        addItemTooltip(GTNLItemList.PolymerTwistingModule.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.OreExtractionModule
            .set(new OreExtractionModule(21134, "OreExtractionModule", TextLocalization.NameOreExtractionModule));
        addItemTooltip(GTNLItemList.OreExtractionModule.get(1), AnimatedText.SNL_QYZG);

        /**
         * SuperSpaceElevator used 21135
         *
         * @see #loadMachinesPostInit()
         */

        GTNLItemList.LargeBioLab.set(new LargeBioLab(21136, "LargeBioLab", TextLocalization.NameLargeBioLab));
        addItemTooltip(GTNLItemList.LargeBioLab.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.LargeGasCollector
            .set(new LargeGasCollector(21137, "LargeGasCollector", TextLocalization.NameLargeGasCollector));
        addItemTooltip(GTNLItemList.LargeGasCollector.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        // Special Machine
        GTNLItemList.CheatOreProcessingFactory.set(
            new CheatOreProcessingFactory(
                21919,
                "CheatOreProcessingFactory",
                TextLocalization.NameCheatOreProcessingFactory));
        addItemTooltip(GTNLItemList.CheatOreProcessingFactory.get(1), AnimatedText.SNL_QYZG);

        GTNLItemList.NineIndustrialMultiMachine.set(
            new NineIndustrialMultiMachine(
                21920,
                "NineIndustrialMultiMachine",
                TextLocalization.NameNineIndustrialMultiMachine));
        addItemTooltip(GTNLItemList.NineIndustrialMultiMachine.get(1), AnimatedText.SNL_QYZG);
    }

    public static void registerMTEHatch() {
        Set<Fluid> acceptedFluids = new HashSet<>();
        acceptedFluids.add(
            FluidUtils.getFluidStack("fluidmana", 1)
                .getFluid());

        if (Mods.TwistSpaceTechnology.isModLoaded()) {
            acceptedFluids.add(
                FluidUtils.getFluidStack("liquid mana", 1)
                    .getFluid());
        }

        GTNLItemList.FluidManaInputHatch.set(
            new CustomFluidHatch(
                ImmutableSet.copyOf(acceptedFluids),
                512000,
                21501,
                "Fluid Mana Input Hatch",
                TextLocalization.FluidManaInputHatch,
                6));
        addItemTooltip(GTNLItemList.FluidManaInputHatch.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.FluidIceInputHatch.set(
            new CustomFluidHatch(
                ImmutableSet.of(
                    FluidUtils.getFluidStack("ice", 1)
                        .getFluid()),
                256000,
                21502,
                "Fluid Ice Input Hatch",
                TextLocalization.FluidIceInputHatch,
                5));
        addItemTooltip(GTNLItemList.FluidIceInputHatch.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.FluidBlazeInputHatch.set(
            new CustomFluidHatch(
                ImmutableSet.of(
                    FluidUtils.getFluidStack("molten.blaze", 1)
                        .getFluid()),
                256000,
                21503,
                "Fluid Blaze Input Hatch",
                TextLocalization.FluidBlazeInputHatch,
                5));
        addItemTooltip(GTNLItemList.FluidBlazeInputHatch.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SuperCraftingInputHatchME.set(
            new SuperCraftingInputHatchME(
                21504,
                "Super Crafting Input Buffer (ME)",
                TextLocalization.SuperCraftingInputHatchME,
                true));
        addItemTooltip(GTNLItemList.SuperCraftingInputHatchME.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SuperCraftingInputBusME.set(
            new SuperCraftingInputHatchME(
                21505,
                "Super Crafting Input Bus (ME)",
                TextLocalization.SuperCraftingInputBusME,
                false));
        addItemTooltip(GTNLItemList.SuperCraftingInputBusME.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousSolidifierHatch.set(
            new HumongousSolidifierHatch(
                21506,
                "Humongous Solidifier Hatch",
                TextLocalization.HumongousSolidifierHatch,
                14));
        addItemTooltip(GTNLItemList.HumongousSolidifierHatch.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DebugEnergyHatch
            .set(new DebugEnergyHatch(21507, "Debug Energy Hatch", TextLocalization.DebugEnergyHatch, 14));
        addItemTooltip(GTNLItemList.DebugEnergyHatch.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchEV
            .set(new NinefoldInputHatch(21508, 9, "Ninefold Input Hatch EV", TextLocalization.NinefoldInputHatchEV, 4));
        addItemTooltip(GTNLItemList.NinefoldInputHatchEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchIV
            .set(new NinefoldInputHatch(21509, 9, "Ninefold Input Hatch IV", TextLocalization.NinefoldInputHatchIV, 5));
        addItemTooltip(GTNLItemList.NinefoldInputHatchIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchLuV.set(
            new NinefoldInputHatch(21510, 9, "Ninefold Input Hatch LuV", TextLocalization.NinefoldInputHatchLuV, 6));
        addItemTooltip(GTNLItemList.NinefoldInputHatchLuV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchZPM.set(
            new NinefoldInputHatch(21511, 9, "Ninefold Input Hatch ZPM", TextLocalization.NinefoldInputHatchZPM, 7));
        addItemTooltip(GTNLItemList.NinefoldInputHatchZPM.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchUV
            .set(new NinefoldInputHatch(21512, 9, "Ninefold Input Hatch UV", TextLocalization.NinefoldInputHatchUV, 8));
        addItemTooltip(GTNLItemList.NinefoldInputHatchUV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchUHV.set(
            new NinefoldInputHatch(21513, 9, "Ninefold Input Hatch UHV", TextLocalization.NinefoldInputHatchUHV, 9));
        addItemTooltip(GTNLItemList.NinefoldInputHatchUHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchUEV.set(
            new NinefoldInputHatch(21514, 9, "Ninefold Input Hatch UEV", TextLocalization.NinefoldInputHatchUEV, 10));
        addItemTooltip(GTNLItemList.NinefoldInputHatchUEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchUIV.set(
            new NinefoldInputHatch(21515, 9, "Ninefold Input Hatch UIV", TextLocalization.NinefoldInputHatchUIV, 11));
        addItemTooltip(GTNLItemList.NinefoldInputHatchUIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchUMV.set(
            new NinefoldInputHatch(21516, 9, "Ninefold Input Hatch UMV", TextLocalization.NinefoldInputHatchUMV, 12));
        addItemTooltip(GTNLItemList.NinefoldInputHatchUMV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchUXV.set(
            new NinefoldInputHatch(21517, 9, "Ninefold Input Hatch UXV", TextLocalization.NinefoldInputHatchUXV, 13));
        addItemTooltip(GTNLItemList.NinefoldInputHatchUXV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldInputHatchMAX.set(
            new NinefoldInputHatch(21518, 9, "Ninefold Input Hatch MAX", TextLocalization.NinefoldInputHatchMAX, 14));
        addItemTooltip(GTNLItemList.NinefoldInputHatchMAX.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousNinefoldInputHatch.set(
            new HumongousNinefoldInputHatch(
                21519,
                9,
                "Humongous Ninefold Input Hatch",
                TextLocalization.HumongousNinefoldInputHatch));
        addItemTooltip(GTNLItemList.HumongousNinefoldInputHatch.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchLV
            .set(new DualInputHatch(21520, "Dual Input Hatch LV", TextLocalization.DualInputHatchLV, 1));
        addItemTooltip(GTNLItemList.DualInputHatchLV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchMV
            .set(new DualInputHatch(21521, "Dual Input Hatch MV", TextLocalization.DualInputHatchMV, 2));
        addItemTooltip(GTNLItemList.DualInputHatchMV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchHV
            .set(new DualInputHatch(21522, "Dual Input Hatch HV", TextLocalization.DualInputHatchHV, 3));
        addItemTooltip(GTNLItemList.DualInputHatchHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchEV
            .set(new DualInputHatch(21523, "Dual Input Hatch EV", TextLocalization.DualInputHatchEV, 4));
        addItemTooltip(GTNLItemList.DualInputHatchEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchIV
            .set(new DualInputHatch(21524, "Dual Input Hatch IV", TextLocalization.DualInputHatchIV, 5));
        addItemTooltip(GTNLItemList.DualInputHatchIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchLuV
            .set(new DualInputHatch(21525, "Dual Input Hatch LuV", TextLocalization.DualInputHatchLuV, 6));
        addItemTooltip(GTNLItemList.DualInputHatchLuV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchZPM
            .set(new DualInputHatch(21526, "Dual Input Hatch ZPM", TextLocalization.DualInputHatchZPM, 7));
        addItemTooltip(GTNLItemList.DualInputHatchZPM.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchUV
            .set(new DualInputHatch(21527, "Dual Input Hatch UV", TextLocalization.DualInputHatchUV, 8));
        addItemTooltip(GTNLItemList.DualInputHatchUV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchUHV
            .set(new DualInputHatch(21528, "Dual Input Hatch UHV", TextLocalization.DualInputHatchUHV, 9));
        addItemTooltip(GTNLItemList.DualInputHatchUHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchUEV
            .set(new DualInputHatch(21529, "Dual Input Hatch UEV", TextLocalization.DualInputHatchUEV, 10));
        addItemTooltip(GTNLItemList.DualInputHatchUEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchUIV
            .set(new DualInputHatch(21530, "Dual Input Hatch UIV", TextLocalization.DualInputHatchUIV, 11));
        addItemTooltip(GTNLItemList.DualInputHatchUIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchUMV
            .set(new DualInputHatch(21531, "Dual Input Hatch UMV", TextLocalization.DualInputHatchUMV, 12));
        addItemTooltip(GTNLItemList.DualInputHatchUMV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchUXV
            .set(new DualInputHatch(21532, "Dual Input Hatch UXV", TextLocalization.DualInputHatchUXV, 13));
        addItemTooltip(GTNLItemList.DualInputHatchUXV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.DualInputHatchMAX
            .set(new DualInputHatch(21533, "Dual Input Hatch MAX", TextLocalization.DualInputHatchMAX, 14));
        addItemTooltip(GTNLItemList.DualInputHatchMAX.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SuperCraftingInputProxy.set(
            new SuperCraftingInputProxy(21534, "Super Crafting Input Proxy", TextLocalization.SuperCraftingInputProxy)
                .getStackForm(1L));
        addItemTooltip(GTNLItemList.SuperCraftingInputProxy.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SuperDataAccessHatch
            .set(new SuperDataAccessHatch(21535, "Super Data Access Hatch", TextLocalization.SuperDataAccessHatch, 14));
        addItemTooltip(GTNLItemList.SuperDataAccessHatch.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.BigSteamInputHatch.set(
            new CustomFluidHatch(
                ImmutableSet.of(
                    FluidUtils.getSteam(1)
                        .getFluid(),
                    FluidUtils.getSuperHeatedSteam(1)
                        .getFluid(),
                    FluidRegistry.getFluidStack("supercriticalsteam", 1)
                        .getFluid(),
                    MaterialPool.CompressedSteam.getMolten(1)
                        .getFluid()),
                4096000,
                22518,
                "Big Steam Input Hatch",
                TextLocalization.BigSteamInputHatch,
                1));
        addItemTooltip(GTNLItemList.BigSteamInputHatch.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchLV.set(
            new ParallelControllerHatch(
                22548,
                "ParallelControllerHatchLV",
                TextLocalization.ParallelControllerHatchLV,
                1));
        addItemTooltip(GTNLItemList.ParallelControllerHatchLV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchMV.set(
            new ParallelControllerHatch(
                22549,
                "ParallelControllerHatchMV",
                TextLocalization.ParallelControllerHatchMV,
                2));
        addItemTooltip(GTNLItemList.ParallelControllerHatchMV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchHV.set(
            new ParallelControllerHatch(
                22550,
                "ParallelControllerHatchHV",
                TextLocalization.ParallelControllerHatchHV,
                3));
        addItemTooltip(GTNLItemList.ParallelControllerHatchHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchEV.set(
            new ParallelControllerHatch(
                22551,
                "ParallelControllerHatchEV",
                TextLocalization.ParallelControllerHatchEV,
                4));
        addItemTooltip(GTNLItemList.ParallelControllerHatchEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchIV.set(
            new ParallelControllerHatch(
                22552,
                "ParallelControllerHatchIV",
                TextLocalization.ParallelControllerHatchIV,
                5));
        addItemTooltip(GTNLItemList.ParallelControllerHatchIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchLuV.set(
            new ParallelControllerHatch(
                22553,
                "ParallelControllerHatchLuV",
                TextLocalization.ParallelControllerHatchLuV,
                6));
        addItemTooltip(GTNLItemList.ParallelControllerHatchLuV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchZPM.set(
            new ParallelControllerHatch(
                22554,
                "ParallelControllerHatchZPM",
                TextLocalization.ParallelControllerHatchZPM,
                7));
        addItemTooltip(GTNLItemList.ParallelControllerHatchZPM.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchUV.set(
            new ParallelControllerHatch(
                22555,
                "ParallelControllerHatchUV",
                TextLocalization.ParallelControllerHatchUV,
                8));
        addItemTooltip(GTNLItemList.ParallelControllerHatchUV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchUHV.set(
            new ParallelControllerHatch(
                22556,
                "ParallelControllerHatchUHV",
                TextLocalization.ParallelControllerHatchUHV,
                9));
        addItemTooltip(GTNLItemList.ParallelControllerHatchUHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchUEV.set(
            new ParallelControllerHatch(
                22557,
                "ParallelControllerHatchUEV",
                TextLocalization.ParallelControllerHatchUEV,
                10));
        addItemTooltip(GTNLItemList.ParallelControllerHatchUEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchUIV.set(
            new ParallelControllerHatch(
                22558,
                "ParallelControllerHatchUIV",
                TextLocalization.ParallelControllerHatchUIV,
                11));
        addItemTooltip(GTNLItemList.ParallelControllerHatchUIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchUMV.set(
            new ParallelControllerHatch(
                22559,
                "ParallelControllerHatchUMV",
                TextLocalization.ParallelControllerHatchUMV,
                12));
        addItemTooltip(GTNLItemList.ParallelControllerHatchUMV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchUXV.set(
            new ParallelControllerHatch(
                22560,
                "ParallelControllerHatchUXV",
                TextLocalization.ParallelControllerHatchUXV,
                13));
        addItemTooltip(GTNLItemList.ParallelControllerHatchUXV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ParallelControllerHatchMAX.set(
            new ParallelControllerHatch(
                22561,
                "ParallelControllerHatchMAX",
                TextLocalization.ParallelControllerHatchMAX,
                14));
        addItemTooltip(GTNLItemList.ParallelControllerHatchMAX.get(1), AnimatedText.SCIENCE_NOT_LEISURE);
    }

    @Deprecated
    public static void registerTestMachine() {
        GTNLItemList.QuadrupleOutputHatchEV.set(
            new DualOutputHatch(21700, 4, "Quadruple Output Hatch EV", TextLocalization.QuadrupleOutputHatchEV, 4));
        addItemTooltip(GTNLItemList.QuadrupleOutputHatchEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.NinefoldOutputHatchEV
            .set(new DualOutputHatch(21701, 9, "Ninefold Output Hatch EV", TextLocalization.NinefoldOutputHatchEV, 4));
        addItemTooltip(GTNLItemList.NinefoldOutputHatchEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusULV
            .set(new HumongousInputBus(21702, "Humongous Input Bus ULV", TextLocalization.HumongousInputBusULV, 0));
        addItemTooltip(GTNLItemList.HumongousInputBusULV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusLV
            .set(new HumongousInputBus(21703, "Humongous Input Bus LV", TextLocalization.HumongousInputBusLV, 1));
        addItemTooltip(GTNLItemList.HumongousInputBusLV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusMV
            .set(new HumongousInputBus(21704, "Humongous Input Bus MV", TextLocalization.HumongousInputBusMV, 2));
        addItemTooltip(GTNLItemList.HumongousInputBusMV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusHV
            .set(new HumongousInputBus(21705, "Humongous Input Bus HV", TextLocalization.HumongousInputBusHV, 3));
        addItemTooltip(GTNLItemList.HumongousInputBusHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusEV
            .set(new HumongousInputBus(21706, "Humongous Input Bus EV", TextLocalization.HumongousInputBusEV, 4));
        addItemTooltip(GTNLItemList.HumongousInputBusEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusIV
            .set(new HumongousInputBus(21707, "Humongous Input Bus IV", TextLocalization.HumongousInputBusIV, 5));
        addItemTooltip(GTNLItemList.HumongousInputBusIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusLuV
            .set(new HumongousInputBus(21708, "Humongous Input Bus LuV", TextLocalization.HumongousInputBusLuV, 6));
        addItemTooltip(GTNLItemList.HumongousInputBusLuV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusZPM
            .set(new HumongousInputBus(21709, "Humongous Input Bus ZPM", TextLocalization.HumongousInputBusZPM, 7));
        addItemTooltip(GTNLItemList.HumongousInputBusZPM.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusUV
            .set(new HumongousInputBus(21710, "Humongous Input Bus UV", TextLocalization.HumongousInputBusUV, 8));
        addItemTooltip(GTNLItemList.HumongousInputBusUV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusUHV
            .set(new HumongousInputBus(21711, "Humongous Input Bus UHV", TextLocalization.HumongousInputBusUHV, 9));
        addItemTooltip(GTNLItemList.HumongousInputBusUHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusUEV
            .set(new HumongousInputBus(21712, "Humongous Input Bus UEV", TextLocalization.HumongousInputBusUEV, 10));
        addItemTooltip(GTNLItemList.HumongousInputBusUEV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusUIV
            .set(new HumongousInputBus(21713, "Humongous Input Bus UIV", TextLocalization.HumongousInputBusUIV, 11));
        addItemTooltip(GTNLItemList.HumongousInputBusUIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusUMV
            .set(new HumongousInputBus(21714, "Humongous Input Bus UMV", TextLocalization.HumongousInputBusUMV, 12));
        addItemTooltip(GTNLItemList.HumongousInputBusUMV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusUXV
            .set(new HumongousInputBus(21715, "Humongous Input Bus UXV", TextLocalization.HumongousInputBusUXV, 13));
        addItemTooltip(GTNLItemList.HumongousInputBusUXV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.HumongousInputBusMAX
            .set(new HumongousInputBus(21716, "Humongous Input Bus MAX", TextLocalization.HumongousInputBusMAX, 14));
        addItemTooltip(GTNLItemList.HumongousInputBusMAX.get(1), AnimatedText.SCIENCE_NOT_LEISURE);
    }

    public static void registerBasicMachine() {
        GTNLItemList.SteamTurbineLV
            .set(new SteamTurbine(22501, "BasicSteamTurbine", TextLocalization.SteamTurbineLV, 1));
        addItemTooltip(GTNLItemList.SteamTurbineLV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamTurbineMV
            .set(new SteamTurbine(22502, "AdvancedSteamTurbine", TextLocalization.SteamTurbineMV, 2));
        addItemTooltip(GTNLItemList.SteamTurbineMV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamTurbineHV
            .set(new SteamTurbine(22503, "AdvancedSteamTurbineII", TextLocalization.SteamTurbineHV, 3));
        addItemTooltip(GTNLItemList.SteamTurbineHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamAssemblerBronze
            .set(new SteamAssemblerBronze(22504, "SteamAssembler", TextLocalization.SteamAssemblerBronze));
        addItemTooltip(GTNLItemList.SteamAssemblerBronze.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.SteamAssemblerSteel
            .set(new SteamAssemblerSteel(22505, "HighPressureSteamAssembler", TextLocalization.SteamAssemblerSteel));
        addItemTooltip(GTNLItemList.SteamAssemblerSteel.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ManaTank.set(new ManaTank(22524, "ManaTank", TextLocalization.ManaTank));
        addItemTooltip(GTNLItemList.ManaTank.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ManaDynamoHatchLV
            .set(new ManaDynamoHatch(22525, "ManaDynamoHatchLV", TextLocalization.ManaDynamoHatchLV, 1, 16));
        addItemTooltip(GTNLItemList.ManaDynamoHatchLV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ManaDynamoHatchHV
            .set(new ManaDynamoHatch(22526, "ManaDynamoHatchHV", TextLocalization.ManaDynamoHatchHV, 3, 16));
        addItemTooltip(GTNLItemList.ManaDynamoHatchHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ManaDynamoHatchIV
            .set(new ManaDynamoHatch(22527, "ManaDynamoHatchIV", TextLocalization.ManaDynamoHatchIV, 5, 16));
        addItemTooltip(GTNLItemList.ManaDynamoHatchIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ManaDynamoHatchZPM
            .set(new ManaDynamoHatch(22528, "ManaDynamoHatchZPM", TextLocalization.ManaDynamoHatchZPM, 7, 16));
        addItemTooltip(GTNLItemList.ManaDynamoHatchZPM.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ManaEnergyHatchLV
            .set(new ManaEnergyHatch(22544, "ManaEnergyHatchLV", TextLocalization.ManaEnergyHatchLV, 1, 16));
        addItemTooltip(GTNLItemList.ManaEnergyHatchLV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ManaEnergyHatchHV
            .set(new ManaEnergyHatch(22545, "ManaEnergyHatchHV", TextLocalization.ManaEnergyHatchHV, 3, 16));
        addItemTooltip(GTNLItemList.ManaEnergyHatchHV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ManaEnergyHatchIV
            .set(new ManaEnergyHatch(22546, "ManaEnergyHatchIV", TextLocalization.ManaEnergyHatchIV, 5, 16));
        addItemTooltip(GTNLItemList.ManaEnergyHatchIV.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.ManaEnergyHatchZPM
            .set(new ManaEnergyHatch(22547, "ManaEnergyHatchZPM", TextLocalization.ManaEnergyHatchZPM, 7, 16));
        addItemTooltip(GTNLItemList.ManaEnergyHatchZPM.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        GTNLItemList.GasCollectorLV.set(
            new MTEBasicMachineWithRecipe(
                22562,
                "GasCollectorLV",
                TextLocalization.GasCollectorLV,
                1,
                new String[] { TextLocalization.Tooltip_GasCollector_00,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorMV.set(
            new MTEBasicMachineWithRecipe(
                22563,
                "GasCollectorMV",
                TextLocalization.GasCollectorMV,
                2,
                new String[] { TextLocalization.Tooltip_GasCollector_00,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorHV.set(
            new MTEBasicMachineWithRecipe(
                22564,
                "GasCollectorHV",
                TextLocalization.GasCollectorHV,
                3,
                new String[] { TextLocalization.Tooltip_GasCollector_00,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorEV.set(
            new MTEBasicMachineWithRecipe(
                22565,
                "GasCollectorEV",
                TextLocalization.GasCollectorEV,
                4,
                new String[] { TextLocalization.Tooltip_GasCollector_00,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorIV.set(
            new MTEBasicMachineWithRecipe(
                22566,
                "GasCollectorIV",
                TextLocalization.GasCollectorIV,
                5,
                new String[] { TextLocalization.Tooltip_GasCollector_01,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorLuV.set(
            new MTEBasicMachineWithRecipe(
                22567,
                "GasCollectorLuV",
                TextLocalization.GasCollectorLuV,
                6,
                new String[] { TextLocalization.Tooltip_GasCollector_01,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorZPM.set(
            new MTEBasicMachineWithRecipe(
                22568,
                "GasCollectorZPM",
                TextLocalization.GasCollectorZPM,
                7,
                new String[] { TextLocalization.Tooltip_GasCollector_01,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorUV.set(
            new MTEBasicMachineWithRecipe(
                22569,
                "GasCollectorUV",
                TextLocalization.GasCollectorUV,
                8,
                new String[] { TextLocalization.Tooltip_GasCollector_02,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorUHV.set(
            new MTEBasicMachineWithRecipe(
                22570,
                "GasCollectorUHV",
                TextLocalization.GasCollectorUHV,
                9,
                new String[] { TextLocalization.Tooltip_GasCollector_02,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorUEV.set(
            new MTEBasicMachineWithRecipe(
                22571,
                "GasCollectorUEV",
                TextLocalization.GasCollectorUEV,
                10,
                new String[] { TextLocalization.Tooltip_GasCollector_02,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorUIV.set(
            new MTEBasicMachineWithRecipe(
                22572,
                "GasCollectorUIV",
                TextLocalization.GasCollectorUIV,
                11,
                new String[] { TextLocalization.Tooltip_GasCollector_02,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorUMV.set(
            new MTEBasicMachineWithRecipe(
                22573,
                "GasCollectorUMV",
                TextLocalization.GasCollectorUMV,
                12,
                new String[] { TextLocalization.Tooltip_GasCollector_02,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));

        GTNLItemList.GasCollectorUXV.set(
            new MTEBasicMachineWithRecipe(
                22574,
                "GasCollectorUXV",
                TextLocalization.GasCollectorUXV,
                13,
                new String[] { TextLocalization.Tooltip_GasCollector_03,
                    StatCollector.translateToLocal("GT5U.MBTT.MachineType") + ": "
                        + EnumChatFormatting.YELLOW
                        + TextLocalization.GasCollectorRecipeType
                        + EnumChatFormatting.RESET },
                RecipeRegister.GasCollectorRecipes,
                1,
                1,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "GAS_COLLECTOR",
                null).getStackForm(1L));
    }

    public static void registerMTEWireAndPipe() {
        CrackRecipeAdder.registerWire(22506, MaterialPool.Stargate, 2147483647, 2147483647, 0, true);
        MoreMaterialToolUtil.generateGTFluidPipes(Materials.BlueAlloy, 22519, 4000, 3000, true);
        CrackRecipeAdder.registerPipe(22529, MaterialPool.CompressedSteam, 250000, 10000, true);
        CrackRecipeAdder.registerPipe(22534, MaterialPool.Stronze, 15000, 10000, true);
        CrackRecipeAdder.registerPipe(22539, MaterialPool.Breel, 10000, 10000, true);
        // 这个可用 MoreMaterialToolUtil.generateNonGTFluidPipes(GregtechOrePrefixes.GT_Materials.Void, 22013, 500, 2000,
        // true);
        // 这个渲染炸了 MoreMaterialToolUtil.registerPipeGTPP(22020, MaterialsAlloy.BLOODSTEEL, 123, 123, true);
    }

    public static void loadMachinesPostInit() {
        ResourceCollectionModule = new ResourceCollectionModule(
            21092,
            "ResourceCollectionModule",
            TextLocalization.NameResourceCollectionModule).getStackForm(1);
        GTNLItemList.ResourceCollectionModule.set(ResourceCollectionModule);
        addItemTooltip(GTNLItemList.ResourceCollectionModule.get(1), AnimatedText.SCIENCE_NOT_LEISURE);

        SuperSpaceElevator = new SuperSpaceElevator(
            21135,
            "SuperSpaceElevator",
            TextLocalization.NameSuperSpaceElevator).getStackForm(1);
        GTNLItemList.SuperSpaceElevator.set(SuperSpaceElevator);
        addItemTooltip(GTNLItemList.SuperSpaceElevator.get(1), AnimatedText.SNL_QYZG);
    }

    private static void registerCovers() {
        GregTechAPI.registerCover(
            GTNLItemList.HydraulicPump.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_PUMP)),
            new CoverPump(1048576, TextureFactory.of(OVERLAY_PUMP)));

        GregTechAPI.registerCover(
            GTNLItemList.HydraulicConveyor.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_CONVEYOR)),
            new CoverConveyor(1, 16, TextureFactory.of(OVERLAY_CONVEYOR)));

        GregTechAPI.registerCover(
            GTNLItemList.HydraulicRegulator.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_PUMP)),
            new CoverFluidRegulator(1048576, TextureFactory.of(OVERLAY_PUMP)));

        GregTechAPI.registerCover(
            GTNLItemList.HydraulicSteamValve.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_VALVE)),
            new CoverSteamValve(16777216, TextureFactory.of(OVERLAY_VALVE)));

        GregTechAPI.registerCover(
            GTNLItemList.HydraulicSteamRegulator.get(1L),
            TextureFactory.of(MACHINE_CASINGS[1][0], TextureFactory.of(OVERLAY_VALVE)),
            new CoverSteamRegulator(16777216, TextureFactory.of(OVERLAY_VALVE)));

    }

    public static void registerGlasses() {
        BorosilicateGlass.registerGlass(BasicBlocks.PlayerDoll, 0, (byte) 12);
        BorosilicateGlass.registerGlass(BasicBlocks.MetaBlockGlass, 0, (byte) 10);
        BorosilicateGlass.registerGlass(BasicBlocks.MetaBlockGlass, 1, (byte) 8);
        BorosilicateGlass.registerGlass(BasicBlocks.MetaBlockGlass, 2, (byte) 7);

        for (int LampMeta = 1; LampMeta <= 32; LampMeta++) {
            BorosilicateGlass.registerGlass(BasicBlocks.MetaBlockGlow, LampMeta, (byte) 3);
        }

        for (int LampOffMeta = 3; LampOffMeta <= 34; LampOffMeta++) {
            BorosilicateGlass.registerGlass(BasicBlocks.MetaBlock, LampOffMeta, (byte) 3);
        }
    }

    public static void run() {
        Logger.INFO("GTNL Content | Registering MTE Block Machine.");
        registerMTEHatch();
        loadMachines();
        registerMTEWireAndPipe();
        registerBasicMachine();
        registerCovers();
    }
}
