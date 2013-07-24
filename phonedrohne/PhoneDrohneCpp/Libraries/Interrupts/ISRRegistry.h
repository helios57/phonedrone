#ifndef __AP_HAL_AVR_ISR_REGISTRY_H__
#define __AP_HAL_AVR_ISR_REGISTRY_H__

#define ISR_REGISTRY_TIMER2_OVF  0
#define ISR_REGISTRY_TIMER4_CAPT 1
#define ISR_REGISTRY_TIMER5_CAPT 2
#define ISR_REGISTRY_NUM_SLOTS   3

#include "Proc.h"

class ISRRegistry {
	public:
		int register_signal(int isr_number, Proc proc);
		int unregister_signal(int isr_number);
};

#endif // __AP_HAL_AVR_ISR_REGISTRY_H__
