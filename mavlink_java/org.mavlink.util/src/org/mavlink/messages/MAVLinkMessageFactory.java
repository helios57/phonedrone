/**
 * Generated class : MAVLinkMessageFactory
 * DO NOT MODIFY!
 **/
package org.mavlink.messages;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.IMAVLinkMessage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.mavlink.messages.phonedrohne.msg_line_value;

/**
 * Class MAVLinkMessageFactory Generate MAVLink message classes from byte array
 **/
public class MAVLinkMessageFactory implements IMAVLinkMessage,
		IMAVLinkMessageID {
	public static MAVLinkMessage getMessage(int msgid, int sysId,
			int componentId, byte[] rawData) throws IOException {
		MAVLinkMessage msg = null;
		ByteBuffer dis = ByteBuffer.wrap(rawData)
				.order(ByteOrder.LITTLE_ENDIAN);
		switch (msgid) {
		case MAVLINK_MSG_ID_LINE_VALUE:
			msg = new msg_line_value(sysId, componentId);
			msg.decode(dis);
			break;
		default:
			System.out.println("Mavlink Factory Error : unknown MsgId : "
					+ msgid);
		}
		return msg;
	}
}
