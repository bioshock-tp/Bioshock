package org.bioshock.engine.renderers;

import org.bioshock.engine.entity.Entity;

import javafx.scene.canvas.GraphicsContext;

public interface Renderer {
	public void render(GraphicsContext gc, Entity entity);
}
