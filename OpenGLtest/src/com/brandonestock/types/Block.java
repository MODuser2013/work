package com.brandonestock.types;

import com.brandonestock.renderers.BlockRenderer;
import com.brandonestock.renderers.Renderer;


public class Block {
	//BLOCK SUPERCLASS
	public int blockId;
	public int texIndex;
	public Renderer ren;
	public float x,y,z;
	
	public Block(int blockID, int textureIndex, float x1, float y1, float z1) 
	{
		x=x1;y=y1;z=z1;
		blockId = blockID;
		texIndex = textureIndex;
	}

}
