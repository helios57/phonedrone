/*
 * PhoneDrohne.cpp
 *
 *  Created on: Jul 24, 2013
 *      Author: helios
 */

#include "PhoneDrohne.h"
#include <AndroidAccessory.h>
#include <Max3421e.h>
#include <Usb.h>
#include <Hal/HalPhoneDrohne.h>

namespace phonedrohne {

	PhoneDrohne::PhoneDrohne() {
		timer = millis();
		len = 0;
		bytes_sent = 0;
		hal = new HalPhoneDrohne();
		adk = new AndroidAccessory("Sharpsoft", "PhoneDrone", "Phone Drone ADK by 3DRobotics", "1.0", "http://www.android.com", "0000000012345678");
	}

	PhoneDrohne::~PhoneDrohne() {
		delete hal;
		delete adk;
	}

	int PhoneDrohne::main() {
		this->setup();
		for (;;) {
			this->loop();
			if (serialEventRun)
				serialEventRun();
		}
		return 0;
	}
	void PhoneDrohne::setup() {
		adk->powerOn();
		Serial.begin(115200);
	}

	void PhoneDrohne::loop() {
		if (Serial.available() > 0) {
			String readString = Serial.readString();
			if (readString.startsWith("o")) {
				Serial.print("Set Output 0 to");
				int i = readString.substring(1).toInt();
				Serial.print(i);
				hal->setPmw(hal->OUT0, i);
				Serial.println();
			}
			for (int i = 0; i <= 7; i++) {
				Serial.print("Ch");
				Serial.print(i);
				Serial.print(": ");
				Serial.print(hal->getPmw(hal->IN0 + i));
				Serial.println();
			}
		}

		if (adk->isConnected()) {
			if ((millis() - timer) >= 500) {
				timer = millis();
				for (int i = 0; i <= 7; i++) {
					unsigned short pmw = hal->getPmw(hal->IN0 + i);
					uint8_t buffer[4];
					buffer[0] = 0;
					buffer[1] = i;
					buffer[2] = (unsigned char) (pmw >> 8);
					buffer[3] = (unsigned char) pmw;
					len = 4;
					adk->write(buffer, len);
				}
			}
			int recieved = adk->read(buf, sizeof(buf), 1);
			if (recieved > 0) {
				for (int i = 0; i < recieved; ++i) {
					unsigned char action = buf[0];
					unsigned char ch = buf[1];
					unsigned short pmw = (buf[2] << 8) + buf[3];
					if (action == 1) {
						hal->setPmw(hal->OUT0 + ch, pmw);
					}
					break;
				}
			}
		}
	}
} /* namespace phonedrohne */
