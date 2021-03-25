package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import entity.Entity;
import models.TexturedModel;
import models.RawModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolBox.Maths;

public class EntityRenderer {

	
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;

		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	

	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch) {
				prepareInstances(entity);
				
//				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE );
//				GL11.glStencilMask(0xFF);
//				GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
				//final render
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
				GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
				GL11.glDrawElements(GL11.GL_STENCIL_TEST, model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			unBindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = model.getTexture();
		
		shader.loadNumberOfRows(texture.getNumberOfRows());
		
		if(texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unBindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstances(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(),
				entity.getScale());
		
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
	

	

	
	
}
