package org.bioshock.audio.settings;

import lombok.Getter;

public class EffectSettings extends AudioSettings {
    /**
     * The relative "centre" of the clip.import org.bioshock.audio.settings.AudioSettings;
     *
     * A pan value of 0.0 plays the clip normally where a -1.0 pan shifts the clip entirely to the left channel
     * and 1.0 shifts entirely to the right channel.
     *
     * Unlike balance this setting mixes both channels so neither channel loses data. Setting pan on a mono
     * clip has the same effect as setting balance, but with a much higher cost in CPU overhead so this i
     * not recommended for mono clips.
     *
     */
    @Getter private double pan = 0.0;

    @Override
    public EffectSettings deepCopy()  {
        final EffectSettings settings = new EffectSettings();
        settings.setVolume(super.volume);
        settings.setCycleCount(super.cycleCount);
        return settings;
    }

    /**
     * Sets the pan to a value of the range [-1, 1].
     *
     * @param pan
     *          The new pan.
     */
    public void setPan(final double pan) {
        if (pan < -1) {
            this.pan = -1;
        } else if (pan > 1) {
            this.pan = 1;
        } else {
            this.pan = pan;
        }
    }

    @Override
    public double getVolume() {
        return super.volume;
    }

//    @Override
//    public int getCycleCount() { return  super.cycleCount; }
}