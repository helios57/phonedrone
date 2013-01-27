/**
 * $Id$
 * $Date$
 *
 * ======================================================
 * Copyright (C) 2012 Guillaume Helle.
 * Project : MAVLINK Java
 * Module : org.drone.mavlink.library
 * File : org.mavlink.TestMavlinkReader.java
 * Author : ghelle
 *
 * ======================================================
 * HISTORY
 * Who       yyyy/mm/dd   Action
 * --------  ----------   ------
 * ghelle	31 ao�t 2012		Create
 * 
 * ====================================================================
 * Licence: ${licence}
 * ====================================================================
 */

package org.mavlink;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.PrintStream;

import org.mavlink.messages.MAVLinkMessage;

/**
 * @author ghelle
 * @version $Rev$
 *
 */
public class TestMavlinkReader {

    /**
     * @param args
     */
    public static void main(String[] args) {
        MAVLinkReader reader;
        String filename = "data/2012-09-17 12-56-35.rlog";
        //String filename = "data/V0.9.log";
        String fileOut = filename + "-resultat.out";
        try {
            System.setOut(new PrintStream(fileOut));
            DataInputStream dis = new DataInputStream(new FileInputStream(filename));
            //reader = new MAVLinkReader(dis, IMAVLinkMessage.MAVPROT_PACKET_START_V09);
            reader = new MAVLinkReader(dis, IMAVLinkMessage.MAVPROT_PACKET_START_V10);

            while (dis.available() > 0) {
                MAVLinkMessage msg = reader.getNextMessage();
                if (msg != null) {
                    //                    if (msg instanceof msg_param_value) {
                    //                        msg_param_value m = (msg_param_value) msg;
                    //                        System.out.println(msg.toString());
                    //                    }
                    System.out.println("SysId=" + msg.sysId + " CompId=" + msg.componentId + " seq=" + msg.sequence + " " + msg.toString());
                    /*
                    byte[] buf = msg.encode();
                    MAVLinkReader test = new MAVLinkReader(new DataInputStream(new ByteArrayInputStream(buf)), IMAVLinkMessage.MAVPROT_PACKET_START_V10);
                    MAVLinkMessage result = test.getNextMessage();
                    System.out.println(result.toString());
                    if (msg != result)
                        System.out.println("ERROR COMPARE");
                        */
                }
                /*
                    if (msg.messageType == IMAVLinkMessageID.MAVLINK_MSG_ID_HEARTBEAT) {
                        System.out.println("MAVLINK_MSG_ID_HEARTBEAT : " + msg);
                        msg_heartbeat hb_read = (msg_heartbeat) msg;
                        msg_heartbeat hb = new msg_heartbeat(hb_read.sysId, hb_read.componentId);
                        hb.sequence = hb_read.sequence;
                        hb.autopilot = hb_read.autopilot;
                        hb.base_mode = hb_read.base_mode;
                        hb.custom_mode = hb_read.custom_mode;
                        hb.mavlink_version = hb_read.mavlink_version;
                        hb.system_status = hb_read.system_status;
                        hb.type = hb_read.type;
                        byte[] result = hb.encode();
                        System.out.println("MAVLINK_MSG_ID_HEARTBEAT ==> " + hb);
                    }
                }
                */
            }
            dis.close();
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e);
        }

    }
}
