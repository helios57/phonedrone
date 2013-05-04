package ch.sharpsoft.quaternion;

public class Quaternion {
	
	private final static float PIOVER180 = (float)Math.PI/180;

	private final float w, x, y, z;

	public Quaternion(float[] q) {
		this.w = q[0];
		this.x = q[1];
		this.y = q[2];
		this.z = q[3];
	}

	public Quaternion(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double norm() {
		return Math.sqrt(w * w + x * x + y * y + z * z);
	}

	public Quaternion normalize() {
		final double norm = norm();
		return new Quaternion((float)(w/norm),(float)(x/norm),(float)(y/norm),(float)(z/norm));
	}
	public Quaternion conjugate() {
		return new Quaternion(w, -x, -y, -z);
	}

	public Quaternion plus(Quaternion b) {
		return new Quaternion(w + b.w, x + b.x, y + b.y, z + b.z);
	}

	// return a new Quaternion whose value is (this * b)
	public Quaternion multiply(Quaternion b) {
		float w0 = w * b.w - x * b.x - y * b.y - z * b.z;
		float x0 = w * b.x + x * b.w + y * b.z - z * b.y;
		float y0 = w * b.y - x * b.z + y * b.w + z * b.x;
		float z0 = w * b.z + x * b.y - y * b.x + z * b.w;
		return new Quaternion(w0, x0, y0, z0);
	}
	
	public Vector3 multiply(final Vector3 vec)
	{
		Quaternion vecQuat = new Quaternion(0.0f,vec.getX(),vec.getY(),vec.getZ());
		Quaternion resQuat = multiply(vecQuat).multiply(conjugate());
		return (new Vector3(resQuat.x, resQuat.y, resQuat.z));
	}
	
	public static Quaternion fromAxis(Vector3 v, float angle)
	{
		Vector3 vn  = v.normalize();
	 
		float sinAngle = (float)Math.sin(angle/2);
	 
		float x = (vn.getX() * sinAngle);
		float y = (vn.getY() * sinAngle);
		float z = (vn.getZ() * sinAngle);
		float w = (float)Math.cos(angle/2);
		return new Quaternion(w,x,y,z);
	}
	
	public static Quaternion fromEuler(float pitch, float yaw, float roll)
	{
		// Basically we create 3 Quaternions, one for pitch, one for yaw, one for roll
		// and multiply those together.
		// the calculation below does the same, just shorter
	 
		
		float p = (float) (pitch * PIOVER180 / 2.0);
		float y = (float)(yaw * PIOVER180 / 2.0);
		float r =(float) (roll * PIOVER180 / 2.0);
	 
		float sinp = (float)Math.sin(p);
		float siny = (float)Math.sin(y);
		float sinr = (float)Math.sin(r);
		float cosp = (float)Math.cos(p);
		float cosy = (float)Math.cos(y);
		float cosr = (float)Math.cos(r);
	 
		float _x = sinr * cosp * cosy - cosr * sinp * siny;
		float _y = cosr * sinp * cosy + sinr * cosp * siny;
		float _z = cosr * cosp * siny - sinr * sinp * cosy;
		float _w = cosr * cosp * cosy + sinr * sinp * siny;
	 
		return new Quaternion(_w,_x,_y,_z).normalize();
	}
	
//	Matrix4 Quaternion::getMatrix() const
//	{
//		float x2 = x * x;
//		float y2 = y * y;
//		float z2 = z * z;
//		float xy = x * y;
//		float xz = x * z;
//		float yz = y * z;
//		float wx = w * x;
//		float wy = w * y;
//		float wz = w * z;
//	 
//		// This calculation would be a lot more complicated for non-unit length quaternions
//		// Note: The constructor of Matrix4 expects the Matrix in column-major format like expected by
//		//   OpenGL
//		return Matrix4( 1.0f - 2.0f * (y2 + z2), 2.0f * (xy - wz), 2.0f * (xz + wy), 0.0f,
//				2.0f * (xy + wz), 1.0f - 2.0f * (x2 + z2), 2.0f * (yz - wx), 0.0f,
//				2.0f * (xz - wy), 2.0f * (yz + wx), 1.0f - 2.0f * (x2 + y2), 0.0f,
//				0.0f, 0.0f, 0.0f, 1.0f)
//	}
		
	public float getW() {
		return w;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quaternion other = (Quaternion) obj;
		if (Float.floatToIntBits(w) != Float.floatToIntBits(other.w))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Quaternion [w=" + String.format("%.4f", w) + ", x="
				+ String.format("%.4f", x) + ", y=" + String.format("%.4f", y)
				+ ", z=" + String.format("%.4f", z) + "]";
	}

}
