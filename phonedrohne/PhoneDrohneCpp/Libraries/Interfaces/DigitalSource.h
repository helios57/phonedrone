/*
 * DigitalSource.h
 *
 *  Created on: Jun 16, 2013
 *      Author: helios
 */

#ifndef DIGITALSOURCE_H_
#define DIGITALSOURCE_H_

class DigitalSource {
	private:
		const uint8_t _bit;
		const uint8_t _port;

	public:
		DigitalSource(uint8_t bit, uint8_t port) :
				_bit(bit), _port(port) {
		}
		virtual ~DigitalSource();
		void mode(uint8_t output);
		uint8_t read();
		void write(uint8_t value);
};

#endif /* DIGITALSOURCE_H_ */
