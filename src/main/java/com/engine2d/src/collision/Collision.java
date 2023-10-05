package com.engine2d.src.collision;

import org.joml.Vector2f;

public class Collision {
	public Vector2f distance;
	public boolean isIntesecting;
	
	public Collision(Vector2f distance, boolean intersects) {
		this.distance = distance;
		this.isIntesecting = intersects;
	}
	
}
