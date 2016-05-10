package entities;

import java.awt.RenderingHints.Key;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TextureModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity{
	
	private static final float RUN_SPEED = 15f;
	private static final float TURN_SPEED = 100;
	private static final float GRAVITY = -40;
	private static final float JUMP_POWER = 20;
	
	private static final float TERRAIN_HEIGHT = 0.0f;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardSpeed = 0;
	
	private boolean isInAir = false;

	public Player(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	
	}
	
	public void move(Terrain terrain){
		checkInputs();
		
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
	
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		
		super.increasePosition(dx, 0, dz);
		
		upwardSpeed +=GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y<terrainHeight){
			upwardSpeed = 0;
			isInAir = false; 
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump(float power){
		if(!isInAir){
			this.upwardSpeed = power;
			isInAir = true;
		}
	}
	
	private void checkInputs(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = RUN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_T)){
			this.currentSpeed = RUN_SPEED+60;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = -RUN_SPEED;
		}else{
			this.currentSpeed = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.currentTurnSpeed = -TURN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.currentTurnSpeed = TURN_SPEED;
		}else{
			this.currentTurnSpeed = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			jump(JUMP_POWER);
		}else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			jump(JUMP_POWER+100);
		}
		
	}
	
	public float getTurnSpeed(){
		return currentTurnSpeed;
	}
	
	public float getCurrentSpeed(){
		return currentSpeed;
	}
	
}
