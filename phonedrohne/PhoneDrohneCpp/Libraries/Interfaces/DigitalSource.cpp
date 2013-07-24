/*
 * AVRDigitalSource.cpp
 *
 *  Created on: Jun 16, 2013
 *      Author: helios
 */

#include <avr/interrupt.h>
#include "GPIO.h"
#include "DigitalSource.h"

DigitalSource::~DigitalSource() {
}

void DigitalSource::mode(uint8_t output) {
	const uint8_t bit = _bit;
	const uint8_t port = _port;

	volatile uint8_t* reg;
	reg = portModeRegister(port);

	if (output == GPIO_INPUT) {
		uint8_t oldSREG = SREG;
		cli();
		*reg &= ~bit;
		SREG = oldSREG;
	} else {
		uint8_t oldSREG = SREG;
		cli();
		*reg |= bit;
		SREG = oldSREG;
	}
}

uint8_t DigitalSource::read() {
	const uint8_t bit = _bit;
	const uint8_t port = _port;
	if (*portInputRegister(port) & bit) return 1;
	return 0;
}

void DigitalSource::write(uint8_t value) {
	const uint8_t bit = _bit;
	const uint8_t port = _port;
	volatile uint8_t* out;
	out = portOutputRegister(port);

	uint8_t oldSREG = SREG;
	cli();

	if (value == 0) {
		*out &= ~bit;
	} else {
		*out |= bit;
	}

	SREG = oldSREG;
}
