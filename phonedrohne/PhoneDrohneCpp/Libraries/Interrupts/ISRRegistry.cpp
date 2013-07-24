#include <stdlib.h>
#include <avr/interrupt.h>
#include "ISRRegistry.h"

Proc _registry[ISR_REGISTRY_NUM_SLOTS];

int ISRRegistry::register_signal(int signal, Proc proc) {
	if (signal >= 0 && signal < ISR_REGISTRY_NUM_SLOTS) {
		_registry[signal] = proc;
		return 0;
	}
	return -1;
}

int ISRRegistry::unregister_signal(int signal) {
	if (signal >= 0 && signal < ISR_REGISTRY_NUM_SLOTS) {
		_registry[signal] = 0;
		return 0;
	}
	return -1;
}

/* ========== ISR IMPLEMENTATIONS ========== */

extern "C" ISR(TIMER2_OVF_vect)
{
	if (_registry[ISR_REGISTRY_TIMER2_OVF] != 0) _registry[ISR_REGISTRY_TIMER2_OVF]();
}

extern "C" ISR(TIMER4_CAPT_vect)
{
	if (_registry[ISR_REGISTRY_TIMER4_CAPT] != 0) _registry[ISR_REGISTRY_TIMER4_CAPT]();
}

extern "C" ISR(TIMER5_CAPT_vect)
{
	if (_registry[ISR_REGISTRY_TIMER5_CAPT] != 0) _registry[ISR_REGISTRY_TIMER5_CAPT]();
}
