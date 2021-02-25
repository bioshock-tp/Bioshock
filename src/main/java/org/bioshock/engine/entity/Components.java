package org.bioshock.engine.entity;

public class Components {
    private boolean rendered = false;
    private boolean networked = false;

    public Components(boolean rendered, boolean networked) {
        this.rendered = rendered;
        this.networked = networked;
    }

	public boolean isRendered() {
		return rendered;
	}

	public boolean isNetworked() {
		return networked;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public void setNetworked(boolean networked) {
		this.networked = networked;
	}
}
