import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.SensorMode;

public class EV3_LightSensor extends Thread{
	private NXTLightSensor lightSensor = new NXTLightSensor(SensorPort.S3);
	private SensorMode lightMode = lightSensor.getRedMode();
	private float[] lightValue = new float[lightMode.sampleSize()];
	
	public void run() {
		while (true) {
			lightMode.fetchSample(lightValue, 0);
		}
	}
	
	public float getLight() {
		return lightValue[0];
	}
}
