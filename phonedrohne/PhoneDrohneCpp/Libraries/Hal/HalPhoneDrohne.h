/*
 * HalApm1.h
 *
 *  Created on: Jun 16, 2013
 *      Author: helios
 */

#ifndef HALPHONEDROHNE_H_
#define HALPHONEDROHNE_H_

#include <Interfaces/GPIO.h>
#include <Interrupts/ISRRegistry.h>
#include "../Pins/RCInput.h"
#include "../Pins/RCOutput.h"

namespace phonedrohne {

	class HalPhoneDrohne {
	public:
		const static unsigned char IN0 = 0x0;
		const static unsigned char IN1 = 0x1;
		const static unsigned char IN2 = 0x2;
		const static unsigned char IN3 = 0x3;
		const static unsigned char IN4 = 0x4;
		const static unsigned char IN5 = 0x5;
		const static unsigned char IN6 = 0x6;
		const static unsigned char IN7 = 0x7;

		const static unsigned char OUT0 = 0x8;
		const static unsigned char OUT1 = 0x9;
		const static unsigned char OUT2 = 0xA;
		const static unsigned char OUT3 = 0xB;
		const static unsigned char OUT4 = 0xC;
		const static unsigned char OUT5 = 0xD;
		const static unsigned char OUT6 = 0xE;
		const static unsigned char OUT7 = 0xF;
	private:
		ISRRegistry *issr;
		GPIO *gpio;
		RCInput *rcInput;
		RCOutput *rcOutput;
	public:
		HalPhoneDrohne();
		~HalPhoneDrohne();
		/// @brief This method returns the current PMW for the given channel.
		/// @param channel Channel one of Hal::(IN0 - IN7 and OUT0-OUT7)
		/// @return The current PMW.
		unsigned short getPmw(const unsigned char channel) const;

		/// @brief This method sets the current PMW for the given channel.
		/// @param channel Channel one of Hal::(IN0 - IN7 and OUT0-OUT7)
		/// @param pmw The pmw to set
		void setPmw(const unsigned char channel, const unsigned short pmw);
	};

}
#endif
