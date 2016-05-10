package engineTester;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import OBJConverter.ModelData;
import OBJConverter.OBJFileLoader;
import Shaders.StaticShader;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.NPC;
import entities.Player;
import models.RawModel;
import models.TextureModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import renderEngine.EntityRenderer;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {
	
	private static int CHUNK_COUNT = 4;
	private static Random random;
	
	public static void main(String[] args){
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		//Shader implemetation
		

		float[] vertices = {			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
				
		};
		
		float[] textureCoords = {
				
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0

				
		};
		
		int[] indices = {
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

		};
		
		random = new Random();
		
		//RawModel cubeModel = loader.loadToVAO(vertices,textureCoords,indices);
		
		//Model Data Method
		
		//ModelData sonnereatModel = OBJFileLoader.loadOBJ("sonnereat");
		//ModelData seqoiaModel = OBJFileLoader.loadOBJ("seqoia2");
		//ModelData scotstModel = OBJFileLoader.loadOBJ("scots");
		//ModelData pintModel = OBJFileLoader.loadOBJ("pin");
		
		//RawModel sonnereat = loader.loadToVAO(sonnereatModel.getVertices(), sonnereatModel.getTextureCoords(), sonnereatModel.getNormals(),sonnereatModel.getIndices());
		
		
		//RawModel sonnereat = OBJLoader.loadObjModel("sonnereat", loader);
		//RawModel seqoia = OBJLoader.loadObjModel("seqoia2", loader);
		//RawModel scots = OBJLoader.loadObjModel("scots", loader);
		//RawModel pin = OBJLoader.loadObjModel("pin", loader);
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("colors"));
		
		TextureModel SonnereatModel = geTextureModel("sonnereat", "colors", loader);
		TextureModel SeqoiaModel = geTextureModel("seqoia2", "colors", loader);
		TextureModel ScotsModel = geTextureModel("scots", "colors", loader);
		TextureModel PinModel = geTextureModel("pin", "colors", loader);
		
		//Specular Lighting
		texture.setReflectivity(0);
		texture.setShineDamper(100);
		
		Entity entity = new Entity(SonnereatModel, new Vector3f(0,0,-1), 0, 0, 0, 1);
		
		Light light  = new Light(new Vector3f(20000,40000, 10000), new Vector3f(1.5f,1.5f,1.5f));	
		
		
		
		//Player Creation
		RawModel playerRawModel = OBJLoader.loadObjModel("batman", loader);
		
		TextureModel batman = new TextureModel(playerRawModel, texture);
	
		Player player = new Player(batman, new Vector3f(10,10,10), 0, 180, 0, 5);
		
		
		
		List<Player> bats = new ArrayList<Player>();
		
		for(int i=0; i<1; i++){
			bats.add(new Player(batman, new Vector3f(0, 0, 0), 0, 0, 0, 5));
		}
		
		Camera camera = new Camera(player);
		
		//Terrain Creation
		
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("green"));
		
		grassTexture.setReflectivity(0);
		grassTexture.setShineDamper(100);
		
		
		//Terrain Texture Configuration
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("green"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("water2"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("ground"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("water3"));
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//TerrainTexturePack
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		Terrain terrain1 = new Terrain(0, 0, loader, texturePack,blendMap, "heightMap");
		Terrain terrain2 = new Terrain(0, 0, loader, texturePack,blendMap, "heightMap");
		Terrain terrain3 = new Terrain(0, -1, loader, texturePack,blendMap, "heightMap");
		Terrain terrain4 = new Terrain(-1, 0, loader, texturePack,blendMap, "heightMap");
		Terrain terrain5 = new Terrain(-1, -1, loader,texturePack,blendMap, "heightMap");
		
		//Terrain ArrayList
		
		List<Terrain> terrains = new ArrayList<Terrain>();

		
		for(int j = 0 ; j<CHUNK_COUNT; j++){
			for(int i=0; i<CHUNK_COUNT; i++){
				Terrain terrain = new Terrain(0-i, 0-j, loader, texturePack,blendMap, "heightMap");
				terrains.add(terrain);
			}
		}
		
		int t=0;
		for(Terrain terrain: terrains){
			System.out.println("Terrain "+t+" : "+terrain.getX()+" "+terrain.getZ());
			t++;
		}
		
		//Deer Creation
		RawModel deerRawModel = OBJLoader.loadObjModel("deer", loader);
		ModelTexture deerTexture = new ModelTexture(loader.loadTexture("deerTex"));
		
		TextureModel deerModel = new TextureModel(deerRawModel, deerTexture);
		
		List<NPC> deerList = new ArrayList<NPC>();
		for(int i=0; i<50; i++){
			float x = random.nextFloat() * 1000 - 500;
			float z = random.nextFloat() * 1000 - 500;
				int terrainIndex = getTerrainIndex(x,z);
			
			float y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			deerList.add(new NPC(deerModel, new Vector3f(x, y, z), 0, y, 0, 1));
		}
		
		for(NPC deer : deerList){
			deer.setMovementDuration(random.nextFloat()*5+1);
		}
		
		//zebra Creation
		RawModel zebraRawModel = OBJLoader.loadObjModel("zebra", loader);
		ModelTexture zebraTexture = new ModelTexture(loader.loadTexture("zebra"));
		
		TextureModel zebraModel = new TextureModel(zebraRawModel, zebraTexture);
		
		List<NPC> zebraList = new ArrayList<NPC>();
		for(int i=0; i<50; i++){
			float x = random.nextFloat() * 1000 - 500;
			float z = random.nextFloat() * 1000 - 500;
				int terrainIndex = getTerrainIndex(x,z);
			
			float y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			zebraList.add(new NPC(zebraModel, new Vector3f(x, y, z), 0, y, 0, 4));
		}
		
		for(NPC zebra : zebraList){
			zebra.setMovementDuration(random.nextFloat()*5+1);
		}
		
		
		//zebra Creation
		RawModel mammothRawModel = OBJLoader.loadObjModel("mammoth", loader);
		ModelTexture mammothTexture = new ModelTexture(loader.loadTexture("colors"));
		
		TextureModel mammothModel = new TextureModel(mammothRawModel, mammothTexture);
		
		List<NPC> mammothList = new ArrayList<NPC>();
		for(int i=0; i<4; i++){
			float x = random.nextFloat() * 1000 - 500;
			float z = random.nextFloat() * 1000 - 500;
				int terrainIndex = getTerrainIndex(x,z);
			
			float y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			mammothList.add(new NPC(mammothModel, new Vector3f(x, y, z), 0, y, 0, 10));
		}
		
		for(NPC mammoth : mammothList){
			mammoth.setMovementDuration(random.nextFloat()*5+1);
		}
		
		//Sheep Generation
		TextureModel sheepModel = geTextureModel("elk", "sheep", loader);
 		List<NPC> sheepList = generateEntities(sheepModel, terrains,20,1000,1000, 4);
 		
 		//Sheep Generation
		TextureModel rhinoModel = geTextureModel("rhino", "elk", loader);
 		List<NPC> rhinoList = generateEntities(rhinoModel, terrains,10,1000,1000, 7);
 		
 		//Rabit
 		TextureModel rabbitModel = geTextureModel("rabbit", "elk", loader);
 		List<NPC> rabbitList = generateEntities(rabbitModel, terrains,20,1000,1000, 2);
 		
 		//Rabit
 		TextureModel elkModel = geTextureModel("elk", "elk", loader);
 		List<NPC> elkList = generateEntities(elkModel, terrains,15,1000,1000, 1);
 		
 		//Rabit
 		TextureModel dogModel = geTextureModel("dog", "wolf", loader);
 		List<NPC> dogList = generateEntities(dogModel, terrains,5,1000,1000, 1);
 		
 		//Rabit
 		TextureModel dinosourModel = geTextureModel("dinosour", "colors", loader);
 		List<NPC> dinosourList = generateEntities(dinosourModel, terrains,5,1000,1000, 25);
 		
 		//Rabit
 		TextureModel wolfModel = geTextureModel("wolf", "wolf", loader);
 		List<NPC> wolfList = generateEntities(wolfModel, terrains,8,1000,1000, 1);
 		
 		//Rabit
 		TextureModel turtleModel = geTextureModel("turtle", "turtle", loader);
 		List<NPC> turtleList = generateEntities(turtleModel, terrains,12,1000,1000, 2);
 		
 		//Rabit
 		TextureModel goatModel = geTextureModel("goat", "goat", loader);
 		List<NPC> goatList = generateEntities(goatModel, terrains,14,1000,1000, 0.4f);
 		
 		//Rabit
 		TextureModel birdModel = geTextureModel("bird", "bird", loader);
 		List<NPC> birdList = generateEntities(birdModel, terrains,20,1000,1000, 1);
 		
 	
 		
 		
		
		
		//Tree Generation
		List<Entity> sonnereatList = new ArrayList<Entity>();
		List<Entity> seqoiaList = new ArrayList<Entity>();
		List<Entity> scotsList = new ArrayList<Entity>();
		List<Entity> pinList = new ArrayList<Entity>();
		
		
		for(int i=0; i<200; i++){
			float x = random.nextFloat() * 1000 - 500;
			float z = random.nextFloat() * 1000 - 500;
			int terrainIndex = getTerrainIndex(x,z);
			
			float y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			sonnereatList.add(new Entity(SonnereatModel, new Vector3f(x, y, z), 0, y, 0, 20));
		}
		
		//Generating Seqoias
		for(int i=0; i<200; i++){
			float x = random.nextFloat() * 1000 - 500;
			float z = random.nextFloat() * 1000 - 500;
			int terrainIndex = getTerrainIndex(x,z);
			
			float y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			seqoiaList.add(new Entity(SeqoiaModel, new Vector3f(x, y, z), 0, y, 0, 10));
		}
		
		//Generating Scots
		for(int i=0; i<200; i++){
			float x = random.nextFloat() * 1000 - 500;
			float z = random.nextFloat() * 1000 - 500;
				int terrainIndex = getTerrainIndex(x,z);
			
			float y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			scotsList.add(new Entity(ScotsModel, new Vector3f(x, y, z), 0, y, 0, 15));
		}
		
		//Generating Tall Pins
		for(int i=0; i<200; i++){
			float x = random.nextFloat() * 1000 - 500;
			float z = random.nextFloat() * 1000 - 500;
			int terrainIndex = getTerrainIndex(x,z);
			
			float y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			pinList.add(new Entity(PinModel, new Vector3f(x, y, z), 0, y, 0, 10));
		}
		
		
		TextureModel grassModel1 = new TextureModel(OBJLoader.loadObjModel("grassModel", loader),new ModelTexture(loader.loadTexture("grass")));
		TextureModel grassModel2 = new TextureModel(OBJLoader.loadObjModel("fern", loader),new ModelTexture(loader.loadTexture("fern")));
		TextureModel grassModel3 = new TextureModel(OBJLoader.loadObjModel("fern", loader),new ModelTexture(loader.loadTexture("grass")));
		
		grassModel1.getTexture().setReflectivity(0);
		grassModel2.getTexture().setReflectivity(0);
		grassModel3.getTexture().setReflectivity(0);
		
		grassModel1.getTexture().setShineDamper(100);
		grassModel2.getTexture().setShineDamper(100);
		grassModel3.getTexture().setShineDamper(110);
		
		grassModel1.getTexture().setHasTransparency(true);
		grassModel1.getTexture().setFakeLighting(true);
		
		grassModel2.getTexture().setHasTransparency(true);
		grassModel2.getTexture().setFakeLighting(true);
		
		grassModel3.getTexture().setHasTransparency(true);
		grassModel3.getTexture().setFakeLighting(true);
		
		
		List<Entity> grasses = new ArrayList<Entity>();
		for(int i=0; i<800; i++){
			float x = random.nextFloat() * 1000 - 500;
			float z = random.nextFloat() * 1000 - 500;
			int terrainIndex = getTerrainIndex(x,z);
			
			float y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			float scale = random.nextFloat() * 4;
			grasses.add(new Entity(grassModel1, new Vector3f(x, y, z), 0, y, 0, scale));
			
			x = random.nextFloat() * 1000 - 500;
			z = random.nextFloat() * 1000 - 500;
			terrainIndex = getTerrainIndex(x,z);
			
			y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			
			grasses.add(new Entity(grassModel1, new Vector3f(x, y, z), 0, y, 0, scale));
			grasses.add(new Entity(grassModel2, new Vector3f(x, y, z-0.01f), 0, y, 0, scale/4));
			grasses.add(new Entity(grassModel3, new Vector3f(x, y, z-0.02f), 0, y, 0, scale/4));
			
			x = random.nextFloat() * 1000 - 500;
			z = random.nextFloat() * 1000 - 500;
			terrainIndex = getTerrainIndex(x,z);
			
			y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
			
			grasses.add(new Entity(grassModel3, new Vector3f(x, y, z), 0, y, 0, scale/4));
		}
		
		float acc=0.002f;
		long startTime = System.nanoTime();
		float vi = 0.0001f;
		int counter = 0;
		
		List<Entity> stalls = new ArrayList<Entity>();
		
		RawModel stallRawModel = OBJLoader.loadObjModel("stall", loader);
		ModelTexture stallTexture = new ModelTexture(loader.loadTexture("abs1"));
		TextureModel stallModel = new TextureModel(stallRawModel, stallTexture);
		
		MasterRenderer renderer = new MasterRenderer();
		
		
		//Free World Blocks Generation
		RawModel cubeRawModel = OBJLoader.loadObjModel("wall", loader);
		ModelTexture cubeTexture = new ModelTexture(loader.loadTexture("colors"));

		TextureModel cubeModel = new TextureModel(cubeRawModel, cubeTexture);
		
		ArrayList<Entity> cubes = new ArrayList<Entity>();
		
		
		//Texture Model initialization
		RawModel barrelRawModel = OBJLoader.loadObjModel("barrel", loader);
		RawModel tradeRawModel = OBJLoader.loadObjModel("trade", loader);
		RawModel homeRawModel = OBJLoader.loadObjModel("home", loader);
		RawModel bathroomRawModel = OBJLoader.loadObjModel("bathroom", loader);
		RawModel wellRawModel = OBJLoader.loadObjModel("well", loader);

		
		ModelTexture crateTexture = new ModelTexture(loader.loadTexture("crate"));
		
		TextureModel barrelModel = new TextureModel(barrelRawModel, crateTexture);
		TextureModel tradeModel = new TextureModel(tradeRawModel, crateTexture);
		TextureModel homeModel = new TextureModel(homeRawModel, crateTexture);
		TextureModel bathroomModel = new TextureModel(bathroomRawModel, crateTexture);
		TextureModel wellModel = new TextureModel(wellRawModel, crateTexture);
		
		while(!Display.isCloseRequested()){
			
			for(Terrain terrain: terrains){
				renderer.processTerrain(terrain);
			}
			
			entity.increasePosition(0, 0.5f, 0f);
			entity.increaseRotation(0.0f, 0.5f, 0);
			
			renderer.render(light, camera);
			
			camera.move();
			
			renderer.preocessEntity(player);
			//player.move();
			
			System.out.println(player.getPosition().getX());
			
			//Game Logic

			long endTime = System.nanoTime();

			long duration = (endTime - startTime)/1000000000;
			
			
			System.out.println("duration" + duration);
			
//			counter++;
//			if(counter>=1000){
//				counter = 0;
//				acc = -acc;
//				startTime = System.nanoTime();
//			}
//			
//			float vf = acc * duration;
			
			for(Entity tree:sonnereatList){
				renderer.preocessEntity(tree);
			}
			
			for(Entity tree:seqoiaList){
				renderer.preocessEntity(tree);
			}
			
			for(Entity tree:scotsList){
				renderer.preocessEntity(tree);
			}
			
			for(Entity tree:pinList){
				renderer.preocessEntity(tree);
			}
			
			for(Entity grass: grasses){
				renderer.preocessEntity(grass);
			}
			
			System.out.println("Players "+ bats.size());
			
//			for(Player bat: bats){
//				renderer.preocessEntity(bat);
//				bat.move();
//				System.out.println(bat.getCurrentSpeed()+ " "+bat.getTurnSpeed());
//			}
			
			renderer.preocessEntity(player);
			int playerTX = (int)Math.floor(player.getPosition().getX()/800);
			int playerTZ = (int)Math.floor(player.getPosition().getZ()/800);
			
			//System.out.println(playerTZ);
			
			//if(player.getPosition().getZ() <0)playerTZ=playerTZ-1;
			
			int terrainIndex = Math.abs(playerTX)+Math.abs(playerTZ) * CHUNK_COUNT;
			
//			System.out.println(playerTX+" "+playerTZ+" = "+terrainIndex);
//			System.out.println("Terrain "+terrainIndex+" "+terrains.get(terrainIndex).getX()+" "+terrains.get(terrainIndex).getZ());
//			System.out.println(terrains.get(terrainIndex).getHeightOfTerrain(player.getPosition().getX(), player.getPosition().getZ()));
			player.move(terrains.get(terrainIndex));
			
			
			//Free World Building
			
			if(Keyboard.isKeyDown(Keyboard.KEY_B)){
				float x = player.getPosition().getX();
				float y = player.getPosition().getY();
				float z = player.getPosition().getZ();
				Vector3f pos = new Vector3f(x,y,z);
				float rotX = player.getRotX();
				float rotZ = player.getRotZ();
				cubes.add(new Entity(cubeModel,pos , rotX, 0, rotZ, 3));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_0)){
				float x = player.getPosition().getX();
				float y = player.getPosition().getY();
				float z = player.getPosition().getZ();
				Vector3f pos = new Vector3f(x,y,z);
				float rotX = player.getRotX();
				float rotZ = player.getRotZ();
				cubes.add(new Entity(barrelModel,pos , rotX, 0, rotZ, 3));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_1)){
				float x = player.getPosition().getX();
				float y = player.getPosition().getY();
				float z = player.getPosition().getZ();
				Vector3f pos = new Vector3f(x,y,z);
				float rotX = player.getRotX();
				float rotZ = player.getRotZ();
				cubes.add(new Entity(tradeModel,pos , rotX, 0, rotZ, 3));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_2)){
				float x = player.getPosition().getX();
				float y = player.getPosition().getY();
				float z = player.getPosition().getZ();
				Vector3f pos = new Vector3f(x,y,z);
				float rotX = player.getRotX();
				float rotZ = player.getRotZ();
				cubes.add(new Entity(homeModel,pos , rotX, 0, rotZ, 20));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_3)){
				float x = player.getPosition().getX();
				float y = player.getPosition().getY();
				float z = player.getPosition().getZ();
				Vector3f pos = new Vector3f(x,y,z);
				float rotX = player.getRotX();
				float rotZ = player.getRotZ();
				cubes.add(new Entity(bathroomModel,pos , rotX, 0, rotZ, 10));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_4)){
				float x = player.getPosition().getX();
				float y = player.getPosition().getY();
				float z = player.getPosition().getZ();
				Vector3f pos = new Vector3f(x,y,z);
				float rotX = player.getRotX();
				float rotZ = player.getRotZ();
				cubes.add(new Entity(wellModel,pos , rotX, 0, rotZ, 3));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_5)){
				float x = player.getPosition().getX();
				float y = player.getPosition().getY();
				float z = player.getPosition().getZ();
				Vector3f pos = new Vector3f(x,y,z);
				float rotX = player.getRotX();
				float rotZ = player.getRotZ();
				cubes.add(new Entity(cubeModel,pos , rotX, 0, rotZ, 3));
			}
			
			for(Entity cube: cubes){
				renderer.preocessEntity(cube);
			}
			
