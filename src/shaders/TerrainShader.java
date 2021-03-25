package shaders;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entity.Camera;
import entity.Light;
import renderEngine.DisplayManager;
import toolBox.Maths;

public class TerrainShader extends ShaderProgram{

	private static final int MAX_LIGHTS = 4;
	
	private static final String VERTEX_FILE = "/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "/shaders/terrainFragmentShader.txt";
	private static int edgeProgram; 
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuantion[];
	
	private int location_skyColor;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;

	private int location_shineDamper;
	private int location_reflectivity;
	
	private static int location_plane;
	
	private static int location_depthTex;
	
	private static int location_invWidth;
	private static int location_invHeight;
	
	public TerrainShader() {	
			super(VERTEX_FILE, FRAGMENT_FILE);
			edgeProgram = super.programID;
	}

	@Override 
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColor = super.getUniformLocation("skyColor");
		
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		
		location_plane = super.getUniformLocation("plane");
		
		location_depthTex = super.getUniformLocation("depthTex");
		location_invWidth = super.getUniformLocation("invWidth");
		location_invHeight = super.getUniformLocation("invHeight");

		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuantion = new int[MAX_LIGHTS];
		for(int i = 0; i<MAX_LIGHTS;i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
			location_lightColour[i] = super.getUniformLocation("lightColour["+i+"]");
			location_attenuantion[i] = super.getUniformLocation("attenuantion["+i+"]");
		}
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	
	
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(location_skyColor, new Vector3f(r,g,b));
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadLights(List<Light> lights) {
		for(int i = 0; i<MAX_LIGHTS;i++) {
			if(i<lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuantion[i], lights.get(i).getAttenuantion());
			}
			else {
				super.loadVector(location_lightPosition[i], new Vector3f(0f, 0f, 0f));
				super.loadVector(location_lightColour[i], new Vector3f(0f, 0f, 0f));
				super.loadVector(location_attenuantion[i], new Vector3f(1f, 0f, 0f));
			}
		}
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public static void renderEdge() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL20.glUseProgram(edgeProgram);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

//        glUniformMatrix4fv(inverseMatrixUniform, false, invMatrix.get(matrixBuffer));
        GL20.glUniform1f(location_invWidth, 1.0f / DisplayManager.WIDTH);
        GL20.glUniform1f(location_invHeight, 1.0f / DisplayManager.HEIGHT);
//        glUniform1i(normalTexUniform, 0);
//        glUniform1i(showEdgeUniform, showEdge ? 1 : 0);
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, normalTexture);
        GL20.glUniform1i(location_depthTex, 1);
//        glActiveTexture(GL_TEXTURE1);
//        GL20.glBindTexture(GL_TEXTURE_2D, location_depthTex);
//        glBindBuffer(GL_ARRAY_BUFFER, this.quadVbo);
//        glEnableVertexAttribArray(0);
//        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0L);
//        glDrawArrays(GL_TRIANGLES, 0, 6);
//        glBindTexture(GL_TEXTURE_2D, 0);
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, 0);
//        glDisableVertexAttribArray(0);
//        glBindBuffer(GL_ARRAY_BUFFER, 0);

        GL20.glUseProgram(0);
    }
	
}
