package org.bioshock.rendering.renderers;

import org.bioshock.entities.Entity;

import javafx.scene.canvas.GraphicsContext;

public interface Renderer {
    /**
     * Method to render the given entity
     * @param <E>
     * @param gc The graphics context to render on
     * @param entity The entity to render
     */
	public static <E extends Entity> void render(
        GraphicsContext gc,
        E entity
    ) {}
}
