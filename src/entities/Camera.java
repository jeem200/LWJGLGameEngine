package entities;

import java.awt.RenderingHints.Key;

import javax.swing.text.JTextComponent.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private float pitch=0;
	private float yaw=0;
	private float roll=0;
	
	private Player player;
	
	private boolean firstPersonFlag = false;
	
	public Camera(Player player){
		this.player = player;
	}
	
	public void move(){
		
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		if(firstPersonFlag ==true){
			distanceFromPlayer =-10.0f;
		}
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		
//		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
//			position.z -=1.02f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
//			position.x +=1.02f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
//			position.x -=1.02f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
//			position.z +=1.02f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
//			position.y+=1.02f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
//			position.y-=1.02f;
//		}
		/*
		if(Mouse.isButtonDown(0)){
			int dx = Mouse.getDX();
			int dy = Mouse.getDY();
			
			pitch -= dy;
			yaw +=dx;
		}*/
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F)){
			if(firstPersonFlag == true){
				firstPersonFlag = false;
			}else{
				firstPersonFlag = true;
			}
		}
		
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		
		position.y = player.getPosition().y+7 + verticalDistance;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom(){
		
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -=zoomLevel;		
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(0)){
			float pitchChange  = Mouse.getDY() * 0.1f;
			pitch -=pitchChange;
		}
	}
	
	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
