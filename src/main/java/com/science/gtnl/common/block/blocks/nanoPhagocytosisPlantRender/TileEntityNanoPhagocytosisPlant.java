package com.science.gtnl.common.block.blocks.nanoPhagocytosisPlantRender;

import static tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor.*;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizons.modularui.api.math.Color;

import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.color.StarColorSetting;

public class TileEntityNanoPhagocytosisPlant extends TileEntity {

    private float radius = 1;
    private float rotationSpeed = 10;
    private float rotAngle = 0, rotAxisX = 1, rotAxisY = 0, rotAxisZ = 0;
    private AxisAlignedBB renderBoundingBox;

    private ForgeOfGodsStarColor starColor = ForgeOfGodsStarColor.DEFAULT;

    // current color data
    private int currentColor = Color.rgb(DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
    private float gamma = DEFAULT_GAMMA;

    // interpolation color data
    private int cycleStep;
    private int interpIndex;
    private int interpA;
    private int interpB;
    private float interpGammaA;
    private float interpGammaB;

    private static final String NBT_TAG = "NPPRender:";
    private static final String ROTATION_SPEED_NBT_TAG = NBT_TAG + "ROTATION";
    private static final String SIZE_NBT_TAG = NBT_TAG + "RADIUS";
    private static final String ROT_ANGLE_NBT_TAG = NBT_TAG + "ROT_ANGLE";
    private static final String ROT_AXIS_X_NBT_TAG = NBT_TAG + "ROT_AXIS_X";
    private static final String ROT_AXIS_Y_NBT_TAG = NBT_TAG + "ROT_AXIS_Y";
    private static final String ROT_AXIS_Z_NBT_TAG = NBT_TAG + "ROT_AXIS_Z";
    private static final String STAR_COLOR_TAG = NBT_TAG + "STAR_COLOR";

    private static final double RING_RADIUS = 63;
    private static final double BEAM_LENGTH = 59;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (renderBoundingBox == null) {
            double x = this.xCoord;
            double y = this.yCoord;
            double z = this.zCoord;

            // This could possibly be made smaller by figuring out the beam direction,
            // but since this is not always known (set dynamically by the MTE), this
            // currently just bounds as if the beam is in all 4 directions.
            renderBoundingBox = AxisAlignedBB.getBoundingBox(
                x - RING_RADIUS - BEAM_LENGTH,
                y - RING_RADIUS - BEAM_LENGTH,
                z - RING_RADIUS - BEAM_LENGTH,
                x + RING_RADIUS + BEAM_LENGTH + 1,
                y + RING_RADIUS + BEAM_LENGTH + 1,
                z + RING_RADIUS + BEAM_LENGTH + 1);
        }
        return renderBoundingBox;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return Double.MAX_VALUE;
    }

    public void setStarRadius(float size) {
        this.radius = size;
    }

    public float getStarRadius() {
        return radius;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float speed) {
        this.rotationSpeed = speed;
    }

    public float getColorR() {
        return Color.getRedF(currentColor);
    }

    public float getColorG() {
        return Color.getGreenF(currentColor);
    }

    public float getColorB() {
        return Color.getBlueF(currentColor);
    }

    public float getGamma() {
        return gamma;
    }

    public void setColor(ForgeOfGodsStarColor color) {
        this.starColor = color;
        if (this.starColor == null) {
            this.starColor = ForgeOfGodsStarColor.DEFAULT;
        }

        StarColorSetting colorSetting = starColor.getColor(0);
        currentColor = Color.rgb(colorSetting.getColorR(), colorSetting.getColorG(), colorSetting.getColorB());
        gamma = colorSetting.getGamma();

        if (starColor.numColors() > 1) {
            cycleStep = 0;
            interpA = currentColor;
            interpGammaA = gamma;
            colorSetting = starColor.getColor(1);
            interpB = Color.rgb(colorSetting.getColorR(), colorSetting.getColorG(), colorSetting.getColorB());
            interpGammaB = colorSetting.getGamma();
        }
    }

    public float getRotAngle() {
        return rotAngle;
    }

    public float getRotAxisX() {
        return rotAxisX;
    }

    public float getRotAxisY() {
        return rotAxisY;
    }

