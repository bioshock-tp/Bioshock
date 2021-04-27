package org.bioshock.audio.settings;

public class EffectSettings extends AudioSettings {

    @Override
    public EffectSettings deepCopy()  {
        final EffectSettings settings = new EffectSettings();
        settings.setVolume(super.volume);
        settings.setCycleCount(super.cycleCount);
        return settings;
    }

    @Override
    public double getVolume() {
        return super.volume;
    }

}