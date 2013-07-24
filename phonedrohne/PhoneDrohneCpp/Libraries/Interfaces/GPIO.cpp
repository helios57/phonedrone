#include <stddef.h>
#include <avr/interrupt.h>
#include <avr/io.h>
#include <avr/common.h>

#include <pins_arduino.h>
#include "GPIO.h"

/* private-ish: only to be used from the appropriate interrupt */
Proc _interrupt_6;

SIGNAL(INT6_vect) {
	if (_interrupt_6) {
		_interrupt_6();
	}
}

GPIO::GPIO() {

}
GPIO::~GPIO() {

}

void GPIO::pinMode(uint8_t pin, uint8_t mode) {
	uint8_t bit = digitalPinToBitMask(pin);
	uint8_t port = digitalPinToPort(pin);
	volatile uint8_t *reg;

	if (port == NOT_A_PIN)
		return;

	// JWS: can I let the optimizer do this?
	reg = portModeRegister(port);

	if (mode == GPIO_INPUT) {
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

uint8_t GPIO::read(uint8_t pin) {
	uint8_t bit = digitalPinToBitMask(pin);
	uint8_t port = digitalPinToPort(pin);

	if (port == NOT_A_PIN)
		return 0;

	if (*portInputRegister(port) & bit)
		return 1;
	return 0;
}

void GPIO::write(uint8_t pin, uint8_t value) {
	uint8_t bit = digitalPinToBitMask(pin);
	uint8_t port = digitalPinToPort(pin);
	volatile uint8_t *out;

	if (port == NOT_A_PIN)
		return;

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

/* Implement GPIO Interrupt 6, used for MPU6000 data ready on APM2. */
bool GPIO::attach_interrupt(uint8_t interrupt_num, Proc proc, uint8_t mode) {
	/* Mode is to set the ISCn0 and ISCn1 bits.
	 * These correspond to the GPIO_INTERRUPT_ defs in AP_HAL.h */
	if (!((mode == 0) || (mode == 1) || (mode == 2) || (mode == 3)))
		return false;
	if (interrupt_num == 6) {
		uint8_t oldSREG = SREG;
		cli();
		_interrupt_6 = proc;
		/* Set the ISC60 and ICS61 bits in EICRB according to the value
		 * of mode. */EICRB = (EICRB & ~((1 << ISC60) | (1 << ISC61))) | (mode << ISC60);
		EIMSK |= (1 << INT6);
		SREG = oldSREG;
		return true;
	} else {
		return false;
	}
}

DigitalSource* GPIO::channel(uint16_t pin) {
	uint8_t bit = digitalPinToBitMask(pin);
	uint8_t port = digitalPinToPort(pin);
	if (port == NOT_A_PIN)
		return NULL;
	return new DigitalSource(bit, port);
}

