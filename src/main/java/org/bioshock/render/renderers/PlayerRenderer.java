package org.bioshock.render.renderers;

import org.bioshock.engine.entity.GameEntityBase;
import org.bioshock.engine.rendering.IBaseRenderer;
import org.bioshock.engine.sprites.Player;
import org.bioshock.render.components.PlayerRendererC;
import org.bioshock.transform.components.PlayerTransformC;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

public class PlayerRenderer implements IBaseRenderer{

	@Override
	public void render(GraphicsContext gc, GameEntityBase ge) {
		Player entity = (Player) ge;
		PlayerTransformC transform = (PlayerTransformC) entity.transformC;
		PlayerRendererC renderer = (PlayerRendererC) entity.rendererC;
		double x = transform.getPosition().getX();
		double y = transform.getPosition().getY();
		double radius = transform.getRadius();
		double width = transform.width;
		double height = transform.height;
		
		gc.save();
		Rotate r = new Rotate(transform.getRotation(), x + (width/2), y + (height/2));
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		gc.setFill(renderer.getColor());
		gc.fillRect(x, y, width, height);
		gc.setLineWidth(10);
		gc.setStroke(renderer.getColor());
		gc.strokeOval(x-radius + width/2, y-radius + height/2, radius*2, radius*2);
		
		gc.restore();
	}

}
