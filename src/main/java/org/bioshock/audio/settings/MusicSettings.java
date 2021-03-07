package org.bioshock.audio.settings;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MusicSettings extends AudioSettings {
    @Override
    public MusicSettings deepCopy() {
        final MusicSettings settings = new MusicSettings();

        settings.setCycleCount(super.cycleCount);

        settings.setVolume(super.volume);
        return settings;
    }

    @Override
    public double getVolume() {
        return super.volume;
    }
}
