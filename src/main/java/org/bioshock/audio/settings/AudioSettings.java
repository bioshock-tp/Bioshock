package org.bioshock.audio.settings;

import lombok.Getter;

public abstract class AudioSettings {

    /**
     * The number of times the clip will be played.
     *
     * A cycleCount of 1 plays exactly once, a cycleCount of 2 plays twice and so on. Valid range is 1 or
     * more, but setting this to -1 will cause the clip to continue looping.
     *
     */
    @Getter protected int cycleCount = 1;

    /**
     * The relative rate at which the clip is played.
     *
     * Valid range is 0.125 (1/8 speed) to 8.0 (8x speed). Normal playback for a clip is 1.0; any other rate
     * will affect pitch and duration accordingly.
     *
     */
    @Getter protected double rate = 1.0;

    /**
     * The relative volume level at which the clip is played.
     *
     * Valid range is 0.0 (muted) to 1.0 (full volume).
     *
     * Volume is controlled by attenuation, so values below 1.0 will reduce the sound level accordingly.
     *
     */
    @Getter protected double volume = 1.0;

    /**
     * Creates a deep copy of this object.
     *
     * @return
     *          A deep copy of this object.
     */
    public abstract AudioSettings deepCopy();

    /**
     * Sets the cycle count to a value of the range [-1, Integer.MAX_VALUE].
     *
     * A value of -1 will cause the audio to repeat indefinitely.
     *
     * @param cycleCount
     *          The new cycle count.
     */
    public void setCycleCount(final int cycleCount) {
        this.cycleCount = Math.max(-1, cycleCount);
    }

    /**
     * Sets the rate to a value of the range [0.125, 8].
     *
     * @param rate
     *          The new rate.
     */
    public void setRate(final double rate) {
        if (rate < 0.125) {
            this.rate = 0.125;
        } else if (rate > 8) {
            this.rate = 8;
        } else {
            this.rate = rate;
        }
    }
 
    public double getVolume() {
        return volume;
    }

    /**
     * Sets the volume to a value of the range [0, 1].
     *
     * @param volume
     *          The new volume.
     */
    public void setVolume(final double volume) {
        if (volume < 0) {
            this.volume = 0;
        } else if (volume > 1) {
            this.volume = 1;
        } else {
            this.volume = volume;
        }
    }

}
