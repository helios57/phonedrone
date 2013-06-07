package ch.sharpsoft.quaternion.util;

import java.util.HashMap;
import java.util.Map;

import ch.sharpsoft.quaternion.v1.Vector3;

public class MotorPositions {

	private final Map<String, Vector3> motors = new HashMap<String, Vector3>();

	/**
	 * <br />
	 * +y-------c---------<br/ >
	 * -x---a---0---b--+x<br/ >
	 * -y-------d---------<br/ >
	 */
	public MotorPositions() {
		motors.put("A", new Vector3(-1f, 0f, 0f));
		motors.put("B", new Vector3(1f, 0f, 0f));
		motors.put("C", new Vector3(0f, 1f, 0f));
		motors.put("D", new Vector3(0f, -1f, 0f));
	}

	public MotorPositions(Map<String, Vector3> motors) {
		this.motors.putAll(motors);
	}

	public String getCalculatedThrust(Quaternion diff) {
		StringBuilder sb = new StringBuilder();
		for (String s : motors.keySet()) {
			Vector3 pos = motors.get(s);
			sb.append("Motor ").append(s).append(" pos ").append(pos).append(" thrust: ");
			sb.append(String.format("%.4f", getThrust(diff, pos)));
			sb.append("\n");
		}
		return sb.toString();
	}

	public float getThrust(Quaternion diff, Vector3 soll) {
		Vector3 ist = diff.multiply(soll);
		return ist.getZ();
	}
}
