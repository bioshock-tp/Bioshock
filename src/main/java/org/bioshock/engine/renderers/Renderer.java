package org.bioshock.engine.renderers;

import org.bioshock.engine.entity.Entity;

import javafx.scene.canvas.GraphicsContext;

public interface Renderer {
	public static <E extends Entity> void render(
        GraphicsContext gc,
        E entity
    ) {}
}
