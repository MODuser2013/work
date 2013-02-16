package com.brandonestock.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.brandonestock.main.Start;
import com.brandonestock.renderers.BlockRenderer;
import com.brandonestock.types.Block;

public class Chunk {
	static public int chunksize = 16;
	static public float chunkRes = 256f; 
	
	int xL, yL, zL; //chunks coordinates (in 16 increments to start)
	boolean stayloaded = false;
	Block data[][][] = new Block[chunksize][chunksize][chunksize]; //stores blocks 
	
	//perlin noise level size = 128 so  
	public Chunk(int x, int y, int z, int id)
	{
		xL = x;
		yL = y;
		zL = z;
		//Vector2f test = generate(id);
		//System.out.println("min=" + test.x + " max=" + test.y);
	}

	public void render()
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((xL - Start.chunkXoff) *chunksize, ((yL - Start.chunkYoff)) * chunksize,(zL - Start.chunkZoff) * chunksize);
		
		int rendered = 0;
		for(int y = 0; y < chunksize; y++)
		{
			for(int x = 0; x < chunksize; x++)
			{
				for(int z = 0; z < chunksize; z++)
				{
					if(data[x][y][z] != null)
					{
						
					if(isVisible(x,y,z)){BlockRenderer.render(data[x][y][z]);}
					
					}
					
				}
			
			}
			
		}
		GL11.glPopMatrix();
	}
	
	public boolean isVisible(int x, int y, int z)
	{
		
		
		if(x == 0 || x == 15) {return true;} //outsides
		if(y == 15) {return true;} //outsides
		if(y == 0){return false;}
		if(z == 0 || z == 15) {return true;}
		
		//checks for ANY air around it
		if(data[x-1][y][z] == null) {return true;}if(data[x+1][y][z] == null) {return true;}
		if(data[x][y-1][z] == null) {return true;}if(data[x][y+1][z] == null) {return true;}
		if(data[x][y][z-1] == null) {return true;}if(data[x][y][z+1] == null) {return true;}
		
		
		return false;
	}
	
	
	public Vector2f generate(int id)
	{
		//test size of 16 for chunk size
		Vector2f test = new Vector2f();
		
		for(int x = 0; x < chunksize; x++)
		{
			for(int y = 0; y < chunksize; y++)
			{
				for(int z = 0; z < chunksize; z++)
				{
					int mid = id; //0; //all grass for now
					float noiseX = (NoiseMap.seedval + (x + (xL * chunksize)) / chunkRes) ;
					float noiseY = (NoiseMap.seedval + (z + (zL* chunksize)) / chunkRes);
					int testH = (int) (Math.abs(NoiseMap.noise(noiseX, noiseY)) * (chunksize*2)/*2 chunks tall for hills*/);
					if((y - ((Start.chunkYoff - yL) * 16)) <= testH && (yL * Start.chunkYoff) <= 160 )
					{ 
					data[x][y][z] = new Block(0,mid,x,y,z);
					//System.out.println("test " + testH);
					}
					else
					{
					//	data[x][y][z] = new Block(0,2,x,y,z);
					
					}
					if(y+ (yL * chunksize)< Start.min){Start.min = y+ (yL * chunksize);}if(y+ (yL * chunksize)>Start.max){Start.max = y+ (yL * chunksize);}
				}
			}
		}
		//Start.min = Start.min + (yL * chunksize);
		//Start.max = Start.max + (yL * chunksize);
		return test;
	}


	

}
