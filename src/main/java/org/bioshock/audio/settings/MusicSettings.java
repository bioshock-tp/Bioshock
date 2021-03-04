package org.bioshock.audio.settings;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MusicSettings extends AudioSettings {
    @Override
    public MusicSettings deepCopy() {
        final var settings = new MusicSettings();
//        settings.setBalance(super.balance);
//        settings.setCycleCount(super.cycleCount);
//        settings.setRate(super.rate);
        settings.setVolume(super.volume);
        return settings;
    }

    @Override
    public double getVolume() {
        return super.volume;
    }


}
