package org.bioshock.engine.renderers;

import org.bioshock.engine.entity.SpriteEntity;
import org.bioshock.engine.entity.SquareEntity;

import javafx.scene.canvas.GraphicsContext;

public class PlayerSpriteRenderer implements Renderer {
    public PlayerSpriteRenderer() {}

    public static <E extends SquareEntity> void render(
            GraphicsContext gc,
            E player
    ) {
        PlayerSpriteRendererHelper.render(gc, player);
    }
}
