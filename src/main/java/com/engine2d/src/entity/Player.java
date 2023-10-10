package com.engine2d.src.entity;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.engine2d.src.io.Window;
import com.engine2d.src.render.Animation;
import com.engine2d.src.render.Camera;
import com.engine2d.src.world.World;

public class Player extends Entity{
	public static final int anim_idle = 0;
	public static final int anim_walk = 1;
	public static final int anim_size = 2;
	
	float Player_speed = 10;

	public Player(Transform transform) {
		super(anim_size, transform);
		
		setAnimation(anim_idle, new Animation(11, 5, "Player/Idle/Idle"));
		setAnimation(anim_walk, new Animation(4, 5, "Player/Walk/Player"));
	}

	@Override
	public void update(float delta, Window window, Camera camera, World world) {
		Vector2f movement = new Vector2f();
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			movement.add(-Player_speed * delta, 0);
		}
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			movement.add(Player_speed * delta, 0);
		}
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			movement.add(0, Player_speed * delta);
		}
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			movement.add(0, -Player_speed * delta);
		}
		
		move(movement);
		
		if(movement.x != 0 || movement.y !=0) {
			useAnimation(anim_walk);
		}else {
			useAnimation(anim_idle);
		}
		
		camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.08f);
		//cam.setPos(transform.pos.mul(-world.getScale(), new Vector3f()));
	}
	
	
}
