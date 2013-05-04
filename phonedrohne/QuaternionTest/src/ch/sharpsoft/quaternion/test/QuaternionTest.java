package ch.sharpsoft.quaternion.test;

import ch.sharpsoft.quaternion.Quaternion;
import ch.sharpsoft.quaternion.Vector3;
import junit.framework.TestCase;

public class QuaternionTest extends TestCase {

	public void testQuaternionFloatArray() {
		Quaternion q = new Quaternion(new float[] { 1f, 2f, 3f, 4f });
		assertEquals(q.getW(),1f);
		assertEquals(q.getX(),2f);
		assertEquals(q.getY(),3f);
		assertEquals(q.getZ(),4f);
	}

	public void testQuaternionFloatFloatFloatFloat() {
		Quaternion q = new Quaternion(1f, 2f, 3f, 4f );
		assertEquals(q.getW(),1f);
		assertEquals(q.getX(),2f);
		assertEquals(q.getY(),3f);
		assertEquals(q.getZ(),4f);
	}

	public void testNorm() {
		Quaternion q = new Quaternion(1f, 2f, 3f, 4f );
		double norm = q.norm();
		assertEquals(Math.sqrt(1+4+9+16), norm);
	}

	public void testConjugate() {
		Quaternion q = new Quaternion(1f, 2f, 3f, 4f ).conjugate();
		assertEquals(q.getW(),1f);
		assertEquals(q.getX(),-2f);
		assertEquals(q.getY(),-3f);
		assertEquals(q.getZ(),-4f);
	}

	public void testPlus() {
		Quaternion q = new Quaternion(1f, 2f, 3f, 4f ).plus(new Quaternion(2, 3, 4, 5));
		assertEquals(q.getW(),3f);
		assertEquals(q.getX(),5f);
		assertEquals(q.getY(),7f);
		assertEquals(q.getZ(),9f);
	}

	public void testMultiplyQuaternion() {
		float qCos = (float) Math.cos(Math.PI/4);
		float qSin = (float) Math.sin(Math.PI/4);
		Quaternion qx = new Quaternion(qCos, qSin, 0, 0);
		Quaternion qz = new Quaternion(qCos, 0, 0, qSin);
		Quaternion qxz = qx.multiply(qz);
		Quaternion qzx = qz.multiply(qx);

		assertEquals(1f/2,qxz.getW(),0.00001f);
		assertEquals(1f/2,qxz.getX(),0.00001f);
		assertEquals(-1f/2,qxz.getY(),0.00001f);
		assertEquals(1f/2,qxz.getZ(),0.00001f);
		
		assertEquals(1f/2,qzx.getW(),0.00001f);
		assertEquals(1f/2,qzx.getX(),0.00001f);
		assertEquals(1f/2,qzx.getY(),0.00001f);
		assertEquals(1f/2,qzx.getZ(),0.00001f);
	}

	public void testNormalize() {
		Quaternion q = new Quaternion(1, 1, 1, 1);
		assertEquals(Math.sqrt(4),q.norm(),0.0000001f);
		assertEquals(1,q.normalize().norm(),0.0000001f);
	}

	public void testMultiplyVector3() {
		Vector3 v = new Vector3(1, 1, 0);
		
		float qCos = (float) Math.cos(Math.PI/4);
		float qSin = (float) Math.sin(Math.PI/4);
		Quaternion qx = new Quaternion(qCos, qSin, 0, 0);
		Quaternion qz = new Quaternion(qCos, 0, 0, qSin);
		Quaternion qxz = qx.multiply(qz);
		Quaternion qzx = qz.multiply(qx);

		Vector3 vxz = qxz.multiply(v);
		Vector3 vzx = qzx.multiply(v);
		assertEquals(-1,vxz.getX(),0.00001f);
		assertEquals(0,vxz.getY(),0.00001f);
		assertEquals(1,vxz.getZ(),0.00001f);
		
		assertEquals(0,vzx.getX(),0.00001f);
		assertEquals(1,vzx.getY(),0.00001f);
		assertEquals(1,vzx.getZ(),0.00001f);
	}

	public void testFromAxis() {
		//TODO
	}

	public void testFromEuler() {
		//TODO
	}
}