//			if(Keyboard.isKeyDown(Keyboard.KEY_B)){
//				Vector3f position = camera.getPosition();
//				stalls.add(new Entity(stallModel, position, 0, 0, 0, 0.25f));
//			}
//			
//			//Process Custom builds
//			for(Entity stall: stalls){
//				renderer.preocessEntity(stall);
//			}
			
			
			//System.out.println("P"+player.getPosition().getX()+" "+player.getPosition().getY()+" "+player.getPosition().getZ());
			
			//Deer movements
			
			if(duration > 0){
				startTime = System.nanoTime();
				for(NPC deer:deerList){			
					float rTime = random.nextFloat() * 3+1;
					
					if(deer.getRestDuration()>0){
						deer.decreaseRestDuration();
					}
					if(deer.getTurnDuration()>0){
						deer.decreaseTurnDuration();
					}
					if(deer.getMovementDuration()>0){
						deer.decreaseMovementDuration();
					}
					
					if(deer.getMovementDuration() <=0){
						deer.setRestDuration(rTime+4);
					}
					if(deer.getRestDuration() <=0){
						deer.setTurnDuration(rTime);
						deer.setTurnDirection((int)rTime%2);
					}
					if(deer.getTurnDuration()<=0){
						deer.setMovementDuration(rTime);
					}
					
				}
			}
			
			//zebra movements
			
			if(duration > 0){
				startTime = System.nanoTime();
				for(NPC zebra:zebraList){			
					float rTime = random.nextFloat() * 3+1;
					
					if(zebra.getRestDuration()>0){
						zebra.decreaseRestDuration();
					}
					if(zebra.getTurnDuration()>0){
						zebra.decreaseTurnDuration();
					}
					if(zebra.getMovementDuration()>0){
						zebra.decreaseMovementDuration();
					}
					
					if(zebra.getMovementDuration() <=0){
						zebra.setRestDuration(rTime+4);
					}
					if(zebra.getRestDuration() <=0){
						zebra.setTurnDuration(rTime);
						zebra.setTurnDirection((int)rTime%2);
					}
					if(zebra.getTurnDuration()<=0){
						zebra.setMovementDuration(rTime);
					}
					
				}
			}
			
			//mammoth movements
			
			if(duration > 0){
				startTime = System.nanoTime();
				for(NPC mammoth:mammothList){			
					float rTime = random.nextFloat() * 3+1;
					
					if(mammoth.getRestDuration()>0){
						mammoth.decreaseRestDuration();
					}
					if(mammoth.getTurnDuration()>0){
						mammoth.decreaseTurnDuration();
					}
					if(mammoth.getMovementDuration()>0){
						mammoth.decreaseMovementDuration();
					}
					
					if(mammoth.getMovementDuration() <=0){
						mammoth.setRestDuration(rTime+4);
					}
					if(mammoth.getRestDuration() <=0){
						mammoth.setTurnDuration(rTime);
						mammoth.setTurnDirection((int)rTime%2);
					}
					if(mammoth.getTurnDuration()<=0){
						mammoth.setMovementDuration(rTime);
					}
					
				}
			}
			
			if(duration > 0){
				startTime = System.nanoTime();
				movementCalculation(sheepList);
				movementCalculation(rhinoList);
				movementCalculation(rabbitList);
				movementCalculation(elkList);
				movementCalculation(dogList);
				movementCalculation(dinosourList);
				movementCalculation(wolfList);
				movementCalculation(turtleList);
				movementCalculation(goatList);
				movementCalculation(birdList);
			}
			
			//Environment Rendering
			
			for(NPC deer:deerList){
				int ti = getTerrainIndex(deer.getPosition().getX(),deer.getPosition().getZ());
				deer.move(terrains.get(ti));
				renderer.preocessEntity(deer);
			}
			
			for(NPC zebra:zebraList){
				int ti = getTerrainIndex(zebra.getPosition().getX(),zebra.getPosition().getZ());
				zebra.move(terrains.get(ti));
				renderer.preocessEntity(zebra);
			}
			
			for(NPC mammoth:mammothList){
				int ti = getTerrainIndex(mammoth.getPosition().getX(),mammoth.getPosition().getZ());
				mammoth.move(terrains.get(ti));
				renderer.preocessEntity(mammoth);
			}
			
			renderNPC(sheepList, terrains, renderer);
			renderNPC(rhinoList, terrains, renderer);
			renderNPC(rabbitList, terrains, renderer);
			renderNPC(elkList, terrains, renderer);
			renderNPC(dogList, terrains, renderer);
			renderNPC(dinosourList, terrains, renderer);
			renderNPC(wolfList, terrains, renderer);
			renderNPC(turtleList, terrains, renderer);
			renderNPC(goatList, terrains, renderer);
			renderNPC(birdList, terrains, renderer);
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	
	private static TextureModel geTextureModel(String objFileName, String TextureFileName, Loader loader){
		RawModel rawModel = OBJLoader.loadObjModel(objFileName, loader);
		ModelTexture modelTexture = new ModelTexture(loader.loadTexture(TextureFileName));
		TextureModel textureModel = new TextureModel(rawModel, modelTexture);
		return textureModel;
	}
	
	private static ArrayList<NPC> generateEntities(TextureModel textureModel, List<Terrain> terrains, int COUNT,int areaX, int areaZ, float scale){				
			List<NPC> npcList = new ArrayList<NPC>();
			for(int i=0; i<COUNT; i++){
				float x = random.nextFloat() * areaX - 500;
				float z = random.nextFloat() * areaZ - 500;
				int terrainIndex = getTerrainIndex(x,z);
				
				float y = terrains.get(terrainIndex).getHeightOfTerrain(x, z);
				npcList.add(new NPC(textureModel, new Vector3f(x, y, z), 0, y, 0, scale));
			}
			
			for(NPC npc : npcList){
				npc.setMovementDuration(random.nextFloat()*5+1);
			}
			return (ArrayList<NPC>) npcList;
	}
	
	private static void movementCalculation(List<NPC> npcList){
		for(NPC npc:npcList){			
			float rTime = random.nextFloat() * 3+1;
			
			if(npc.getRestDuration()>0){
				npc.decreaseRestDuration();
			}
			if(npc.getTurnDuration()>0){
				npc.decreaseTurnDuration();
			}
			if(npc.getMovementDuration()>0){
				npc.decreaseMovementDuration();
			}
			
			if(npc.getMovementDuration() <=0){
				npc.setRestDuration(rTime+4);
			}
			if(npc.getRestDuration() <=0){
				npc.setTurnDuration(rTime);
				npc.setTurnDirection((int)rTime%2);
			}
			if(npc.getTurnDuration()<=0){
				npc.setMovementDuration(rTime);
			}
			
		}
	}
	
	private static void renderNPC(List<NPC> npcList, List<Terrain> terrains,MasterRenderer renderer ){
		for(NPC npc:npcList){
			int ti = getTerrainIndex(npc.getPosition().getX(),npc.getPosition().getZ());
			npc.move(terrains.get(ti));
			renderer.preocessEntity(npc);
		}
	}
	
	private static int getTerrainIndex(float x, float z){
		int playerTX = (int)Math.floor(x/800);
		int playerTZ = (int)Math.floor(z/800);
		
		int terrainIndex = Math.abs(playerTX)+Math.abs(playerTZ) * CHUNK_COUNT;
		
		return terrainIndex;
	}

}
