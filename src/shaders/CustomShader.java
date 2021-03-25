package shaders;

import java.util.ArrayList;
import java.util.List;

public class CustomShader extends ShaderProgram{

	private List<Integer> locations = new ArrayList<Integer>();
	
	private String VShader;
	private String FShdaer;
	
	public CustomShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
		this.VShader = vertexShader;
		this.FShdaer = fragmentShader;
	}
	
	@Override 
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		
	}
	
	public void addUniformLocation() {
		
	}
	
	public void loadShader() {
		super.createShader(VShader, FShdaer);
		
	}
}
