package org.bioshock.entities.map;

public class Exits {
	boolean top = false;
	boolean bot = false;
	boolean left = false;
	boolean right = false;
	
	public Exits(boolean newTop, boolean newBot, boolean newLeft, boolean newRight) {
		top = newTop;
		bot = newBot;
		left = newLeft;
		right = newRight;
	}	

	public boolean isTop() {
		return top;
	}

	public boolean isBot() {
		return bot;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}
}
