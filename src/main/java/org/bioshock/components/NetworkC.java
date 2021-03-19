package org.bioshock.components;


/**
 * An object that stores whether an {@code Entity} is to be managed by the
 * {@link #org.bioshock.engine.networking.NetworkManager NetworkManager}
 */
public class NetworkC {
    /**
     * True if this {@code Entity} is networked
     * @see org.bioshock.entities.Entity
     */
    private boolean networked;


    /**
     * @param networked the value to initialise {@link #networked} to
     */
    public NetworkC(boolean networked) {
        this.networked = networked;
    }


    /**
     * @return true if {@code Entity} is managed by the
     * {@link #org.bioshock.engine.networking.NetworkManager NetworkManager}
     */
    public boolean isNetworked() {
		return networked;
	}


    /**
     * @param networked the new value for the {@link #networked} field
     */
    public void setNetworked(boolean networked) {
		this.networked = networked;
	}
}
