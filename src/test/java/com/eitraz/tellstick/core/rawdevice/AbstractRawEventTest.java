package com.eitraz.tellstick.core.rawdevice;

import com.eitraz.tellstick.core.AbstractMockTellstickTest;
import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.junit.After;
import org.junit.Before;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public abstract class AbstractRawEventTest extends AbstractMockTellstickTest {
    private List<RawDeviceEvent> events = new CopyOnWriteArrayList<>();
    private RawDeviceEventListener listener = e -> events.add(e);

    @Before
    public void setUp() throws Exception {
        tellstick.getRawDeviceHandler().addRawDeviceEventListener(listener);
    }

    @After
    public void tearDown() throws Exception {
        tellstick.getRawDeviceHandler().removeRawDeviceEventListener(listener);
    }

    @SuppressWarnings("WeakerAccess")
    protected List<RawDeviceEvent> getEvents() {
        return events;
    }

    protected List<RawDeviceEvent> waitForNumberOfEvents(int numberOfEvents) {
        await().untilCall(to(getEvents()).size(), equalTo(numberOfEvents));
        return getEvents();
    }

    @SuppressWarnings("unchecked")
    protected <T extends RawDeviceEvent> T event(int index, Class<T> type) {
        RawDeviceEvent event = getEvents().get(index);
        assertThat(event).isInstanceOf(type);
        return (T) event;
    }
}
