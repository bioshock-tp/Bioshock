package org.bioshock.engine.renderers;

import java.security.InvalidParameterException;

import org.bioshock.engine.entity.Entity;
import org.bioshock.engine.entity.SquareEntity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public class SquareRenderer implements Renderer{
	 public void render(GraphicsContext gc, Entity entity) {
	        if (!(entity instanceof SquareEntity)) {
	            throw new InvalidParameterException();
	        }

	        SquareEntity rect = (SquareEntity) entity;

	        int x = rect.getX();
	        int y = rect.getY();
	        double width = rect.getWidth();
	        double height = rect.getHeight();

	        gc.save();

	        Rotate r = rect.getRotation();
	        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	        gc.setFill(rect.getRendererC().getColor());
	        gc.fillRect(x, y, width, height);
	       
	        gc.restore();
	    }
}
