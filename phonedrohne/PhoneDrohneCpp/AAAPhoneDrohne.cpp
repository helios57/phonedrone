/*
 * AAAPhoneDrohne.cpp
 *
 *  Created on: Jan 20, 2013
 *      Author: helios
 */

#include <Arduino.h>
#include <AndroidAccessory.h>
#include <Max3421e.h>
#include <Usb.h>
#include <mavlink.h>
#include <phonedrohne.h>
#include "Pins.h"

long timer = 0;
long timer2 = 0;
byte Status = 0;

int offset0 = 0;
int offset1 = 0;

int loopCount = 0;
AndroidAccessory adk("Sharpsoft", "PhoneDrone", "Phone Drone ADK by 3DRobotics",
		"1.0", "http://www.android.com", "0000000012345678");

int firstSensor = 0;    // first analog sensor
int secondSensor = 0;   // second analog sensor
int thirdSensor = 0;    // digital sensor
int inByte = 0;         // incoming serial byte

#define BUFFER_LENGTH 2041
uint8_t buf[BUFFER_LENGTH];

mavlink_message_t msg;
uint16_t len;
int bytes_sent;

void establishContact() {
	while (Serial.available() <= 0) {
		Serial.println("0,0,0");   // send an initial string
		delay(300);
	}
}

void setup() {
	Serial.begin(115200);
//	while (!Serial) {
//		; // wait for serial port to connect. Needed for Leonardo only
//	}
//	establishContact(); // send a byte to establish contact until receiver responds

	digitalWrite(4, LOW); //PG5 por esto
	pinMode(4, INPUT);

	Init_PWM1();      //OUT2&3
	Init_PWM3();      //OUT6&7
	Init_PWM5();      //OUT0&1
	Init_PPM_PWM4();  //OUT4&5

	Serial.println("\r\nADK demo start");
	adk.powerOn();
	if (adk.isConnected()) {
		Serial.println("Connected");
	} else {
		Serial.println("Not Connected");
	}
}

void loop() {

	if (Serial.available() > 0) {
		String readString = Serial.readString();
		if (readString.startsWith("o")) {
			Serial.print("Set Output 0 to");
			int i = readString.substring(1).toInt();
			Serial.print(i);
			OutputCh(0, i);
			Serial.println();
		}
		for (int i = 0; i <= 7; i++) {
			Serial.print("Ch");
			Serial.print(i);
			Serial.print(": ");
			Serial.print(InputCh(i));
			Serial.println();
		}
	}

	if (adk.isConnected()) {
		if ((millis() - timer) >= 250) {
			timer = millis();

			Serial.print(" connected");
			mavlink_msg_line_value_pack(1, 1, &msg, PD_LINE_IN_0, 0);
			uint16_t len = mavlink_msg_to_send_buffer(buf, &msg);

			uint16_t bytes_sent = adk.write(buf, len);

			if (bytes_sent != len) {
				Serial.println("Nicht alles gesendet");
			}

			memset(buf, 0, BUFFER_LENGTH);
			int recieved = adk.read(buf, sizeof(buf), 1);

			if (recieved > 0) {
				Serial.println("Recieved");

				mavlink_message_t msg;
				mavlink_status_t status;

				for (int i = 0; i < recieved; ++i) {
					if (mavlink_parse_char(MAVLINK_COMM_0, buf[i], &msg,
							&status)) {
						break;
					}
				}
				printf("\n");
			}
			memset(buf, 0, BUFFER_LENGTH);
		}
	}
}
