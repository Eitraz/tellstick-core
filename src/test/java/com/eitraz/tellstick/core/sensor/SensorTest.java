package com.eitraz.tellstick.core.sensor;

import com.eitraz.tellstick.core.TellstickCoreMockLibrary;
import com.eitraz.tellstick.core.sensor.value.SensorValue;
import com.eitraz.tellstick.core.sensor.value.TemperatureSensorValue;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.time.LocalDateTime;

import static com.eitraz.tellstick.core.TellstickCoreLibrary.TELLSTICK_HUMIDITY;
import static com.eitraz.tellstick.core.TellstickCoreLibrary.TELLSTICK_TEMPERATURE;
import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SensorTest extends AbstractSensorTest {
    @Test
    public void sensorEvent() throws Exception {
        TellstickCoreMockLibrary.SensorMock sensorMock = library.createSensor(123,
                TELLSTICK_TEMPERATURE | TELLSTICK_HUMIDITY,
                "fineoffset", "temperature");

        sensorMock.setValue(TELLSTICK_TEMPERATURE, "23");
        sensorMock.setValue(TELLSTICK_TEMPERATURE, "23");
        sensorMock.setValue(TELLSTICK_TEMPERATURE, "23");

        await().untilCall(to(events).size(), equalTo(1));

        Pair<Sensor, SensorValue> sensorEvent = events.get(0);
        Sensor sensor = sensorEvent.getKey();
        SensorValue value = sensorEvent.getValue();

        assertThat(sensor.getProtocol()).isEqualTo("fineoffset");
        assertThat(sensor.getModel()).isEqualTo("temperature");
        assertThat(sensor.getId()).isEqualTo(123);
        assertThat(sensor.getDataType()).isEqualTo(TELLSTICK_TEMPERATURE); // each sensor contains only one datatype

        assertThat(value.getDataType()).isEqualTo(TELLSTICK_TEMPERATURE);
        assertThat(value.getValue()).isEqualTo("23");
        assertThat(value.getTimestamp().isBefore(LocalDateTime.now())).isTrue();

        assertThat(value).isInstanceOf(TemperatureSensorValue.class);
        assertThat(((TemperatureSensorValue) value).getTemperature()).isEqualTo(23.0d);

        // Fetch value (should be last one from mock)
        value = sensor.getValue();
        assertThat(value.getDataType()).isEqualTo(TELLSTICK_TEMPERATURE);
        assertThat(value.getValue()).isEqualTo("23");
        assertThat(value.getTimestamp().isBefore(LocalDateTime.now())).isTrue();

        assertThat(value).isInstanceOf(TemperatureSensorValue.class);
        assertThat(((TemperatureSensorValue) value).getTemperature()).isEqualTo(23.0d);

        assertThat(sensor.getSensorHandler()).isEqualTo(sensorHandler);
    }

    @Test
    public void equalsAndHashCode() throws Exception {
        Sensor<SensorValue> tempSensor = new Sensor<>(sensorHandler, 123, "fineoffset", "temperature", TELLSTICK_TEMPERATURE);
        Sensor<SensorValue> humiditySensor = new Sensor<>(sensorHandler, 123, "fineoffset", "temperature", TELLSTICK_HUMIDITY);

        assertThat(tempSensor).isNotEqualTo(humiditySensor);
        assertThat(tempSensor.hashCode()).isNotEqualTo(humiditySensor.hashCode());
    }
}
