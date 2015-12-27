package com.eitraz.tellstick.core.rawdevice;

import com.eitraz.tellstick.core.TellstickImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class RawDeviceHandlerTest {
    private static TellstickImpl tellstick;

    @BeforeClass
    public static void beforeClass() {
        tellstick = new TellstickImpl();
        tellstick.start();
    }

    @AfterClass
    public static void afterClass() {
        if (tellstick != null) {
            tellstick.stop();
            tellstick = null;
        }
    }

    @Test
    public void testHandleEvent() throws Exception {
        tellstick.getRawDeviceHandler().addRawDeviceEventListener(System.out::println);

        Thread.sleep(60000);
    }
}