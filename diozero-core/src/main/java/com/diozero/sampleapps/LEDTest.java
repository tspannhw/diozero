package com.diozero.sampleapps;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.diozero.LED;
import com.diozero.util.SleepUtil;

/**
 * LED test application
 * To run:
 * JDK Device I/O 1.0:
 *	sudo java -classpath diozero-core-0.2-SNAPSHOT.jar:diozero-provider-jdkdio10-0.2-SNAPSHOT.jar:log4j-api-2.5.jar:log4j-core-2.5.jar com.diozero.sampleapps.LEDTest 18
 * JDK Device I/O 1.1:
 *	sudo java -classpath diozero-core-0.2-SNAPSHOT.jar:diozero-provider-jdkdio11-0.2-SNAPSHOT.jar:log4j-api-2.5.jar:log4j-core-2.5.jar com.diozero.sampleapps.LEDTest 18
 * Pi4j:
 *	sudo java -classpath diozero-core-0.2-SNAPSHOT.jar:diozero-provider-pi4j-0.2-SNAPSHOT.jar:pi4j-core-1.1-SNAPSHOT.jar:log4j-api-2.5.jar:log4j-core-2.5.jar com.diozero.sampleapps.LEDTest 18
 * wiringPi:
 * sudo java -classpath diozero-core-0.2-SNAPSHOT.jar:diozero-provider-wiringpi-0.2-SNAPSHOT.jar:pi4j-core-1.1-SNAPSHOT.jar:log4j-api-2.5.jar:log4j-core-2.5.jar com.diozero.sampleapps.LEDTest 18
 * pigpgioJ:
 * sudo java -Djava.library.path=. -classpath diozero-core-0.2-SNAPSHOT.jar:diozero-provider-pigpio-0.2-SNAPSHOT.jar:pigpioj-java-0.0.1-SNAPSHOT.jar:log4j-api-2.5.jar:log4j-core-2.5.jar com.diozero.sampleapps.LEDTest 18
 */
public class LEDTest {
	private static final Logger logger = LogManager.getLogger(LEDTest.class);
	
	public static void main(String[] args) {
		if (args.length < 1) {
			logger.error("Usage: LEDTest <BCM pin number>");
			System.exit(1);
		}
		
		int pin = Integer.parseInt(args[0]);
		try (LED led = new LED(pin)) {
			logger.info("On");
			led.on();
			SleepUtil.sleepSeconds(1);
			logger.info("Off");
			led.off();
			SleepUtil.sleepSeconds(1);
			logger.info("Toggle");
			led.toggle();
			SleepUtil.sleepSeconds(1);
			logger.info("Toggle");
			led.toggle();
			SleepUtil.sleepSeconds(1);
			
			logger.info("Blink 10 times");
			led.blink(0.5f, 0.5f, 10, false);
			
			logger.info("Done");
		} catch (IOException e) {
			logger.error("Error: " + e, e);
		}
	}
}
