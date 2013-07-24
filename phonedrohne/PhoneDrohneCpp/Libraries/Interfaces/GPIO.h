#ifndef __AP_HAL_AVR_GPIO_H__
#define __AP_HAL_AVR_GPIO_H__

#include <avr/io.h>
#include <Interrupts/Proc.h>
#include <pins_arduino.h>
#include "DigitalSource.h"

#define GPIO_INPUT  0
#define GPIO_OUTPUT 1
#define GPIO_INTERRUPT_LOW 0
#define GPIO_INTERRUPT_HIGH 1
#define GPIO_INTERRUPT_FALLING 2
#define GPIO_INTERRUPT_RISING 3

// Get the bit location within the hardware port of the given virtual pin.
// This comes from the pins_*.c file for the active board configuration.

#define analogInPinToBit(P) (P)

// Get the bit location within the hardware port of the given virtual pin.
// This comes from the pins_*.c file for the active board configuration.
//
// These perform slightly better as macros compared to inline functions
//
#define digitalPinToPort(P) ( pgm_read_byte( digital_pin_to_port_PGM + (P) ) )
#define digitalPinToBitMask(P) ( pgm_read_byte( digital_pin_to_bit_mask_PGM + (P) ) )
#define digitalPinToTimer(P) ( pgm_read_byte( digital_pin_to_timer_PGM + (P) ) )
#define analogInPinToBit(P) (P)
#define portOutputRegister(P) ( (volatile uint8_t *)( pgm_read_word( port_to_output_PGM + (P))) )
#define portInputRegister(P) ( (volatile uint8_t *)( pgm_read_word( port_to_input_PGM + (P))) )
#define portModeRegister(P) ( (volatile uint8_t *)( pgm_read_word( port_to_mode_PGM + (P))) )

class GPIO {
public:
	GPIO();
	virtual ~GPIO();
	void pinMode(uint8_t pin, uint8_t output);
	uint8_t read(uint8_t pin);
	void write(uint8_t pin, uint8_t value);
	DigitalSource* channel(uint16_t n);

	/* Interrupt interface: */
	bool attach_interrupt(uint8_t interrupt_num, Proc p, uint8_t mode);
};

#endif // __AP_HAL_AVR_GPIO_H__
