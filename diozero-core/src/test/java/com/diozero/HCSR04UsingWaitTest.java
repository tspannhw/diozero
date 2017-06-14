package com.diozero;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pmw.tinylog.Logger;

import com.diozero.internal.provider.test.HCSR04EchoPin;
import com.diozero.internal.provider.test.HCSR04TriggerPin;
import com.diozero.internal.provider.test.TestDeviceFactory;
import com.diozero.sampleapps.sandpit.HCSR04UsingWait;
import com.diozero.util.SleepUtil;

@SuppressWarnings("static-method")
public class HCSR04UsingWaitTest {
	@BeforeClass
	public static void beforeClass() {
		TestDeviceFactory.setDigitalInputDeviceClass(HCSR04EchoPin.class);
		TestDeviceFactory.setDigitalOutputDeviceClass(HCSR04TriggerPin.class);
	}
	
	@Test
	public void test() {
		try (HCSR04UsingWait hcsr04 = new HCSR04UsingWait(26, 4)) {
			for (int i=0; i<6; i++) {
				float distance = hcsr04.getDistanceCm();
				Logger.info("Distance={}", Float.valueOf(distance));
				SleepUtil.sleepSeconds(0.5);
			}
		}
	}
}
