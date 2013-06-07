package ch.sharpsoft.quaternion.v2;

import javax.microedition.khronos.opengles.GL10;

import ch.sharpsoft.quaternion.R;
import ch.sharpsoft.quaternion.util.Quaternion;
import rajawali.BaseObject3D;
import rajawali.animation.Animation3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.EllipticalOrbitAnimation3D;
import rajawali.animation.EllipticalOrbitAnimation3D.OrbitDirection;
import rajawali.animation.RotateAnimation3D;
import rajawali.lights.PointLight;
import rajawali.materials.SkyboxMaterial;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.math.Vector3;
import rajawali.math.Vector3.Axis;
import rajawali.parser.AParser.ParsingException;
import rajawali.parser.ObjParser;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.opengl.EGLConfig;

public class RajawaliLoadModelRenderer extends RajawaliRenderer {
	private PointLight mLight;
	private BaseObject3D mObjectGroup;
	private Animation3D mCameraAnim, mLightAnim;

	public RajawaliLoadModelRenderer(Context context) {
		super(context);
		setFrameRate(60);
	}

	public void setRotation(Quaternion q) {
		rajawali.math.Quaternion qq = new rajawali.math.Quaternion(q.getW(), q.getX(), q.getY(), q.getZ());
		if (mObjectGroup != null) {
			mObjectGroup.setOrientation(qq);
		}
	}

	protected void initScene() {
		try {
			getCurrentScene().setSkybox(R.drawable.posz, R.drawable.posx,
					R.drawable.negz, R.drawable.negx, R.drawable.posy,
					R.drawable.negy);
		} catch (TextureException e1) {
			e1.printStackTrace();
		}
		mLight = new PointLight();
		mLight.setPosition(0, 0, 10);
		mLight.setPower(8);
		mLight.setColor(1f, 1f, 1f);
		getCurrentCamera().setZ(6);
		getCurrentCamera().setLookAt(0, 0, 1);
		getCurrentCamera().setPosition(0, 0, 5);

		ObjParser objParser = new ObjParser(mContext.getResources(),
				mTextureManager, R.raw.model);
		try {
			objParser.parse();
			mObjectGroup = objParser.getParsedObject();
			mObjectGroup.addLight(mLight);
			addChild(mObjectGroup);

//			mCameraAnim = new RotateAnimation3D(Axis.X, 360);
//			mCameraAnim.setDuration(8000);
//			mCameraAnim.setRepeatMode(RepeatMode.INFINITE);
//			mCameraAnim.setTransformable3D(mObjectGroup);
		} catch (ParsingException e) {
			e.printStackTrace();
		}

		mLightAnim = new EllipticalOrbitAnimation3D(new Vector3(), new Vector3(
				0, 10, 0), Vector3.getAxisVector(Axis.Z), 0, 360,
				OrbitDirection.CLOCKWISE);

		mLightAnim.setDuration(3000);
		mLightAnim.setRepeatMode(RepeatMode.INFINITE);
		mLightAnim.setTransformable3D(mLight);

//		registerAnimation(mCameraAnim);
		registerAnimation(mLightAnim);

//		mCameraAnim.play();
		mLightAnim.play();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// ((RajawaliExampleActivity) mContext).showLoader();
		// super.onSurfaceCreated(gl, config);
		// ((RajawaliExampleActivity) mContext).hideLoader();
	}

	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
	}
}