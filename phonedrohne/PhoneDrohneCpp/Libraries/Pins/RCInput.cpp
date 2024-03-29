#include <avr/io.h>
#include <avr/interrupt.h>

#include "RCInput.h"
#include <Interrupts/ISRRegistry.h>
#include <Interrupts/Proc.h>

volatile unsigned short _pulse_capt[AVR_RC_INPUT_NUM_CHANNELS];
volatile unsigned char _valid = 0;
volatile unsigned short icr4_prev = 0;
volatile unsigned char channel_ctr = 0;

/* private callback for input capture ISR */
void _timer4_capt_cb() {

	const unsigned short icr4_current = ICR4;
	unsigned short pulse_width;
	if (icr4_current < icr4_prev) {
		/* ICR4 rolls over at TOP=40000 */
		pulse_width = icr4_current + 40000 - icr4_prev;
	} else {
		pulse_width = icr4_current - icr4_prev;
	}

	if (pulse_width > 8000) {
		/* sync pulse detected */
		channel_ctr = 0;
	} else {
		if (channel_ctr < AVR_RC_INPUT_NUM_CHANNELS) {
			_pulse_capt[channel_ctr] = pulse_width;
			channel_ctr++;
			if (channel_ctr == AVR_RC_INPUT_NUM_CHANNELS) {
				_valid = AVR_RC_INPUT_NUM_CHANNELS;
			}
		}
	}
	icr4_prev = icr4_current;
}

void RCInput::init() {
	isrregistry->register_signal(ISR_REGISTRY_TIMER4_CAPT, _timer4_capt_cb);

	/* initialize overrides */
	clear_overrides();
	/* Arduino pin 49 is ICP4 / PL0,  timer 4 input capture */
	gpio->pinMode(49, GPIO_INPUT);
	/**
	 * WGM: 1 1 1 1. Fast WPM, TOP is in OCR4A
	 * COM all disabled
	 * CS41: prescale by 8 => 0.5us tick
	 * ICES4: input capture on rising edge
	 * OCR4A: 40000, 0.5us tick => 2ms period / 50hz freq for outbound
	 * fast PWM.
	 */TCCR4A = _BV(WGM40) | _BV(WGM41);
	TCCR4B = _BV(WGM43) | _BV(WGM42) | _BV(CS41) | _BV(ICES4);
	OCR4A = 40000;

	/* OCR4B and OCR4C will be used by RCOutput_APM1. init to nil output */OCR4B = 0xFFFF;
	OCR4C = 0xFFFF;

	/* Enable input capture interrupt */
	TIMSK4 |= _BV(ICIE4);
}

unsigned char RCInput::valid() {
	return _valid;
}

/* constrain captured pulse to be between min and max pulsewidth. */
inline unsigned short constrain_pulse(unsigned short p) {
	if (p > RC_INPUT_MAX_PULSEWIDTH)
		return RC_INPUT_MAX_PULSEWIDTH;
	if (p < RC_INPUT_MIN_PULSEWIDTH)
		return RC_INPUT_MIN_PULSEWIDTH;
	return p;
}

unsigned short RCInput::read(unsigned char ch) {
	/* constrain ch */
	if (ch >= AVR_RC_INPUT_NUM_CHANNELS)
		return 0;
	/* grab channel from isr's memory in critical section*/cli();
	unsigned short capt = _pulse_capt[ch];
	sei();
	_valid = 0;
	/* scale _pulse_capt from 0.5us units to 1us units. */
	unsigned short pulse = constrain_pulse(capt >> 1);
	/* Check for override */
	unsigned short over = _override[ch];
	return (over == 0) ? pulse : over;
}

unsigned char RCInput::read(unsigned short* periods, unsigned char len) {
	/* constrain len */
	if (len > AVR_RC_INPUT_NUM_CHANNELS) {
		len = AVR_RC_INPUT_NUM_CHANNELS;
	}
	/* grab channels from isr's memory in critical section */cli();
	for (unsigned char i = 0; i < len; i++) {
		periods[i] = _pulse_capt[i];
	}
	sei();
	/* Outside of critical section, do the math (in place) to scale and
	 * constrain the pulse. */
	for (unsigned char i = 0; i < len; i++) {
		/* scale _pulse_capt from 0.5us units to 1us units. */
		periods[i] = constrain_pulse(periods[i] >> 1);
		/* check for override */
		if (_override[i] != 0) {
			periods[i] = _override[i];
		}
	}
	unsigned char v = _valid;
	_valid = 0;
	return v;
}

bool RCInput::set_overrides(short *overrides, unsigned char len) {
	bool res = false;
	for (unsigned char i = 0; i < len; i++) {
		res |= set_override(i, overrides[i]);
	}
	return res;
}

bool RCInput::set_override(unsigned char channel, short override) {
	if (override < 0)
		return false; /* -1: no change. */
	if (channel < AVR_RC_INPUT_NUM_CHANNELS) {
		_override[channel] = override;
		if (override != 0) {
			_valid = 1;
			return true;
		}
	}
	return false;
}

void RCInput::clear_overrides() {
	for (unsigned char i = 0; i < AVR_RC_INPUT_NUM_CHANNELS; i++) {
		_override[i] = 0;
	}
}
