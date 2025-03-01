package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class PositionedSoundRecord extends PositionedSound
{
    public static PositionedSoundRecord create(ResourceLocation soundResource, float pitch)
    {
        return new PositionedSoundRecord(soundResource, 0.25F, pitch, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F, false);
    }

    public static PositionedSoundRecord create(ResourceLocation soundResource)
    {
        return new PositionedSoundRecord(soundResource, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F, false);
    }

    public static PositionedSoundRecord create(ResourceLocation soundResource, boolean isMusic)
    {
        return new PositionedSoundRecord(soundResource, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F, isMusic);
    }

    public static PositionedSoundRecord create(ResourceLocation soundResource, float xPosition, float yPosition, float zPosition)
    {
        return new PositionedSoundRecord(soundResource, 4.0F, 1.0F, false, 0, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition, false);
    }

    public PositionedSoundRecord(ResourceLocation soundResource, float volume, float pitch, float xPosition, float yPosition, float zPosition)
    {
        this(soundResource, volume, pitch, false, 0, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition, false);
    }

    private PositionedSoundRecord(ResourceLocation soundResource, float volume, float pitch, boolean repeat, int repeatDelay, ISound.AttenuationType attenuationType, float xPosition, float yPosition, float zPosition, boolean isMusic)
    {
        super(soundResource);
        this.volume = volume;
        this.pitch = pitch;
        this.xPosF = xPosition;
        this.yPosF = yPosition;
        this.zPosF = zPosition;
        this.repeat = repeat;
        this.repeatDelay = repeatDelay;
        this.attenuationType = attenuationType;
        this.isMusic = isMusic;
    }

}
