package com.eitraz.tellstick.core.sensor;

import com.eitraz.tellstick.core.AbstractMockTellstickTest;
import com.eitraz.tellstick.core.sensor.value.SensorValue;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSensorTest extends AbstractMockTellstickTest {
    protected SensorHandler sensorHandler;

    protected List<Pair<Sensor, SensorValue>> events = new ArrayList<>();
    private SensorEventListener listener;

    @Before
    public void setUp() throws Exception {
        sensorHandler = tellstick.getSensorHandler();

        listener = (sensor, value) -> events.add(new Pair<Sensor, SensorValue>() {
            @Override
            public Sensor getLeft() {
                return sensor;
            }

            @Override
            public SensorValue getRight() {
                return value;
            }

            @Override
            public SensorValue setValue(SensorValue value) {
                throw new UnsupportedOperationException("Not implemented");
            }
        });
        sensorHandler.addSensorEventListener(listener);
    }

    @After
    public void tearDown() throws Exception {
        sensorHandler.removeSensorEventListener(listener);
    }
}