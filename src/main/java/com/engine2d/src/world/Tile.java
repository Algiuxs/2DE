package com.engine2d.src.world;

public class Tile {
	public static Tile tiles[] = new Tile[255];
	public static byte not = 0;

	public static final Tile grass_tile = new Tile("Grass");
	public static final Tile stone_tile = new Tile("Stone").setSolid();
	public static final Tile wood_tile = new Tile("Wood").setSolid();
	public static final Tile test_tile = new Tile("text").setSolid();
	
	private byte id;
	private boolean solid;
	private String texture;
	
	public Tile(String texture) {
		this.id = not;
		not++;
		this.texture = texture;
		this.solid = false;
		if(tiles[id] != null) {
			throw new IllegalStateException("Tiles at:[" + id + "] is already being used");
		}
		
		tiles[id] = this;
	}
	

	public Tile setSolid() {
		this.solid = true;
		return this;
	}
	public boolean isSolid() {
		return solid;
	}


	public byte getId() {
		return id;
	}

	public String getTexture() {
		return texture;
	}
}
