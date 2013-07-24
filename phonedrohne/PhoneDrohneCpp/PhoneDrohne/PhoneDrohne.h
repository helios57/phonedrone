/*
 * PhoneDrohne.h
 *
 *  Created on: Jul 24, 2013
 *      Author: helios
 */

#ifndef PHONEDROHNE_H_
#define PHONEDROHNE_H_
#include <AndroidAccessory.h>
#include <Hal/HalPhoneDrohne.h>

namespace phonedrohne {
	class PhoneDrohne {
	private:
		unsigned char buf[4];
		unsigned long timer;
		unsigned short len;
		unsigned short bytes_sent;
		AndroidAccessory* adk;
		HalPhoneDrohne* hal;
	public:
		PhoneDrohne();
		virtual ~PhoneDrohne();
		int main();
		void setup();
		void loop();
	};

} /* namespace phonedrohne */
#endif /* PHONEDROHNE_H_ */
