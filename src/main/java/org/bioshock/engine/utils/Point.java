package org.bioshock.engine.utils;

public class Point {
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;		
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Point subtract(Point sub ) {
		return new Point(x - sub.x, y - sub.y);
	}
}
