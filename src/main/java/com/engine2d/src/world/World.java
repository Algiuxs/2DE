package com.engine2d.src.world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.engine2d.src.collision.AABB;
import com.engine2d.src.entity.Entity;
import com.engine2d.src.entity.Player;
import com.engine2d.src.entity.Transform;
import com.engine2d.src.io.Window;
import com.engine2d.src.render.Camera;
import com.engine2d.src.render.Shader;

public class World {
	private final int view = 25;
	private byte[] tiles;
	private AABB[] bounding_boxes;
	private List<Entity> entities;
	private int width;
	private int height;
	private int scale;

	private Matrix4f world;

	public World(String world) {
		try {
			BufferedImage tile_sheet = ImageIO.read(new File("./src/main/java/com/engine2d/res/levels/" + world + "_world.png"));
			// BufferedImage entity_sheet = ImageIO.read(new File("./res/levels/" + world + "entities.png"));
			
			width = tile_sheet.getWidth();
			height = tile_sheet.getHeight();
			scale = 16;

			this.world = new Matrix4f().setTranslation(new Vector3f(0));
			this.world.scale(scale);
			int[] colorTileSheet = tile_sheet.getRGB(0, 0, width, height, null, 0, width);

			tiles = new byte[width * height];
			bounding_boxes = new AABB[width * height];

			entities = new ArrayList<Entity>();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;

					Tile tile;
					try {
						tile = Tile.tiles[red];
					} catch (ArrayIndexOutOfBoundsException e) {
						tile = null;
					}

					if (tile != null) {
						setTile(tile, x, y);
					}
				}
			}

			// TODO
			entities.add(new Player(new Transform()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public World() {
		width = 64;
		height = 64;
		scale = 16;

		tiles = new byte[width * height];
		bounding_boxes = new AABB[width * height];

		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(scale);
	}

	public Matrix4f getWorld() {
		return world;
	}

	public void render(TileRenderer render, Shader shader, Camera cam, Window window) {
		int pos_x = ((int) cam.getPosition().x + (window.getWidth() / 2)) / (scale * 2);
		int pos_y = ((int) cam.getPosition().y - (window.getHeight() / 2)) / (scale * 2);

		for (int i = 0; i < view; i++) {
			for (int j = 0; j < view; j++) {
				Tile tile = getTile(i - pos_x, j + pos_y);
				if (tile != null) {
					render.renderTile(tile, i - pos_x, -j - pos_y, shader, world, cam);
				}
			}
		}

		for (Entity entity : entities) {
			entity.render(shader, cam, this);
		}

	}

	public void Update(float delta, Window window, Camera camera) {
		for (Entity entity : entities) {
			entity.update(delta, window, camera, this);
		}
		
		for(int i = 0; i < entities.size(); i ++) {
			entities.get(i).collideWithTiles(this);
			for(int j = i+1; j < entities.size(); j++) {
				entities.get(i).collideWithEntities(entities.get(j));
			}
			entities.get(i).collideWithTiles(this);
		}
	}

	public void correctCamera(Camera camera, Window window) {
		Vector3f pos = camera.getPosition();

		int w = -width * scale * 2;
		int h = height * scale * 2;

		if (pos.x > -(window.getWidth() / 2) + scale) {
			pos.x = -(window.getWidth() / 2) + scale;
		}

		if (pos.x < w + (window.getWidth() / 2) + scale) {
			pos.x = w + (window.getWidth() / 2) + scale;
		}

		if (pos.y < (window.getHeight() / 2) - scale) {
			pos.y = (window.getHeight() / 2) - scale;
		}

		if (pos.y > h - (window.getHeight() / 2) - scale) {
			pos.y = h - (window.getHeight() / 2) - scale;
		}

	}

	public void setTile(Tile tile, int x, int y) {
		tiles[x + y * width] = tile.getId();
		if (tile.isSolid()) {
			bounding_boxes[x + y * width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
		} else {
			bounding_boxes[x + y * width] = null;
		}
	}

	public Tile getTile(int x, int y) {
		try {
			return Tile.tiles[tiles[x + y * width]];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public AABB getTileBounds(int x, int y) {
		try {
			return bounding_boxes[x + y * width];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public int getScale() {
		return scale;
	}
}