    public float getRotAxisZ() {
        return rotAxisZ;
    }

    public void setRenderRotation(Rotation rotation, ForgeDirection direction) {
        switch (direction) {
            case SOUTH -> rotAngle = 90;
            case NORTH -> rotAngle = 90;
            case WEST -> rotAngle = 0;
            case EAST -> rotAngle = 180;
            case UP -> rotAngle = -90;
            case DOWN -> rotAngle = -90;
        }
        rotAxisX = 0;
        rotAxisY = direction.offsetZ + direction.offsetX;
        rotAxisZ = direction.offsetY;

        updateToClient();
    }

    public float getLensDistance(int lensID) {
        return switch (lensID) {
            case 0 -> -7.6875f;
            case 1 -> -6.8125f;
            case 2 -> -5.5625f;
            default -> throw new IllegalStateException("Unexpected value: " + lensID);
        };
    }

    public float getLenRadius(int lensID) {
        return switch (lensID) {
            case 0 -> 0.1375f;
            case 1 -> 0.4375f;
            case 2 -> 0.625f;
            default -> throw new IllegalStateException("Unexpected value: " + lensID);
        };
    }

    public float getStartAngle() {
        float x = -getLensDistance(2);
        float y = getLenRadius(2);
        float alpha = (float) Math.atan2(y, x);
        float beta = (float) Math.asin(radius / Math.sqrt(x * x + y * y));
        return alpha + ((float) Math.PI / 2 - beta);
    }

    public static float interpolate(float x0, float x1, float y0, float y1, float x) {
        return y0 + ((x - x0) * (y1 - y0)) / (x1 - x0);
    }

    public void incrementColors() {
        if (starColor.numColors() > 1) {
            cycleStep += starColor.getCycleSpeed();

            if (cycleStep < 255) {
                // interpolate like normal between these two colors
                interpolateColors();
            } else if (cycleStep == 255) {
                // interpolate like normal, but then update interp values to the next set and reset cycleStep
                cycleStarColors();
                currentColor = interpA;
                gamma = interpGammaA;
                cycleStep = 0;
            } else {
                // update interp values to the next set, reset cycleStep then interpolate
                cycleStep -= 255;
                cycleStarColors();
                interpolateColors();
            }
        }
    }

    private void interpolateColors() {
        float position = cycleStep / 255.0f;
        currentColor = Color.interpolate(interpA, interpB, position);
        gamma = interpGammaA + (interpGammaB - interpGammaA) * position;
    }

    private void cycleStarColors() {
        interpA = interpB;
        interpGammaA = interpGammaB;

        interpIndex++;
        if (interpIndex >= starColor.numColors()) {
            interpIndex = 0;
        }
        StarColorSetting nextColor = starColor.getColor(interpIndex);

        interpB = Color.rgb(nextColor.getColorR(), nextColor.getColorG(), nextColor.getColorB());
        interpGammaB = nextColor.getGamma();
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat(ROTATION_SPEED_NBT_TAG, rotationSpeed);
        compound.setFloat(SIZE_NBT_TAG, radius);
        compound.setFloat(ROT_ANGLE_NBT_TAG, rotAngle);
        compound.setFloat(ROT_AXIS_X_NBT_TAG, rotAxisX);
        compound.setFloat(ROT_AXIS_Y_NBT_TAG, rotAxisY);
        compound.setFloat(ROT_AXIS_Z_NBT_TAG, rotAxisZ);
        compound.setTag(STAR_COLOR_TAG, starColor.serializeToNBT());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        rotationSpeed = compound.getFloat(ROTATION_SPEED_NBT_TAG);
        radius = compound.getFloat(SIZE_NBT_TAG);

        rotAngle = compound.getFloat(ROT_ANGLE_NBT_TAG);
        rotAxisX = compound.getFloat(ROT_AXIS_X_NBT_TAG);
        rotAxisY = compound.getFloat(ROT_AXIS_Y_NBT_TAG);
        rotAxisZ = compound.getFloat(ROT_AXIS_Z_NBT_TAG);

        if (compound.hasKey(STAR_COLOR_TAG)) {
            setColor(ForgeOfGodsStarColor.deserialize(compound.getCompoundTag(STAR_COLOR_TAG)));
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    public void updateToClient() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
