#ifndef __AP_HAL_AVR_RC_INPUT_H__
#define __AP_HAL_AVR_RC_INPUT_H__

#define AVR_RC_INPUT_NUM_CHANNELS 8

#define RC_INPUT_MIN_PULSEWIDTH 900
#define RC_INPUT_MAX_PULSEWIDTH 2100

#include <Interfaces/GPIO.h>
#include <Interrupts/ISRRegistry.h>
#include <Interrupts/Proc.h>

class RCInput {
private:
	GPIO *gpio;
	ISRRegistry *isrregistry;

	/* override state */
	unsigned short _override[AVR_RC_INPUT_NUM_CHANNELS];

public:
	RCInput(GPIO *gpio, ISRRegistry *isrregistry) :
			gpio(gpio), isrregistry(isrregistry) {
	}
	virtual ~RCInput() {
	}
	/**
	 * Call init from the platform hal instance init, so that both the type of
	 * the RCInput implementation and init argument (e.g. ISRRegistry) are
	 * known to the programmer. (Its too difficult to describe this dependency
	 * in the C++ type system.)
	 */
	virtual void init();

	/**
	 * Return the number of currently valid channels.
	 * Typically 0 (no valid radio channels) or 8 (implementation-defined)
	 * Could be less than or greater than 8 depending on your incoming radio
	 * or PPM stream
	 */
	virtual unsigned char valid();

	/* Read a single channel at a time */
	virtual unsigned short read(unsigned char ch);

	/* Read an array of channels, return the valid count */
	virtual unsigned char read(unsigned short* periods, unsigned char len);

	/**
	 * Overrides: these are really grody and don't belong here but we need
	 * them at the moment to make the port work.
	 * case v of:
	 *  v == -1 -> no change to this channel
	 *  v == 0  -> do not override this channel
	 *  v > 0   -> set v as override.
	 */

	/* set_overrides: array starts at ch 0, for len channels */
	virtual bool set_overrides(short *overrides, unsigned char len);
	/* set_override: set just a specific channel */
	virtual bool set_override(unsigned char channel, short override);
	/* clear_overrides: equivelant to setting all overrides to 0 */
	virtual void clear_overrides();
};

#endif // __AP_HAL_AVR_RC_INPUT_H__
