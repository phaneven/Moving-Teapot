package ass2.spec;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;


public class Other {
	float[] vertexPos = new float[] { -0.1f, 0, 0.1f, 0.1f, 0, 0.1f, 0.1f, 0.2f, 0.1f, -0.1f, 0.2f, 0.1f, 
			0.1f, 0, 0.1f, 0.1f, 0, -0.1f, 0.1f, 0.2f, -0.1f, 0.1f, 0.2f, 0.1f, 
			-0.1f, 0.2f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.2f, -0.1f, -0.1f, 0.2f, -0.1f, 
			-0.1f, 0, 0.1f, -0.1f, 0.2f, 0.1f, -0.1f, 0.2f, -0.1f, -0.1f, 0, -0.1f,
			-0.1f, 0, -0.1f, -0.1f, 0.2f, -0.1f, 0.1f, 0.2f, -0.1f, 0.1f, 0, -0.1f,
			-0.1f, 0, -0.1f, 0.1f, 0, -0.1f, 0.1f, 0, 0.1f, -0.1f, 0, 0.1f }; //cube
	
	float[] Normal = new float[] { 0, 0, 1f, 0, 0, 1f, 0, 0, 1f, 0, 0, 1f,
			1f, 0, 0, 1f, 0, 0, 1f, 0, 0, 1f, 0, 0, 0, 1f, 0, 0, 1f, 0, 0,
			1f, 0, 0, 1f, 0, -1f, 0, 0, -1f, 0, 0, -1f, 0, 0, -1f, 0, 0, 0,
			0, -1f, 0, 0, -1f, 0, 0, -1f, 0, 0, -1f, 0, -1f, 0, 0, -1f, 0,
			0, -1f, 0, 0, -1f, 0 };
	
	float[] Texture = new float[] {0, 1, 1, 1, 1, 0, 0, 0,
								   0, 1, 1, 1, 1, 0, 0, 0,
								   0, 1, 1, 1, 1, 0, 0, 0,
								   0, 1, 1, 1, 1, 0, 0, 0,
								   0, 1, 1, 1, 1, 0, 0, 0,
								   0, 1, 1, 1, 1, 0, 0, 0};
	
	float[] pos;
	FloatBuffer vertexData;
	FloatBuffer normalData;
	FloatBuffer textureData;
	private final int FloatBYTE = 4;
	private int[] bufferIds = new int[1];
	
	private static final String VERTEX_SHADER = "vertex_shader.glsl";
	private static final String FRAGMENT_SHADER = "fragment_shader.glsl";
	private String textureFileName = "src/ass2/spec/bark.jpg";
	private String textureExt = "jpg";
	private int program;
	private MyTexture tex;
	private int texUnitLoc;
	
	Other (float[] pos) {
		this.pos = pos;
	}
	
	void clientSideData (GL2 gl){
		gl.glEnable(GL2.GL_TEXTURE_2D);
		tex = new MyTexture (gl, textureFileName, textureExt, true);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		
		vertexData = Buffers.newDirectFloatBuffer(vertexPos);
		normalData = Buffers.newDirectFloatBuffer(Normal);
		textureData = Buffers.newDirectFloatBuffer(Texture);
		gl.glGenBuffers(1, bufferIds, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIds[0]);
		
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, (vertexPos.length + Normal.length + Texture.length)* FloatBYTE, null, GL2.GL_STATIC_DRAW);
		// vertex
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, vertexPos.length * FloatBYTE, vertexData);
		// Normal
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, vertexPos.length * FloatBYTE, Normal.length * FloatBYTE, normalData);
		// Texture
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, (vertexPos.length + Normal.length) * FloatBYTE, Texture.length * FloatBYTE, textureData);
	
		
		try {
			program = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		texUnitLoc = gl.glGetUniformLocation(program, "texUnit");
	}
	
	public void draw(GL2 gl) {
		clientSideData(gl);

		gl.glPushMatrix();
		{
			gl.glTranslated(pos[0], pos[1], pos[2]);
			
			// use the shader
			gl.glUseProgram(program);
			gl.glUniform1i(texUnitLoc, 0);
			
			// set current texture
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureId());
			
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
			
			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY); // VertexData
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY); // Normal
			gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY); // Texture
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIds[0]);
			gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0); 
			gl.glNormalPointer(GL2.GL_FLOAT, 0, Normal.length * FloatBYTE);		
			gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, (vertexPos.length + Normal.length)*FloatBYTE);	
			
			gl.glDrawArrays(GL2.GL_QUADS, 0, 4);
			
		
			gl.glDrawArrays(GL2.GL_QUADS, 4, 20);
					
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
			gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

			tex.release(gl);
			gl.glDisable(GL2.GL_TEXTURE_2D);
			gl.glUseProgram(0);
		}
		gl.glPopMatrix();
	}
}
