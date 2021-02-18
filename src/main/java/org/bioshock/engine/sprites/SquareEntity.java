package org.bioshock.engine.sprites;

import org.bioshock.engine.entity.GameEntityBase;
import org.bioshock.render.components.SquareEntityRendererC;
import org.bioshock.transform.components.SquareEntityTransformC;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class SquareEntity extends GameEntityBase {
	SquareEntityTransformC transform;
	SquareEntityRendererC renderer;
	
    protected SquareEntity(SquareEntityTransformC transform, SquareEntityRendererC renderer, 
    		int x, int y, int w, int h, Color c, double z) {
    	this.transformC = (this.transform = transform);
        transform.width = w;
        transform.height = h;
        
        this.rendererC = (this.renderer = renderer);
        renderer.setColor(c);
        renderer.setZ(z);
    }

    protected void setXYRectangle(double x, double y) {
        this.transformC.setPosition(new Point2D(x, y));
    }

	public double getX() {
		return transform.getPosition().getX();
	}

	public double getY() {
		return transform.getPosition().getY();
	}
	
	public double getWidth() {
		return transform.width;
	}
	
	public double getHeight() {
		return transform.height;
	}
}
