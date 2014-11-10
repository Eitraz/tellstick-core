package com.eitraz.tellstick.core.rawdevice;

import com.eitraz.tellstick.core.Tellstick;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

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
        tellstick.getRawDeviceHandler().addRawDeviceEventListener(new RawDeviceEventListener() {
            @Override
            public void rawDeviceEvent(Map<String, String> parameters) {
                System.out.println(parameters);
            }
        });

        Thread.sleep(30000);
    }
}