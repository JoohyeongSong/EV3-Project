import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class EV3_motor {
	static private RegulatedMotor leftmotor = new EV3LargeRegulatedMotor(MotorPort.A);
	static private RegulatedMotor rightmotor = new EV3LargeRegulatedMotor(MotorPort.B);
	
	public void forward() {
		leftmotor.forward();
		rightmotor.forward();
	}
	
	public void forwardOneblock(int rotate) {
		leftmotor.rotate(rotate, true);
		rightmotor.rotate(rotate, true);
	}
	
	public void backward() {
		leftmotor.backward();
		rightmotor.backward();
	}
	
	public void stop() {
		leftmotor.stop();
		rightmotor.stop();
	}
	
	public void setSpeed(int speed) {
		leftmotor.setSpeed(speed);
		rightmotor.setSpeed(speed);
	}
	
	public void leftTurn(int rotate) {
		leftmotor.rotate(-rotate, true);
		rightmotor.rotate(rotate, true);
	}
	
	public void rightTurn(int rotate) {
		leftmotor.rotate(rotate, true);
		rightmotor.rotate(-rotate, true);
	}
	
}
