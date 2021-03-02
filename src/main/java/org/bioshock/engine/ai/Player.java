package org.bioshock.engine.ai;

import javafx.scene.Parent;
import org.decimal4j.immutable.Decimal5f;

public class Player extends Parent {
    public final FixedPointVector position;

    @Override
    public String toString() {
        return "Player{" +
                "position=" + position +
                '}';
    }

    public Player(FixedPointVector position) {
        this.position = position;
    }
}
