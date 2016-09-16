package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.AbstractMockTellstickTest;
import com.eitraz.tellstick.core.TellstickCoreLibrary;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DeviceHandlerTest extends AbstractMockTellstickTest {
    @Test
    public void createGetRemove() throws Exception {
        List<Integer> added = new CopyOnWriteArrayList<>();
        List<String> changed = new CopyOnWriteArrayList<>();
        List<Integer> removed = new CopyOnWriteArrayList<>();

        DeviceHandler deviceHandler = tellstick.getDeviceHandler();

        DeviceChangeEventListener changeListener = new DeviceChangeEventListener() {
            @Override
            public void deviceChanged(int deviceId, Device device, int changeType) {
                changed.add(deviceId + "_" + changeType);
            }

            @Override
            public void deviceAdded(int deviceId, Device device) {
                added.add(deviceId);
            }

            @Override
            public void deviceRemoved(int deviceId) {
                removed.add(deviceId);
            }
        };
        deviceHandler.addDeviceChangeEventListener(changeListener);

        // Create
        OnOffDevice device = createDefaultDevice(OnOffDevice.class, "test");
        assertThat(deviceHandler.getDevices().size()).isEqualTo(1);

        // Get
        device = deviceHandler.getDevice(device.getDeviceId());
        assertThat(device.getName()).isEqualTo("OnOffDevice_test");

        // Get by name
        device = deviceHandler.<OnOffDevice>getDeviceByName(device.getName()).orElse(null);
        assertThat(device.getName()).isEqualTo("OnOffDevice_test");

        // Delete
        assertThat(deviceHandler.removeDevice(device)).isTrue();
        assertThat(deviceHandler.getDevices().size()).isEqualTo(0);

        // Wait for last event
        await().untilCall(to(removed).size(), equalTo(1));

        // Validate changeListener events
        assertThat(added.toArray()).containsExactly(1);
        assertThat(changed.toArray()).containsExactly(
                "1_" + TellstickCoreLibrary.TELLSTICK_CHANGE_NAME,
                "1_" + TellstickCoreLibrary.TELLSTICK_CHANGE_MODEL,
                "1_" + TellstickCoreLibrary.TELLSTICK_CHANGE_PROTOCOL,
                "1_0"
                //"1_0" - will be filtered
        );
        assertThat(removed.toArray()).containsExactly(1);

        deviceHandler.removeDeviceChangeEventListener(changeListener);

    }

    @Test
    public void event() throws Exception {
        List<String> events = new CopyOnWriteArrayList<>();

        DeviceHandler deviceHandler = tellstick.getDeviceHandler();

        DeviceEventListener eventListener = (deviceId, device1, method, data) ->
                events.add(deviceId + "_" + method + "_" + data);
        deviceHandler.addDeviceEventListener(eventListener);

        OnOffDevice device = createDefaultDevice(OnOffDevice.class, "test");

        device.on();
        device.off();

        // Wait for last event
        await().untilCall(to(events).size(), equalTo(2));

        assertThat(events.toArray()).containsExactly(
                "1_" + TellstickCoreLibrary.TELLSTICK_TURNON + "_",
                "1_" + TellstickCoreLibrary.TELLSTICK_TURNOFF + "_"
        );

        deviceHandler.removeDeviceEventListener(eventListener);
    }
}