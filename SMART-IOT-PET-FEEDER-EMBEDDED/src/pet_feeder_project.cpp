/*
===============================================================================
 Name        : main.c
 Author      : $(author)
 Version     :
 Copyright   : $(copyright)
 Description : main definition
===============================================================================
*/

#if defined (__USE_LPCOPEN)
#if defined(NO_BOARD_LIB)
#include "chip.h"
#else
#include "board.h"
#endif
#endif

#include <cr_section_macros.h>

// TODO: insert other include files here
#include "DigitalIoPin.h"
static volatile int counter;
void SysTick_Handler(void)
{
if(counter > 0) counter--;
}
#ifdef __cplusplus

#endif
void Sleep(int ms)
{
counter = ms;
while(counter > 0) {
__WFI();
}
}

// TODO: insert other definitions and declarations here

int main(void) {

#if defined (__USE_LPCOPEN)
    // Read clock settings and update SystemCoreClock variable
    SystemCoreClockUpdate();
#if !defined(NO_BOARD_LIB)
    // Set up and initialize all required blocks and
    // functions related to the board hardware
    Board_Init();
    // Set the LED to the state of "On"
    Board_LED_Set(0, true);
#endif
#endif

    // TODO: insert code here
    DigitalIoPin d10(0,27,DigitalIoPin::output);
    DigitalIoPin d11(0,28,DigitalIoPin::output);
    //DigitalIoPin d12(0,12,DigitalIoPin::output);
    //DigitalIoPin d13(0,14,DigitalIoPin::output);

    DigitalIoPin switch1(0,17,DigitalIoPin::input, true);
    	DigitalIoPin switch2(1,11,DigitalIoPin::input, true);
    	DigitalIoPin switch3(1,9,DigitalIoPin::input, true);
    // Force the counter to be placed into memory
    volatile static int i = 0 ;
    // Enter an infinite loop, just incrementing a counter
    while(1) {
    	if(switch1.read()){
    		d10.write(true);
    		d11.write(false);
    	}
    	else if(switch2.read()){
    	    		d10.write(false);
    	    		d11.write(true);
    	    	}

    	//d12.write(true);
    	//d13.write(false);
    	/*while(!switch1.read());
    	d10.write(false);
    	    	d11.write(true);

    	    	//d12.write(false);
    	    	//d13.write(true);
    	    	Sleep(5000);
    	    	while(!switch2.read());*/
        i++ ;
        // "Dummy" NOP to allow source level single
        // stepping of tight while() loop
        __asm volatile ("nop");
    }
    return 0 ;
}
