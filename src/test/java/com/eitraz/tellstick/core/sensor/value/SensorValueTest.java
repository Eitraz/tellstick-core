package com.eitraz.tellstick.core.sensor.value;

import com.eitraz.tellstick.core.TellstickCoreMockLibrary;
import com.eitraz.tellstick.core.sensor.AbstractSensorTest;
import org.junit.Before;
import org.junit.Test;

import static com.eitraz.tellstick.core.TellstickCoreLibrary.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SensorValueTest extends AbstractSensorTest {
    private TellstickCoreMockLibrary.SensorMock sensorMock;

    @Before
    public void createSensorMock() throws Exception {
        sensorMock = library.createSensor(123, TELLSTICK_TEMPERATURE | TELLSTICK_HUMIDITY | TELLSTICK_WINDAVERAGE, "fineoffset", "temperature");
    }

    @Test
    public void sensorValue() throws Exception {
        sensorMock.setValue(TELLSTICK_TEMPERATURE, "23");
        sensorMock.setValue(TELLSTICK_HUMIDITY, "54");
        sensorMock.setValue(TELLSTICK_WINDAVERAGE, "12");

        TemperatureSensorValue temperature = sensorHandler.<TemperatureSensorValue>getSensor(123, TELLSTICK_TEMPERATURE)
                .orElseThrow(() -> new RuntimeException("Sensor expected"))
                .getValue();
        assertThat(temperature.getTemperature()).isEqualTo(23);

        HumiditySensorValue humidity = sensorHandler.<HumiditySensorValue>getSensor(123, TELLSTICK_HUMIDITY)
                .orElseThrow(() -> new RuntimeException("Sensor expected"))
                .getValue();
        assertThat(humidity.getHumidity()).isEqualTo(54);

        SensorValue other = sensorHandler.getSensor(123, TELLSTICK_WINDAVERAGE)
                .orElseThrow(() -> new RuntimeException("Sensor expected"))
                .getValue();
        assertThat(other.getValue()).isEqualTo("12");

        assertThat(temperature.toString()).contains("dataType=1").contains("value='23'");
    }
}