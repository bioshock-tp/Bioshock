package org.bioshock.engine.networking;

import java.util.HashMap;
import java.util.Map;

import org.bioshock.engine.ai.Seeker;
import org.bioshock.engine.entity.EntityManager;
import org.bioshock.engine.entity.Hider;

public final class GameState {
    private final Map<String, Hider> players = new HashMap<>();
    private final Seeker seeker;

    //other data tAo be synchronized

    public GameState() {
        EntityManager.getPlayers().forEach(player ->
            players.put(player.getID(), player)
        );

        seeker = EntityManager.getSeeker();
    }

    // public GameState(ClientInput[] inputs, double delta) {
    //     players.entrySet().forEach(entry -> {
    //         String id = entry.getKey();
    //         Hider player = entry.getValue();

    //         Point2D direction = new Point2D(inputs[i].dirX, inputs[i].dirY);
    //         Point2D velocity = direction.scaled(Decimal5f.valueOf(200).multiply(delta));
    //         player.setPosition(player.getPosition().add(velocity));
    //     });

    //     Point2D direction = new Point2D(inputs[i].dirX, inputs[i].dirY);
    //     Point2D velocity = direction.scaled(200 * delta);
    //     seeker.setPosition(seeker.getPosition().add(velocity));
    // }

    @Override
    public String toString() {
        return "GameState{" +
                "players=" + players.values() +
                '}';
    }
}
