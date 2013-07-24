/*
 * HalApm1.cpp
 *
 *  Created on: Jun 16, 2013
 *      Author: helios
 */

#include "HalPhoneDrohne.h"

namespace phonedrohne {

	HalPhoneDrohne::HalPhoneDrohne() {
		issr = new ISRRegistry();
		gpio = new GPIO();
		issr->unregister_signal(0);
		gpio->channel(1);
		rcInput = new RCInput(gpio, issr);
		rcInput->init();
		rcOutput = new RCOutput(gpio);
		rcOutput->init();
	}

	HalPhoneDrohne::~HalPhoneDrohne() {
		delete issr;
		delete gpio;
		delete rcInput;
		delete rcOutput;
	}
	unsigned short HalPhoneDrohne::getPmw(const unsigned char channel) const {
		if (channel >= IN0 && channel <= IN7) {
			return rcInput->read(channel);
		}
		if (channel >= OUT0 && channel <= OUT7) {
			return rcOutput->read(channel - OUT0);
		}
		return 0;
	}

	void HalPhoneDrohne::setPmw(const unsigned char channel, const unsigned short pmw) {
		if (channel >= OUT0 && channel <= OUT7 && pmw >= 1000 && pmw <= 2000) {
			rcOutput->write(channel - OUT0, pmw);
		}
	}

} /* namespace phonedrohne */
