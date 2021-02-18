package org.bioshock.engine.rendering;

import org.bioshock.engine.entity.GameEntityBase;

import javafx.scene.canvas.GraphicsContext;

public interface IBaseRenderer {
	public void render(GraphicsContext gc, GameEntityBase ge);
}
