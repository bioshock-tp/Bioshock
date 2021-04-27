package org.bioshock.audio.settings;

public class MusicSettings extends AudioSettings {
    public MusicSettings() {
    }

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

    /**
     * Converts settings to a string
     * @return String value of settings
     */
    public String toString() {
        return "MusicSettings()";
    }

    /**
     *
     * @param o Object to compare
     * @return Equality boolean
     */
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MusicSettings)) return false;
        final MusicSettings other = (MusicSettings) o;
        if (!other.canEqual(this)) return false;
        return super.equals(o);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MusicSettings;
    }

    /**
     * Hashes the value of the result
     * @return Hashcode value of the result
     */
    public int hashCode() {
        return super.hashCode();
    }
}
