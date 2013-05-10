package ch.sharpsoft.quaternion;

public class Quaternion {

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
		return new Quaternion((float) (w / norm), (float) (x / norm),
				(float) (y / norm), (float) (z / norm));
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

	public Vector3 multiply(final Vector3 vec) {
		Quaternion vecQuat = new Quaternion(0.0f, vec.getX(), vec.getY(),
				vec.getZ());
		Quaternion resQuat = multiply(vecQuat).multiply(conjugate());
		return (new Vector3(resQuat.x, resQuat.y, resQuat.z));
	}

	public static Quaternion fromAxis(Vector3 v, float angle) {
		Vector3 vn = v.normalize();

		float sinAngle = (float) Math.sin(angle / 2);

		float x = (vn.getX() * sinAngle);
		float y = (vn.getY() * sinAngle);
		float z = (vn.getZ() * sinAngle);
		float w = (float) Math.cos(angle / 2);
		return new Quaternion(w, x, y, z);
	}

	/**
	 * 
	 * @param pitch
	 *            in radiants
	 * @param yaw
	 *            in radiants
	 * @param roll
	 *            in radiants
	 * @return
	 */
	public static Quaternion fromEuler(float pitch, float yaw, float roll) {
		// Basically we create 3 Quaternions, one for pitch, one for yaw, one
		// for roll
		// and multiply those together.
		// the calculation below does the same, just shorter

		float p = (float) (pitch / 2.0);
		float y = (float) (yaw / 2.0);
		float r = (float) (roll / 2.0);

		float sinp = (float) Math.sin(p);
		float siny = (float) Math.sin(y);
		float sinr = (float) Math.sin(r);
		float cosp = (float) Math.cos(p);
		float cosy = (float) Math.cos(y);
		float cosr = (float) Math.cos(r);

		float _x = sinr * cosp * cosy - cosr * sinp * siny;
		float _y = cosr * sinp * cosy + sinr * cosp * siny;
		float _z = cosr * cosp * siny - sinr * sinp * cosy;
		float _w = cosr * cosp * cosy + sinr * sinp * siny;

		return new Quaternion(_w, _x, _y, _z).normalize();
	}

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
		return "Q [w=" + String.format("%.4f", w) + ", x="
				+ String.format("%.4f", x) + ", y=" + String.format("%.4f", y)
				+ ", z=" + String.format("%.4f", z) + "]";
	}

	public float[] getFloatArray() {
		return new float[]{w,x,y,z};
	}	
	
	public float[] getFloatArrayXYZW() {
		return new float[]{x,y,z,w};
	}

}
