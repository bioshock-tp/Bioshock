package org.bioshock.rendering.renderers;

import org.bioshock.entities.Entity;

import javafx.scene.canvas.GraphicsContext;

public interface Renderer {
	public static <E extends Entity> void render(
        GraphicsContext gc,
        E entity
    ) {}
}
