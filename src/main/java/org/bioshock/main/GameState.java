package org.bioshock.main;

import org.bioshock.engine.ai.FixedPointVector;
import org.bioshock.engine.ai.Player;
import org.bioshock.networking.Messages;
import org.decimal4j.immutable.Decimal5f;

import java.util.Arrays;
import java.util.List;

public final class GameState {
    public final Player[] players;

    //other data tAo be synchronized

    public GameState(Messages.InQueue d) {
        this.players = new Player[d.N];
        for(int i = 0; i < d.N; i++){
            players[i] = new Player((new FixedPointVector(i * 100, i * 100)));
        }
    }
    public GameState(Messages.ServerInputState d, GameState oldGame) {
        this.players = new Player[d.inputs.length];
        for(int i = 0; i < d.inputs.length; i++){
            FixedPointVector direction = new FixedPointVector((d.inputs[i].dirX), d.inputs[i].dirY);
            FixedPointVector velocity = direction.scaled(Decimal5f.valueOf(200).multiply(d.delta));
            players[i] = new Player((oldGame.players[i].position).add(velocity));
        }
    }

    @Override
    public String toString() {
        return "GameState{" +
                "players=" + Arrays.toString(players) +
                '}';
    }
}
