package org.bioshock.entities;

import org.bioshock.engine.components.NetworkC;
import org.bioshock.engine.entity.Size;
import org.bioshock.engine.entity.SquareEntity;
import org.bioshock.engine.renderers.SquareRenderer;
import org.bioshock.engine.renderers.components.SquareEntityRendererC;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class TexRectEntity extends SquareEntity {
	public TexRectEntity(Point3D p, NetworkC com, Size s, Color c) {
		super(p, com, new SquareEntityRendererC(), s, 0, c);
		renderer = SquareRenderer.class;
	}

	@Override
	protected void tick(double timeDelta) {
		// TODO Auto-generated method stub
	}
}
