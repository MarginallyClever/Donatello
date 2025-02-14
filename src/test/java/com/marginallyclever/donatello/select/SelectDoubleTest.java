package com.marginallyclever.donatello.select;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelectDoubleTest {
	private static final Logger logger = LoggerFactory.getLogger(SelectDoubleTest.class);
	protected int testObservation;
	
	protected void testFloatField() {
		// test contructor(s)
		SelectDouble b = new SelectDouble("test","test",0);
		assertEquals(0.0f,b.getValue(),1e-6);
		b = new SelectDouble("test2","test2",0.1f);
		assertEquals(0.1f,b.getValue(),1e-6);
		b.setValue(2000.34f);

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(2000.34f,b.getValue(),1e-3);
	}
	
	@Test
	public void testAllFloatFields() throws Exception {
		logger.debug("testAllFloatFields() start");
		Locale original = Locale.getDefault();
		Locale [] list = Locale.getAvailableLocales();
		logger.info("Available Locales: {}", list.length);
		Arrays.stream(list).parallel().forEach(loc ->{
			logger.debug("Locale={} {}", loc.toString(), loc.getDisplayLanguage());
			Locale.setDefault(loc);
			testFloatField();
		});
		Locale.setDefault(original);
		logger.debug("testAllFloatFields() end");
	}
}
