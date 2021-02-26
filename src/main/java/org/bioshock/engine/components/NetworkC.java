package org.bioshock.engine.components;

public class NetworkC {
    private boolean networked;

    public NetworkC( boolean networked) {
        this.networked = networked;
    }


	public boolean isNetworked() {
		return networked;
	}
	
	public void setNetworked(boolean networked) {
		this.networked = networked;
	}
}
