package entities;

import java.awt.RenderingHints.Key;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TextureModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class NPC extends Entity{
	
	private static float RUN_SPEED = 5f;
	private static float TURN_SPEED = 50;
	private static float GRAVITY = -40;
	private static float JUMP_POWER = 20;
	
	private static final float TERRAIN_HEIGHT = 0.0f;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardSpeed = 0;
	
	private boolean isInAir = false;
	
	//movement variables
	
	private float turnDuration;
	private float movementDuration;
	private float turnDirection;
	private float restDuration;

	public static float getRUN_SPEED() {
		return RUN_SPEED;
	}

	public static void setRUN_SPEED(float rUN_SPEED) {
		RUN_SPEED = rUN_SPEED;
	}

	public static float getTURN_SPEED() {
		return TURN_SPEED;
	}

	public static void setTURN_SPEED(float tURN_SPEED) {
		TURN_SPEED = tURN_SPEED;
	}

	public static float getGRAVITY() {
		return GRAVITY;
	}

	public static void setGRAVITY(float gRAVITY) {
		GRAVITY = gRAVITY;
	}

	public static float getJUMP_POWER() {
		return JUMP_POWER;
	}

	public static void setJUMP_POWER(float jUMP_POWER) {
		JUMP_POWER = jUMP_POWER;
	}

	public float getTurnDirection() {
		return turnDirection;
	}

	public void setTurnDirection(float turnDirection) {
		this.turnDirection = turnDirection;
	}

	public float getRestDuration() {
		return restDuration;
	}

	public void setRestDuration(float restDuration) {
		this.restDuration = restDuration;
	}

	public float getTurnDuration() {
		return turnDuration;
	}

	public void setTurnDuration(float turnDuration) {
		this.turnDuration = turnDuration;
	}

	public float getMovementDuration() {
		return movementDuration;
	}

	public void setMovementDuration(float movementDuration) {
		this.movementDuration = movementDuration;
	}
	
	public void decreaseMovementDuration(){
		this.movementDuration -=1;
	}
	public void decreaseTurnDuration(){
		this.turnDuration -=1;
	}
	public void decreaseRestDuration(){
		this.restDuration -=1;
	}

	public NPC(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	
		movementDuration=0;
		turnDirection=0;
		turnDuration=0;
		restDuration=0;
		
	}
	
	public void move(Terrain terrain){
		checkMovements();
		
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
	
	private void jump(){
		if(!isInAir){
			this.upwardSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	private void checkMovements(){
		if(movementDuration>0){
			this.currentSpeed = RUN_SPEED;
		}else{
			this.currentSpeed = 0;
		}
		if(turnDuration>0 && turnDirection==1){
			this.currentTurnSpeed = -TURN_SPEED;
		}else if(turnDuration>0 && turnDirection==0){
			this.currentTurnSpeed = TURN_SPEED;
		}else{
			this.currentTurnSpeed = 0;
		}
		
	}
	
	public float getTurnSpeed(){
		return currentTurnSpeed;
	}
	
	public float getCurrentSpeed(){
		return currentSpeed;
	}
	
}
