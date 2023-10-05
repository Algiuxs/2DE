package com.engine2d.src.entity;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.engine2d.src.collision.AABB;
import com.engine2d.src.collision.Collision;
import com.engine2d.src.io.Window;
import com.engine2d.src.render.Animation;
import com.engine2d.src.render.Camera;
import com.engine2d.src.render.Model;
import com.engine2d.src.render.Shader;
import com.engine2d.src.world.World;

public abstract class Entity {
	protected static Model model;
	protected AABB bounding_box;
	//private Texture tex;
	protected Animation[] animations;
	private int use_animation;
	protected Transform transform;
	
	public Entity(int max_anim, Transform transform) {
		
		this.animations = new Animation[max_anim]; 
		
		this.transform = transform;
		this.use_animation = 0;
		
		bounding_box = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x+1,transform.scale.y+1));
	}
	
	protected void setAnimation(int index, Animation anim) {
		animations[index] = anim;
	}
	
	public void useAnimation(int index) {
		this.use_animation = index;
	}
	
	public void move(Vector2f direction) {
		
		transform.pos.add(new Vector3f(direction, 0));
		bounding_box.getCenter().set(transform.pos.x, transform.pos.y);
	}
	
	public void collideWithTiles(World world) {
		AABB[] boxes = new AABB[25];
		for(int i = 0 ; i <5; i++) {
			for(int j = 0 ; j<5; j++) {
				boxes[i+j*5] = world.getTileBounds((int)(((transform.pos.x/2)+0.5f)+i - (5/2)), (int)(((-transform.pos.y/2)+0.5f)+j - (5/2)));
			}
		}
		
		AABB box = null;
		for(int i = 0 ; i <boxes.length; i++){
			if(boxes[i] !=null) {
				if(box == null) box = boxes[i];
					
				Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
				Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
				
				if(length1.lengthSquared() > length2.lengthSquared()) {
					box = boxes[i];
				}
			}
		}
		
		if(box !=null) {
			Collision data = bounding_box.getCollision(box);
			
			if(data.isIntesecting) {
				bounding_box.correctPosition(box, data);
				transform.pos.set(bounding_box.getCenter(), 0);
			}
			for(int i = 0 ; i <boxes.length; i++){
				if(boxes[i] !=null) {
					if(box == null) box = boxes[i];
						
					Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
					Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
					
					if(length1.lengthSquared() > length2.lengthSquared()) {
						box = boxes[i];
					}
				}
			}
			
			
				data = bounding_box.getCollision(box);
				
				if(data.isIntesecting) {
					bounding_box.correctPosition(box, data);
					transform.pos.set(bounding_box.getCenter(), 0);
				
			}
		}
	}
	
	
	public abstract void update(float delta, Window window, Camera cam, World world);
	
	public void render(Shader shader, Camera camera, World world) {
		Matrix4f target = camera.getProjection();//TODO Size
		target.mul(world.getWorld());
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", transform.getprojection(target));
		animations[use_animation].bind(0);
		model.render();
	}
	public static void initAsset() {
		float [] vertices = new float[] {
				-1f, 1f, 0,
				1f, 1f, 0,
				1f, -1f, 0,
				-1f, -1f, 0,
		};
		
		float [] texture = new float[] {
			0,0,
			1,0,
			1,1,
			0,1,

			
		};
		
		int [] indices = new int[] {
			0,1,2,
			2,3,0
		};
		
		model = new Model(vertices, texture, indices);
	}
	
	public static void deleteAsset() {
		model = null;
	}

	public void collideWithEntities(Entity entity) {
		Collision collide = bounding_box.getCollision(entity.bounding_box);
		
		if(collide.isIntesecting) {
			collide.distance.x /=2;
			collide.distance.y /=2;
			
			bounding_box.correctPosition(entity.bounding_box, collide);
			transform.pos.set(bounding_box.getCenter().x, bounding_box.getCenter().y, 0);
			
			entity.bounding_box.correctPosition(bounding_box, collide);
			entity.transform.pos.set(entity.bounding_box.getCenter().x, entity.bounding_box.getCenter().y, 0);
		}
		
	}
}
