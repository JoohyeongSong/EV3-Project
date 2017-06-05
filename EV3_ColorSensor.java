import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class EV3_ColorSensor extends Thread{
	private EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
	private SensorMode colorMode = colorSensor.getRGBMode();
	private float[] colorValue = new float[colorMode.sampleSize()];	
	final float RED_THRES = 0.2f;
	final float BLACK_THRES_MIN = 0.01f;
	final float BLACK_THRES_MAX = 0.02f;
	final float BLUE_THRES = 0.1f;
	final float YELLOW_THRES = 0.1f;
	final float GREEN_THRES = 0.1f;
	final int RED = 0;
	final int BLACK = 1;
	final int BLUE = 2;
	final int YELLOW = 3;
	final int GREEN = 4;
	
	public void run() {
		while (true) {
			colorMode.fetchSample(colorValue, 0);
		}
	}
	
	public int getColor() {
		if (colorValue[0] > YELLOW_THRES && colorValue[1] > YELLOW_THRES) {
			return YELLOW;
		}
		else if (colorValue[0] > RED_THRES) {
			return RED;
		}
		else if (BLACK_THRES_MIN < colorValue[0] && colorValue[0] < BLACK_THRES_MAX 
				&& BLACK_THRES_MIN < colorValue[1] && colorValue[1] < BLACK_THRES_MAX 
				&& BLACK_THRES_MIN < colorValue[2] && colorValue[2] < BLACK_THRES_MAX) {
			return BLACK;
		}
		else if (colorValue[2] > BLUE_THRES) {
			return BLUE;
		}
		else if (colorValue[1] > GREEN_THRES){
			return GREEN;
		}
		else
			return -1;
	}
}
