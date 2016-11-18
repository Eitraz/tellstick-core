package com.eitraz.tellstick.core.sensor;

import org.junit.Test;

import java.util.List;

import static com.eitraz.tellstick.core.TellstickCoreLibrary.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SensorHandlerTest extends AbstractSensorTest {
    @Test
    public void sensors() throws Exception {
        library.createSensor(123, TELLSTICK_TEMPERATURE | TELLSTICK_HUMIDITY, "fineoffset", "temperature");

        List<Sensor> sensors = sensorHandler.getSensors();
        assertThat(sensors).hasSize(2);

        assertThat(sensors.get(0).getId()).isEqualTo(123);
        assertThat(sensors.get(1).getId()).isEqualTo(123);

        assertThat(sensors.get(0).getDataType()).isNotEqualTo(sensors.get(1).getDataType());

        assertThat(sensorHandler.getSensor(123, TELLSTICK_TEMPERATURE)).isPresent();
        assertThat(sensorHandler.getSensor(123, TELLSTICK_HUMIDITY)).isPresent();

        assertThat(sensorHandler.getSensor(123, TELLSTICK_RAINRATE)).isNotPresent();
        assertThat(sensorHandler.getSensor(456, TELLSTICK_TEMPERATURE)).isNotPresent();
    }

    @Test
    public void dataTypes() throws Exception {
        assertThat(sensorHandler.getSupportedDataTypes()).isEqualTo(
                TELLSTICK_TEMPERATURE | TELLSTICK_HUMIDITY | TELLSTICK_RAINRATE | TELLSTICK_RAINTOTAL |
                        TELLSTICK_WINDDIRECTION | TELLSTICK_WINDAVERAGE | TELLSTICK_WINDGUST);

    }
}