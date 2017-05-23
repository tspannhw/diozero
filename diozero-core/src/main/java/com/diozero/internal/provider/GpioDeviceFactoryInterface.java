package com.diozero.internal.spi;

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
import com.diozero.api.*;
import com.diozero.util.RuntimeIOException;

public interface GpioDeviceFactoryInterface extends DeviceFactoryInterface {
	default GpioDigitalInputDeviceInterface provisionDigitalInputDevice(int gpio, GpioPullUpDown pud,
			GpioEventTrigger trigger) throws RuntimeIOException {
		PinInfo pin_info = getBoardPinInfo().getByGpioNumber(gpio);
		if (pin_info == null || !pin_info.isSupported(DeviceMode.DIGITAL_INPUT)) {
			throw new IllegalArgumentException("Invalid mode (digital input) for GPIO " + gpio);
		}

		String key = createPinKey(pin_info);

		// Check if this pin is already provisioned
		if (isDeviceOpened(key)) {
			throw new DeviceAlreadyOpenedException("Device " + key + " is already in use");
		}

		GpioDigitalInputDeviceInterface device = createDigitalInputDevice(key, pin_info, pud, trigger);
		deviceOpened(device);

		return device;
	}
	
	default GpioDigitalOutputDeviceInterface provisionDigitalOutputDevice(int gpio, boolean initialValue)
			throws RuntimeIOException {
		PinInfo pin_info = getBoardPinInfo().getByGpioNumber(gpio);
		if (pin_info == null || !pin_info.isSupported(DeviceMode.DIGITAL_OUTPUT)) {
			throw new IllegalArgumentException("Invalid mode (digital output) for GPIO " + gpio);
		}

		String key = createPinKey(pin_info);

		// Check if this pin is already provisioned
		if (isDeviceOpened(key)) {
			throw new DeviceAlreadyOpenedException("Device " + key + " is already in use");
		}

		GpioDigitalOutputDeviceInterface device = createDigitalOutputDevice(key, pin_info, initialValue);
		deviceOpened(device);

		return device;
	}
	
	default GpioDigitalInputOutputDeviceInterface provisionDigitalInputOutputDevice(int gpio, DeviceMode mode) throws RuntimeIOException {
		PinInfo pin_info = getBoardPinInfo().getByGpioNumber(gpio);
		if (pin_info == null || ! pin_info.getModes().containsAll(PinInfo.DIGITAL_IN_OUT)) {
			throw new IllegalArgumentException("Invalid mode (digital input/output) for GPIO " + gpio);
		}
		
		String key = createPinKey(pin_info);
		
		// Check if this pin is already provisioned
		if (isDeviceOpened(key)) {
			throw new DeviceAlreadyOpenedException("Device " + key + " is already in use");
		}
		
		GpioDigitalInputOutputDeviceInterface device = createDigitalInputOutputDevice(key, pin_info, mode);
		deviceOpened(device);
		
		return device;
	}
	
	GpioDigitalInputDeviceInterface createDigitalInputDevice(String key, PinInfo pinInfo, GpioPullUpDown pud, GpioEventTrigger trigger);
	GpioDigitalOutputDeviceInterface createDigitalOutputDevice(String key, PinInfo pinInfo, boolean initialValue);
	GpioDigitalInputOutputDeviceInterface createDigitalInputOutputDevice(String key, PinInfo pinInfo, DeviceMode mode);
}