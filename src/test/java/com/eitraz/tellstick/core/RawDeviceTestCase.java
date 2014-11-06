package com.eitraz.tellstick.core;

import com.eitraz.tellstick.core.rawdevice.RawCommandDevice;
import com.eitraz.tellstick.core.rawdevice.RawDevice;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RawDeviceTestCase {
    @Test
    public void rawDeviceHashTest() {
        Map<String, String> ap = new HashMap<>();

        ap.put("house", "h");
        ap.put("unit", "u");
        ap.put("code", "c");
        ap.put("group", "g");

        RawDevice a = new RawCommandDevice("a", "b", "c", "d", ap);

        Map<String, String> bp = new HashMap<>();

        RawDevice b = new RawCommandDevice("a", "b", "c", "d", bp);
        assertFalse(a.equals(b));
        assertNotEquals(a.hashCode(), b.hashCode());

        bp.put("house", "h");
        assertFalse(a.equals(b));
        assertNotEquals(a.hashCode(), b.hashCode());

        bp.put("unit", "u");
        assertFalse(a.equals(b));
        assertNotEquals(a.hashCode(), b.hashCode());

        bp.put("code", "c");
        assertFalse(a.equals(b));
        assertNotEquals(a.hashCode(), b.hashCode());

        bp.put("group", "g");
        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());

        b = new RawDevice("a1", "b", "c", bp);
        assertFalse(a.equals(b));

        b = new RawDevice("a", "b1", "c", bp);
        assertFalse(a.equals(b));

        b = new RawDevice("a", "b", "c1", bp);
        assertFalse(a.equals(b));

        b = new RawDevice("a1", "b", "c1", bp);
        assertFalse(a.equals(b));

        b = new RawDevice("a", "b1", "c1", bp);
        assertFalse(a.equals(b));

        b = new RawDevice("a1", "b1", "c", bp);
        assertFalse(a.equals(b));

        b = new RawDevice("a1", "b1", "c1", bp);
        assertFalse(a.equals(b));
    }

    @Test
    public void testRawDeviceHashMap() {
        Map<RawDevice, String> map = new HashMap<>();

        Map<String, String> ap = new HashMap<>();

        ap.put("house", "h");
        ap.put("unit", "u");
        ap.put("code", "c");
        ap.put("group", "g");

        RawDevice a = new RawDevice("a", "b", "c", ap);
        map.put(a, "1");

        assertEquals(1, map.size());

        Map<String, String> bp = new HashMap<>();

        map.put(new RawDevice("a", "b", "c", bp), "1");

        assertEquals(2, map.size());

        bp = new HashMap<>();
        bp.put("house", "h");
        bp.put("unit", "u");
        bp.put("code", "c");
        bp.put("group", "g");

        map.put(new RawDevice("a", "b", "c", bp), "1");
        assertEquals(2, map.size());
    }
}
