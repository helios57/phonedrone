#ifndef __AP_HAL_AVR_RC_OUTPUT_H__
#define __AP_HAL_AVR_RC_OUTPUT_H__

/* Define the CH_n names, indexed from 1, if we don't have them already */
#ifndef CH_1
#define CH_1 0
#define CH_2 1
#define CH_3 2
#define CH_4 3
#define CH_5 4
#define CH_6 5
#define CH_7 6
#define CH_8 7
#define CH_9 8
#define CH_10 9
#define CH_11 10
#endif

#include <Interfaces/GPIO.h>

class RCOutput {
private:
	GPIO *gpio;
	unsigned short _timer_period(unsigned short speed_hz);
public:
	RCOutput(GPIO *gpio) :
			gpio(gpio) {
	}
	~RCOutput() {
	}
	void init();

	/* Output freq (1/period) control */
	void set_freq(unsigned int chmask, unsigned short freq_hz);
	unsigned short get_freq(unsigned char ch);

	/* Output active/highZ control, either by single channel at a time
	 * or a mask of channels */
	void enable_ch(unsigned char ch);
	void enable_mask(unsigned int chmask);

	void disable_ch(unsigned char ch);
	void disable_mask(unsigned int chmask);

	/* Output, either single channel or bulk array of channels */
	void write(unsigned char ch, unsigned short period_us);
	void write(unsigned char ch, unsigned short* period_us, unsigned char len);

	/* Read back current output state, as either single channel or
	 * array of channels. */
	unsigned short read(unsigned char ch);
	void read(unsigned short* period_us, unsigned char len);
};

#endif // __AP_HAL_AVR_RC_OUTPUT_H__
