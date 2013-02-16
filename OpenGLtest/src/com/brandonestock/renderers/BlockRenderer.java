package com.brandonestock.renderers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.brandonestock.main.Start;
import com.brandonestock.types.Block;

public class BlockRenderer extends Renderer{

	static boolean wireframe = false;//works, kinda :/
	protected BlockRenderer() 
	{
	//dummy constructor
	}
	
	static public void render(Block toRender)
	{
		float x, y,z;
		x = toRender.x;
		y = toRender.y;
		z = toRender.z;
		GL11.glPushMatrix();
		float scale = 1;
	
		x*=scale;
		y*=scale;
		z*=scale;
		GL11.glTranslatef(x, y, z);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		
		float scaleVal = (1.0f/256.0f);
		int wid = toRender.texIndex; //x tile
		int hid = 10; //y tile
		//for grass
				if(wid == 0 && hid == 0){wid = 3;}
		wid*=16;
		hid*=16;
		float x1 = (scaleVal * wid); float y1 = (hid * scaleVal);
		float x2 = (scaleVal * wid) + (scaleVal * 16); float y2 = (scaleVal * hid) + (scaleVal * 16);
		
		
		
		//front face
		if(wireframe== false)
		{
		GL11.glBegin(GL11.GL_QUADS); 
			GL11.glNormal3f( 0.0f, 0.0f, 1.0f);  
			GL11.glTexCoord2f(x1, y1); GL11.glVertex3f(-(scale/2f), (scale/2f),(scale/2f)); 
			GL11.glTexCoord2f(x2, y1);GL11.glVertex3f((scale/2f), (scale/2f), (scale/2f));			 
			GL11.glTexCoord2f(x2, y2);GL11.glVertex3f((scale/2f), -(scale/2f), (scale/2f));			
			GL11.glTexCoord2f(x1, y2);GL11.glVertex3f(-(scale/2f), -(scale/2f), (scale/2f)); 
		GL11.glEnd();
		
		//right face
		GL11.glBegin(GL11.GL_QUADS); 
			GL11.glNormal3f( 1.0f, 0.0f, 0.0f);
		
			GL11.glTexCoord2f(x1, y1);GL11.glVertex3f((scale/2f), (scale/2f), (scale/2f)); 
			GL11.glTexCoord2f(x2, y1);GL11.glVertex3f((scale/2f), (scale/2f), -(scale/2f));			 
			GL11.glTexCoord2f(x2, y2);GL11.glVertex3f((scale/2f), -(scale/2f), -(scale/2f));			
			GL11.glTexCoord2f(x1, y2);GL11.glVertex3f((scale/2f), -(scale/2f), (scale/2f)); 
		GL11.glEnd();
		//back face
		GL11.glBegin(GL11.GL_QUADS); 
			GL11.glNormal3f( 0.0f, 0.0f, -1.0f);  
				
			GL11.glTexCoord2f(x1, y1);GL11.glVertex3f(-(scale/2f), (scale/2f), -(scale/2f)); 
			GL11.glTexCoord2f(x2, y1);GL11.glVertex3f((scale/2f), (scale/2f), -(scale/2f));			 
			GL11.glTexCoord2f(x2, y2);GL11.glVertex3f((scale/2f), -(scale/2f), -(scale/2f));			
			GL11.glTexCoord2f(x1, y2);GL11.glVertex3f(-(scale/2f), -(scale/2f), -(scale/2f)); 
		GL11.glEnd();		
		//left face
		GL11.glBegin(GL11.GL_QUADS); 
			GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
		
			GL11.glTexCoord2f(x1, y1);GL11.glVertex3f(-(scale/2f), (scale/2f), (scale/2f)); 
			GL11.glTexCoord2f(x2, y1);GL11.glVertex3f(-(scale/2f), (scale/2f), -(scale/2f));			 
			GL11.glTexCoord2f(x2, y2);GL11.glVertex3f(-(scale/2f), -(scale/2f), -(scale/2f));			
			GL11.glTexCoord2f(x1, y2);GL11.glVertex3f(-(scale/2f), -(scale/2f), (scale/2f)); 
		GL11.glEnd();
		
		//sets up for grass
		if(wid == 48 && hid == 0) {wid = 0; wid*=16; x1 = (scaleVal * wid); x2 = (scaleVal * wid) + (scaleVal * 16);GL11.glColor3f(0.25f, 0.859f, 0.25f);}
		
		//top face
		GL11.glBegin(GL11.GL_QUADS); 
			GL11.glNormal3f( 0.0f, 1.0f, 0.0f);
		
			GL11.glTexCoord2f(x1, y1);GL11.glVertex3f(-(scale/2f), (scale/2f), -(scale/2f)); 
			GL11.glTexCoord2f(x2, y1);GL11.glVertex3f((scale/2f), (scale/2f), -(scale/2f));			 
			GL11.glTexCoord2f(x2, y2);GL11.glVertex3f((scale/2f), (scale/2f), (scale/2f));			
			GL11.glTexCoord2f(x1, y2);GL11.glVertex3f(-(scale/2f), (scale/2f), (scale/2f)); 
		GL11.glEnd();
		
		//sets up for grass
				if(wid == 0 && hid == 0) {wid = 2; wid*=16; x1 = (scaleVal * wid); x2 = (scaleVal * wid) + (scaleVal * 16);GL11.glColor3f(1.0f, 1.0f, 1.0f);}
				
		
		
		//bottom face
				GL11.glBegin(GL11.GL_QUADS); 
					GL11.glNormal3f( 0.0f, -1.0f, 0.0f);
				
					GL11.glTexCoord2f(x1, y1);GL11.glVertex3f(-(scale/2f), -(scale/2f), -(scale/2f)); 
					GL11.glTexCoord2f(x2, y1);GL11.glVertex3f((scale/2f), -(scale/2f), -(scale/2f));			 
					GL11.glTexCoord2f(x2, y2);GL11.glVertex3f((scale/2f), -(scale/2f), (scale/2f));			
					GL11.glTexCoord2f(x1, y2);GL11.glVertex3f(-(scale/2f), -(scale/2f), (scale/2f)); 
				GL11.glEnd();
		}
		else
		{
			GL11.glBegin(GL11.GL_LINES); 
			GL11.glNormal3f( 0.0f, 0.0f, 1.0f);  
			  GL11.glVertex3f(-(scale/2f), (scale/2f),(scale/2f)); 
			 GL11.glVertex3f((scale/2f), (scale/2f), (scale/2f));			 
			 GL11.glVertex3f((scale/2f), -(scale/2f), (scale/2f));			
			 GL11.glVertex3f(-(scale/2f), -(scale/2f), (scale/2f)); 
		GL11.glEnd();
		
		//right face
		GL11.glBegin(GL11.GL_LINES); 
			GL11.glNormal3f( 1.0f, 0.0f, 0.0f);
		
			 GL11.glVertex3f((scale/2f), (scale/2f), (scale/2f)); 
			 GL11.glVertex3f((scale/2f), (scale/2f), -(scale/2f));			 
			 GL11.glVertex3f((scale/2f), -(scale/2f), -(scale/2f));			
			 GL11.glVertex3f((scale/2f), -(scale/2f), (scale/2f)); 
		GL11.glEnd();
		//back face
		GL11.glBegin(GL11.GL_LINES); 
			GL11.glNormal3f( 0.0f, 0.0f, -1.0f);  
				
			 GL11.glVertex3f(-(scale/2f), (scale/2f), -(scale/2f)); 
			 GL11.glVertex3f((scale/2f), (scale/2f), -(scale/2f));			 
			 GL11.glVertex3f((scale/2f), -(scale/2f), -(scale/2f));			
			 GL11.glVertex3f(-(scale/2f), -(scale/2f), -(scale/2f)); 
		GL11.glEnd();		
		//left face
		GL11.glBegin(GL11.GL_LINES); 
			GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
		
			 GL11.glVertex3f(-(scale/2f), (scale/2f), (scale/2f)); 
			 GL11.glVertex3f(-(scale/2f), (scale/2f), -(scale/2f));			 
			 GL11.glVertex3f(-(scale/2f), -(scale/2f), -(scale/2f));			
			 GL11.glVertex3f(-(scale/2f), -(scale/2f), (scale/2f)); 
		GL11.glEnd();
		
		//sets up for grass
		if(wid == 48 && hid == 0) {wid = 0; wid*=16; x1 = (scaleVal * wid); x2 = (scaleVal * wid) + (scaleVal * 16);GL11.glColor3f(0.25f, 0.859f, 0.25f);}
		
		//top face
		GL11.glBegin(GL11.GL_LINES); 
			GL11.glNormal3f( 0.0f, 1.0f, 0.0f);
		
			 GL11.glVertex3f(-(scale/2f), (scale/2f), -(scale/2f)); 
			 GL11.glVertex3f((scale/2f), (scale/2f), -(scale/2f));			 
			 GL11.glVertex3f((scale/2f), (scale/2f), (scale/2f));			
			 GL11.glVertex3f(-(scale/2f), (scale/2f), (scale/2f)); 
		GL11.glEnd();
		
		//sets up for grass
				if(wid == 0 && hid == 0) {wid = 2; wid*=16; x1 = (scaleVal * wid); x2 = (scaleVal * wid) + (scaleVal * 16);GL11.glColor3f(1.0f, 1.0f, 1.0f);}
				
		
		
		//bottom face
				GL11.glBegin(GL11.GL_LINES); 
					GL11.glNormal3f( 0.0f, -1.0f, 0.0f);
				
					 GL11.glVertex3f(-(scale/2f), -(scale/2f), -(scale/2f)); 
					 GL11.glVertex3f((scale/2f), -(scale/2f), -(scale/2f));			 
					 GL11.glVertex3f((scale/2f), -(scale/2f), (scale/2f));			
					 GL11.glVertex3f(-(scale/2f), -(scale/2f), (scale/2f)); 
				GL11.glEnd();
		}
		GL11.glPopMatrix();
	}

	
	
	
}
