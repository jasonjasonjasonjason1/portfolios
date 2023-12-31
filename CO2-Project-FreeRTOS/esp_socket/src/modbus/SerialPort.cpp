/*
 * SerialPort.cpp
 *
 *  Created on: 10.2.2016
 *      Author: krl
 */
#include "SerialPort.h"



SerialPort::SerialPort() {
	if(!u) {
		LpcPinMap none = {-1, -1}; // unused pin has negative values in it
		LpcPinMap txpin = { 0, 28 }; // transmit pin that goes to rs485 driver chip
		LpcPinMap rxpin = { 0, 24 }; // receive pin that goes to rs485 driver chip
		LpcPinMap rtspin = { 1, 0 }; // handshake pin that is used to set tranmitter direction
		LpcUartConfig cfg = { LPC_USART1, 9600, UART_CFG_DATALEN_8 | UART_CFG_PARITY_NONE | UART_CFG_STOPLEN_2, true, txpin, rxpin, rtspin, none };
		u = new LpcUart(cfg);
	}
}

LpcUart *SerialPort::u = nullptr;

SerialPort::~SerialPort() {
	/* DeInitialize UART peripheral */
	delete u;
}

int SerialPort::available() {
	return u->peek();
}

void SerialPort::begin(int speed) {
	u->speed(speed);

}

int SerialPort::read() {
	char byte;
	if(u->read(&byte, 1, 1)> 0) return (byte);
	return -1;
}
int SerialPort::write(const char* buf, int len) {
	return u->write(buf, len);
}

int SerialPort::print(int val, int format) {
	// here only to maintain compatibility with Arduino interface
	(void) val;
	(void) format;
	return (0);
}

void SerialPort::flush() {
	while(!u->txempty()) __WFI();
}
