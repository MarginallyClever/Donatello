package com.marginallyclever.donatello;

/**
 * Represents a cartesian coordinate in 2D, with double precision.
 */
public class Point2D {
	/**
	 * the cartesian coordinates.
	 */
	public double x,y;

	/**
	 * Constructor for subclasses to call.
	 */
	public Point2D() {}

	/**
	 * Constructor sets this to some {@link Point2D}
	 * @param b the {@link Point2D} to copy.
	 */
	public Point2D(Point2D b) {
		this(b.x,b.y);
	}

	/**
	 * Sets this to some cartesian (x0,y0) value.
	 * @param x0 the new x value.
	 * @param y0 the new y value.
	 */
	public Point2D(double x0, double y0) {
		super();
		x=x0;
		y=y0;
	}

	/**
	 * Sets this to some cartesian (x0,y0) value.
	 * @param x0 the new x value.
	 * @param y0 the new y value.
	 */
	public void set(double x0,double y0) {
		x=x0;
		y=y0;
	}

	/**
	 * Sets this to some other {@link Point2D}.
	 * @param p the other {@link Point2D}.
	 */
	public void set(Point2D p) {
		x=p.x;
		y=p.y;
	}

	/**
	 * Returns the square of the distance to the origin.
	 * @return the square of the distance to the origin.
	 */
	public double lengthSquared() {
		return x*x+y*y;
	}

	/**
	 * Returns the distance to the origin.
	 * @return the distance to the origin.
	 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}

	/**
	 * Scales this point about the origin.
	 * @param scale the amount to scale
	 */
	public void scale(double scale) {
		x*=scale;
		y*=scale;
	}

	/**
	 * Returns the square of the distance to some other {@link Point2D}.
	 * @param p the other {@link Point2D}.
	 * @return the square of the distance to some other {@link Point2D}.
	 */
	public double distanceSquared(Point2D p) {
		double dx = p.x-x;
		double dy = p.y-y;
		return dx*dx + dy*dy;
	}

	/**
	 * Returns the distance to some other {@link Point2D}.
	 * @param p the other {@link Point2D}.
	 * @return the distance to some other {@link Point2D}.
	 */
	public double distance(Point2D p) {
		return Math.sqrt(distanceSquared(p));
	}
}
