package com.brandonestock.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.brandonestock.renderers.BlockRenderer;
import com.brandonestock.types.Block;
import com.brandonestock.util.Chunk;
import com.brandonestock.util.FPScamera;
import com.brandonestock.util.NoiseMap;

public class Start {

	static public FPScamera player = new FPScamera(0,-1.3f,0);
	static int quality = 3; //chunks loaded (this would load 27 chunks
	//holds all chunks loaded around player (should be odd number)
	static Chunk[][][] chunks = new Chunk[quality][quality][quality];
	
	static List<Chunk> loadedChunks = new ArrayList<Chunk>(); //contains all loaded chunks
	
	static int amount = 16; //16x16x16 chunk size
	
	static float cRx, cRy, cRz = 0.0f; //camera rotation

	static public int max = 0;static public int min = 0;
	
	static public int chunkXoff = 0;static public int chunkdistX = 0;
	static public int chunkYoff = 0;static public int chunkdistY = 2;
	static public int chunkZoff = 0;static public int chunkdistZ = 0;
	
	
	
	static float mouseSen = 0.3f;
	static float moveSen = 0.5f; //1.5f
	
	public static void main(String[] args) throws LWJGLException 
	{
		Display.setTitle("InfiniCraft");
		Display.setResizable(false);
		Display.setDisplayMode(new DisplayMode(900,500));
		Display.create();
		
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_DEPTH_TEST);  // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL);// The Type Of Depth Test To Do
		//GL11.glEnable(GL11.GL_CULL_FACE);/*culls the faces of the cubes*/GL11.glCullFace(GL11.GL_BACK);
		
		FloatBuffer lightPos, difuseLight, ambientLight;
		lightPos = BufferUtils.createFloatBuffer(4);
		lightPos.put(0).put(200).put(0).put(0).flip();
		difuseLight = BufferUtils.createFloatBuffer(4);
		difuseLight.put(10.0f).put(10.0f).put(10.0f).put(1.0f).flip();
		ambientLight = BufferUtils.createFloatBuffer(4);
		ambientLight.put(1.75f).put(1.75f).put(1.75f).put(0).flip();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPos);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_LIGHT_MODEL_AMBIENT, ambientLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, difuseLight);
		
		//load texture
		Texture materials;
		try {
			materials = TextureLoader.getTexture("PNG", new FileInputStream(new File("Z:/work/OpenGLtest/src/com/brandonestock/types/terrain.png")));
		     
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, materials.getTextureID());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		GLU.gluPerspective(45f, 900f / 500f, 1, 1000);
		
		//Pyramid();
		worldGen();
		
		//Chunk testChunk = new Chunk(0,0,0);
		Mouse.setGrabbed(true); //grabs mouse
		while(!Display.isCloseRequested())
		{
			Display.sync(60); //sets FPS
			
			Controls();
			Render();
			//run world tick
			Display.update();
		}
		
		Display.destroy();
	}

	public static void Controls()
	{
		float speed = moveSen;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){player.strafeLeft(speed);}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){player.strafeRight(speed);}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){player.walkBackwards(speed);}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){player.walkForward(speed);}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){player.flyUp(speed);}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){player.flyDown(speed);}
		
		//WORKS!
		//if(player.position.x > 0){/*shift chunk over 1 and teleport to 0*/player.position.x =-16; chunkXoff--; update();} if(player.position.x < -16){/*shift chunk over 1 and teleport to 0*/player.position.x = 0; chunkXoff++; update();}
		
		//if(player.position.z > 0){/*shift chunk over 1 and teleport to 0*/player.position.z =-16; chunkZoff--; update();} if(player.position.z < -16){/*shift chunk over 1 and teleport to 0*/player.position.z = 0; chunkZoff++; update();}
		
		if(player.position.y > (-16)){/*shift chunk over 1 and teleport to 0*/player.position.y =-32f/*0*/; chunkYoff++; update();}
		if(player.position.y < (-32)){/*shift chunk over 1 and teleport to 0*/player.position.y = -16f/*0*/; chunkYoff--; update();}
				
		
		if(Keyboard.isKeyDown(Keyboard.KEY_I)){chunkYoff++;update();}
		if(Keyboard.isKeyDown(Keyboard.KEY_K)){chunkYoff--;update();}
		
		
		Display.setTitle(player.position.toString() + "centerChunk(" + chunkXoff + ", " + chunkYoff + ", " + chunkZoff + ")");
		
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_R) && Keyboard.isRepeatEvent() == false){worldGen();}

		//updates mouse
		int dx = Mouse.getDX();
		int dy = Mouse.getDY();
		player.pitch(-1 * (dy * mouseSen));
		player.yaw(dx * mouseSen);
		//System.out.println("x=" + dx + " y=" + dy);
		speed = mouseSen;
		if(Keyboard.isKeyDown(Keyboard.KEY_I)){}
		if(Keyboard.isKeyDown(Keyboard.KEY_K)){}
		if(Keyboard.isKeyDown(Keyboard.KEY_J)){player.yaw(-speed);}
		if(Keyboard.isKeyDown(Keyboard.KEY_L)){/*lighting*/}
		if(Keyboard.isKeyDown(Keyboard.KEY_U)){}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_M)){}
		//Display.setTitle("loc (" + cX + ", " + cY + ", " + cZ + ") rot (" + cRx + ", " + cRy + ", " + cRz + ")");
	}
	

	public static void Render()
	{
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);     // Clear The Screen And The Depth Buffer
		GL11.glLoadIdentity(); //resets location
		
				
		player.lookThrough();
		GL11.glPushMatrix();
		
		for(int c = 0; c < loadedChunks.size(); c++)
		{
			loadedChunks.get(c).render();
		}
		/*for(int b = 0; b < test.size(); b++)
		{
			BlockRenderer.render(test.get(b));
		}*/
		GL11.glPopMatrix();
	}
	
	public static void worldGen()
	{
		NoiseMap.seedval = (float) (Math.random() * 256);
		loadedChunks.clear();
		int minX = -chunkdistX; //horizontal chunks to load
		int maxX = chunkdistX;
		int minY = -chunkdistY;
		int maxY = chunkdistY;
		int minZ = -chunkdistZ;
		int maxZ = chunkdistZ;
		int id = 1;
		System.out.println("============");
		for(int x = minX; x <= maxX; x+=1)
		{
			for(int y = minY; y <= maxY; y+=1)
			{
				for(int z = minZ; z <= maxZ; z+=1)
				{
				loadedChunks.add(new Chunk(x,y,z, id));
				//id++;
			//	System.out.println(id + "= id");
				}
			}
		}
		System.out.println("min-" + min + " max-" + max);
		System.out.println("============");
		//System.out.println(loadedChunks.size());
	}

	
	public static void update()
	{
		loadedChunks.clear();
		int minX = -chunkdistX; //horizontal chunks to load
		int maxX = chunkdistX;
		int minY = -chunkdistY;
		int maxY = chunkdistY;
		int minZ = -chunkdistZ;
		int maxZ = chunkdistZ;
		int id = 1;
		System.out.println("============");
		for(int x = minX; x <= maxX; x+=1)
		{
			for(int y = minY; y <= maxY; y+=1)
			{
				for(int z = minZ; z <= maxZ; z+=1)
				{
				loadedChunks.add(new Chunk(x + chunkXoff,(y) + chunkYoff,z + chunkZoff, id));
				//id++;
			//	System.out.println(id + "= id");
				}
			}
		}
		System.out.println("============");
	}
	
}
