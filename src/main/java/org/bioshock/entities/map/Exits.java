package org.bioshock.entities.map;

/***
 * A class which defines which exits a room has
 *
 */
public class Exits {
	boolean top = false;
	public void setTop(boolean top) {
        this.top = top;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    boolean bot = false;
	boolean left = false;
	boolean right = false;

	/***
	 * Creates a new Exits object where true represents having an exit on that side
	 * @param newTop
	 * @param newBot
	 * @param newLeft
	 * @param newRight
	 */
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
