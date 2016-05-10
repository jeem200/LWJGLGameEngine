package textures;

public class ModelTexture {
	private int textureID;
	
	private float shineDamper = 100;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	public boolean isUseFakeLighting(){
		return useFakeLighting;
	}
	
	public void setFakeLighting(boolean b){
		this.useFakeLighting = b;
	}
	
	public boolean hasTransparency() {
		return hasTransparency;
	}
	
	public void setHasTransparency(boolean b) {
		hasTransparency = b;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public ModelTexture(int id){
		this.textureID = id;
	}
	
	public int getID(){
		
		return this.textureID;
	}
}
