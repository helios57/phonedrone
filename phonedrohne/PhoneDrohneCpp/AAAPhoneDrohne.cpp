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
#include "Pins.h"

long timer = 0;
AndroidAccessory adk("Sharpsoft", "PhoneDrone", "Phone Drone ADK by 3DRobotics",
		"1.0", "http://www.android.com", "0000000012345678");

#define BUFFER_LENGTH 4
uint8_t buf[BUFFER_LENGTH];

uint16_t len;
int bytes_sent;

void setup() {
	Serial.begin(115200);

	digitalWrite(4, LOW); //PG5 por esto
	pinMode(4, INPUT);

	Init_PWM1();      //OUT2&3
	Init_PWM3();      //OUT6&7
	Init_PWM5();      //OUT0&1
	Init_PPM_PWM4();  //OUT4&5
	adk.powerOn();
	//initialize on low level
	for (int i = 0; i <= 7; i++) {
		OutputCh(i, 1000);
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
		if ((millis() - timer) >= 500) {
			timer = millis();
			for (int i = 0; i <= 7; i++) {
				int pmw = (uint16_t) InputCh(i);
				uint8_t buffer[4];
				buffer[0] = 0;
				buffer[1] = i;
				buffer[2] = (uint8_t) (pmw >> 8);
				buffer[3] = (uint8_t) pmw;
				len = 4;
				adk.write(buffer, len);
			}
		}
		int recieved = adk.read(buf, sizeof(buf), 1);
		if (recieved > 0) {
			for (int i = 0; i < recieved; ++i) {
				uint8_t action = buf[0];
				uint8_t ch = buf[1];
				uint16_t pmw = (buf[2] << 8) + buf[3];
				if (action == 1) {
					OutputCh(ch, pmw);
				}
				break;
			}
		}
	}
}
