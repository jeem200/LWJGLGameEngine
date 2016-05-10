package terrains;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBox.Maths;

public class Terrain{
	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;
	
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = 256*256*256;
	
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	
	private TerrainTexture blendMap;
	
	
	private float[][] heights;
	
	boolean plainTerrain = true;
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap){
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader);
	}
	
	public Terrain(String objFileName, int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap){
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateFromModel(loader, objFileName);
	}
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap){
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
		plainTerrain = false;
	} 
	
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}
	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	//Non-Hilly Terrain Generation
	private RawModel generateTerrain(Loader loader){
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		
		int vertexPointer = 0;
		
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}

		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private RawModel generateFromModel(Loader loader, String objFileName){
		return OBJLoader.loadObjModel(objFileName, loader);
	}
	

	
	public float getHeightOfTerrain(float worldX,float worldZ){
		if(plainTerrain == true)return 0.0f;
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		
		//System.out.println(" worldX "+ worldX+ " this.x = "+this.x +"\t  worldZ "+ worldZ +" this.z" + this.z);
		//System.out.println("terrainX: "+terrainX+" TerrainZ: "+terrainZ + " heights length " + heights.length + " gridSquareSize: "+gridSquareSize);
		
		int gridX = ((int)Math.floor(terrainX/gridSquareSize));
		int gridZ = ((int)Math.floor(terrainZ / gridSquareSize));
		
		if(gridX >=heights.length -1 || gridX <0 || gridZ>=heights.length - 1 || gridZ<0){
			//System.out.println("The gridX was 0ed "+gridX+" "+gridZ);
			return 0;
		}
		//System.out.println("The normal grid values "+gridX+" "+gridZ);
		float xCoord  = (terrainX % gridSquareSize)/gridSquareSize;
		float zCoord  = (terrainZ % gridSquareSize) / gridSquareSize; 
		
		float finalHeight;
		if (xCoord <= (1-zCoord)) {
			finalHeight = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			finalHeight = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return finalHeight;
	}
	
	// Hilly Terrain Generation
	
	private RawModel generateTerrain(Loader loader, String heightMap){
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("res/"+heightMap+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int VERTEX_COUNT_CUSTOM = image.getHeight();
		heights = new float[VERTEX_COUNT_CUSTOM][VERTEX_COUNT_CUSTOM];
		
		int count = VERTEX_COUNT_CUSTOM * VERTEX_COUNT_CUSTOM;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT_CUSTOM-1)*(VERTEX_COUNT_CUSTOM-1)];
		
		int vertexPointer = 0;
		
		for(int i=0;i<VERTEX_COUNT_CUSTOM;i++){
			for(int j=0;j<VERTEX_COUNT_CUSTOM;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT_CUSTOM - 1) * SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT_CUSTOM - 1) * SIZE;
				Vector3f normal = calculateNormals(j, i, image);
				
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT_CUSTOM - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT_CUSTOM - 1);
				vertexPointer++;
			}
		}

		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT_CUSTOM-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT_CUSTOM-1;gx++){
				int topLeft = (gz*VERTEX_COUNT_CUSTOM)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT_CUSTOM)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	
	private Vector3f calculateNormals(int x, int z, BufferedImage image){
		float heightL = getHeight(x-1, z, image);
		float heightR = getHeight(x+1, z, image);
		float heightD = getHeight(x, z-1, image);
		float heightU = getHeight(x, z+1, image);
		
		Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}
	
	private float getHeight(int x, int z, BufferedImage image){
		
		if(x< 0 || x>=image.getHeight() || z<0 || z>=image.getHeight()){
			return 0;
		}
		
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOUR/2f;
		height /=MAX_PIXEL_COLOUR/2f;
		height *=MAX_HEIGHT;
		return height;
	}
	
	public float getX() {
		return x;
	}
	public float getZ() {
		return z;
	}
		
	public RawModel getModel() {
		return model;
	}
}