package ch.sharpsoft.quaternion;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class GLRenderer implements GLSurfaceView.Renderer {

	Square square;
	private final static String vertexShaderCode = "attribute vec4 vPosition;"
			+ "void main() {" + "  gl_Position = vPosition;" + "}";

	private final static String fragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "  gl_FragColor = vColor;" + "}";

	private int mProgram;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;
    
	@Override
	public void onDrawFrame(GL10 gl) { // Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		
		// Set the camera position (View matrix)
		Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		
//		Matrix.setRotateM(mRotationMatrix, 0, 10, 0, 0, -1.0f);
		
		Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix,0);

		// Draw square
		square.draw(mMVPMatrix);
	}

	public void setRotationMatrix(float[] m44){
		for(int i = 0;i<16;i++){
			mRotationMatrix[i] = m44[i];
		}
	}
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Adjust the viewport based on geometry changes,
		// such as screen rotation
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);
		square = new Square();
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram(); // create empty OpenGL ES Program
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
														// to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
															// shader to program
		GLES20.glLinkProgram(mProgram); // creates OpenGL ES program executables

	}

	public static int loadShader(int type, String shaderCode) {

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}
}
