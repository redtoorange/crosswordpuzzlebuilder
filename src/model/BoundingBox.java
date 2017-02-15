package model;

/**
 * BoundingBox.java - Description
 *
 * @author Andrew McGuiness
 * @version 15/Feb/2017
 */
public class BoundingBox {
	int minX, maxX, minY, maxY;

	public BoundingBox(){
		minX = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		minY = Integer.MAX_VALUE;
		maxY = Integer.MIN_VALUE;
	}



	public int getMinX( ) {
		return minX;
	}

	public void setMinX( int minX ) {
		this.minX = minX;
	}

	public int getMaxX( ) {
		return maxX;
	}

	public void setMaxX( int maxX ) {
		this.maxX = maxX;
	}

	public int getMinY( ) {
		return minY;
	}

	public void setMinY( int minY ) {
		this.minY = minY;
	}

	public int getMaxY( ) {
		return maxY;
	}

	public void setMaxY( int maxY ) {
		this.maxY = maxY;
	}
}
