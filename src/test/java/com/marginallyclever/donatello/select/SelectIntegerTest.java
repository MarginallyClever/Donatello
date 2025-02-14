package com.marginallyclever.donatello.select;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class SelectIntegerTest {
    private int result = 5;

    @Test
    public void test() throws InterruptedException {
        Locale locale = Locale.getDefault();
        SelectInteger selectInteger = new SelectInteger("internalName", "labelKey", locale, 0);
        selectInteger.addSelectListener(evt->{
            result = (Integer)evt.getNewValue();
        });
        selectInteger.setValue(1);
        // the event fires ~100ms after setValue is called.
        Thread.sleep(200);
        Assertions.assertEquals(1,result);
    }
}
