package com.diozero.internal.provider.mcp23017;

/*
 * #%L
 * Device I/O Zero - Core
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

import org.pmw.tinylog.Logger;

import com.diozero.MCP23017;
import com.diozero.internal.spi.AbstractDevice;
import com.diozero.internal.spi.GpioDigitalOutputDeviceInterface;

public class MCP23017DigitalOutputDevice extends AbstractDevice implements GpioDigitalOutputDeviceInterface {
	private MCP23017 mcp23017;
	private int pinNumber;

	public MCP23017DigitalOutputDevice(MCP23017 mcp23017, String key, int pinNumber) {
		super(key, mcp23017);
		
		this.mcp23017 = mcp23017;
		this.pinNumber = pinNumber;
	}

	@Override
	public boolean getValue() throws IOException {
		return mcp23017.getValue(pinNumber);
	}

	@Override
	public void setValue(boolean value) throws IOException {
		mcp23017.setValue(pinNumber, value);
	}

	@Override
	public int getPin() {
		return pinNumber;
	}

	@Override
	protected void closeDevice() throws IOException {
		Logger.debug("closeDevice()");
		mcp23017.closePin(pinNumber);
	}
}
