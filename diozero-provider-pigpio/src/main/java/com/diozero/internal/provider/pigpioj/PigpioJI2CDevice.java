package com.diozero.internal.provider.pigpioj;

/*
 * #%L
 * Device I/O Zero - pigpioj provider
 * %%
 * Copyright (C) 2016 diozero
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */


import java.io.IOException;
import java.nio.ByteBuffer;

import org.pmw.tinylog.Logger;

import com.diozero.internal.spi.AbstractDevice;
import com.diozero.internal.spi.DeviceFactoryInterface;
import com.diozero.internal.spi.I2CDeviceInterface;
import com.diozero.pigpioj.PigpioI2C;
import com.diozero.util.RuntimeIOException;

public class PigpioJI2CDevice extends AbstractDevice implements I2CDeviceInterface {
	private static final int CLOSED = -1;
	
	private int controller;
	private int address;
	private int handle = CLOSED;

	public PigpioJI2CDevice(String key, DeviceFactoryInterface deviceFactory, int controller, int address,
			int addressSize) throws RuntimeIOException {
		super(key, deviceFactory);
		
		this.controller = controller;
		this.address = address;
		
		try {
			handle = PigpioI2C.i2cOpen(controller, address, 0);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
		Logger.debug("I2C device ({}, 0x{}) opened, handle={}",
				Integer.valueOf(controller), Integer.toHexString(address), Integer.valueOf(handle));
		if (handle < 0) {
			handle = CLOSED;
			throw new RuntimeIOException(String.format("Error opening I2C device on bus %d, address 0x%x",
					Integer.valueOf(controller), Integer.toHexString(address)));
		}
	}

	@Override
	public boolean isOpen() {
		return handle >= 0;
	}

	@Override
	public void read(int register, int subAddressSize, ByteBuffer dst) throws RuntimeIOException {
		if (! isOpen()) {
			throw new IllegalStateException("I2C Device " + controller + "-" + address + " is closed");
		}
		
		int to_read = dst.remaining();
		byte[] buffer = new byte[to_read];
		try {
			int read = PigpioI2C.i2cReadI2CBlockData(handle, register, buffer, to_read);
			if (read != to_read) {
				throw new RuntimeIOException("Didn't read correct number of bytes, read " + read + ", expected " + to_read);
			}
			dst.put(buffer);
			dst.flip();
		}  catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	@Override
	public void write(int register, int subAddressSize, ByteBuffer src) throws RuntimeIOException {
		if (! isOpen()) {
			throw new IllegalStateException("I2C Device " + controller + "-" + address + " is closed");
		}
		
		int to_write = src.remaining();
		byte[] buffer = new byte[to_write];
		src.get(buffer, src.position(), to_write);
		try {
			PigpioI2C.i2cWriteI2CBlockData(handle, register, buffer, to_write);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	@Override
	protected void closeDevice() throws RuntimeIOException {
		Logger.debug("closeDevice()");
		try {
			PigpioI2C.i2cClose(handle);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
		handle = CLOSED;
	}
}
