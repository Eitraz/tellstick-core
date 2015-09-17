package com.eitraz.tellstick.core.rawdevice;

import com.eitraz.tellstick.core.Tellstick;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RawDeviceHandlerTest {
    private static Tellstick tellstick;

    @BeforeClass
    public static void beforeClass() {
        tellstick = new Tellstick();
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

        Thread.sleep(30000);
    }
}