package renderEngine;

import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.util.vector.Matrix4f;

import Shaders.StaticShader;
import entities.Entity;
import models.RawModel;
import models.TextureModel;
import textures.ModelTexture;
import toolBox.Maths;
import java.util.List;
import java.util.Map;

public class EntityRenderer {

	private Matrix4f projectionMatrix;
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TextureModel, List<Entity>> entities){
		for(TextureModel model:entities.keySet()){
			prepareTextureModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch){
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(),GL_UNSIGNED_INT,0);
			}
			unbindTextureModel();
		}
	}
	
	private void prepareTextureModel(TextureModel model){
		RawModel rawModel = model.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		ModelTexture texture = model.getTexture();
		
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		if(texture.hasTransparency()){
			MasterRenderer.disableCulling();
		}
		
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
		
	}
	
	private void unbindTextureModel(){
		MasterRenderer.enableCulling();
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		
		shader.loadTransformationMatrix(transformationMatrix);
		
	}
	
	
	
	public void render(Entity entity, StaticShader shader){
		TextureModel textureModel = entity.getModel();
		RawModel model = textureModel.getRawModel();
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		//Transformation Matrix;
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		
		shader.loadTransformationMatrix(transformationMatrix);
		
		ModelTexture texture = textureModel.getTexture();
		
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureModel.getTexture().getID());
		glDrawElements(GL_TRIANGLES, model.getVertexCount(),GL_UNSIGNED_INT,0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	

}
