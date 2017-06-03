import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
public class EV3_mazeSolve 
{
	static EV3_motor move = new EV3_motor();
	static EV3_LightSensor light = new EV3_LightSensor();
	static EV3_ColorSensor color = new EV3_ColorSensor();
	static EV3_mazeSolve EV3 = new EV3_mazeSolve();
	
	// EV3_mazeSolve Constructor
	public EV3_mazeSolve() {
		light.setDaemon(true);
		color.setDaemon(true);
		light.start();
		color.start();
		move.setSpeed(100);
	}
	
	// Main function
	public static void main(String[] args) {
		Behavior b2 = new SenseAndMove();
		
		Behavior[] behaviorList = {b2};
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		
		LCD.drawString("Behavior Start!", 0, 2);
		arbitrator.start();
	}
}

// Find correct path
class PathFind implements Behavior {
	
	static Integer rotate_count = 0;
	static boolean LeftRightFlag = false;
	public static boolean suppressed;
	
	@Override
	public boolean takeControl() {
		// Sense White
		if (EV3_mazeSolve.light.getLight() > 0.5) {
			return true;
		}
		else {
			rotate_count = 0;
			return false;
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
	
	@Override
	public void action() {
		if (rotate_count < 10) {
			EV3_mazeSolve.move.leftTurn(20);
			Delay.msDelay(1000);
			LCD.clearDisplay();
			LCD.drawString(rotate_count.toString(), 0, 5);
			rotate_count++;
		}
		else {
			for(int i = 0; i<18; i++, rotate_count++) {
				EV3_mazeSolve.move.rightTurn(70);
				Delay.msDelay(2000);
				LCD.clearDisplay();
				LCD.drawString(rotate_count.toString(), 0, 5);
				rotate_count++;
			}
			if (rotate_count > 30)
				rotate_count = 0;
		}
	}
}

// Move robot and detect wall
class SenseAndMove implements Behavior {
	final int OneBlock = 540;
	static boolean mutex = false;
	final int RED = 0;
	final int BLACK = 1;
	final int BLUE = 2;
	final int YELLOW = 3;
	final int GREEN = 4;
	
	static Integer rotate_count = 0;
	static boolean LeftRightFlag = false;
	public static boolean suppressed;
	static boolean white_flag = false;
	
	public void Process_Red_Wall() {
		LCD.clearDisplay();
		LCD.drawString("RED", 0, 1);
		EV3_mazeSolve.move.rightTurn(180);
		Delay.msDelay(2000);
		EV3_mazeSolve.move.forwardOneblock(OneBlock);
		Delay.msDelay(4000);
	}
	
	public void Process_Black_Wall() {
		LCD.clearDisplay();
		LCD.drawString("BLACK", 0, 1);
		System.exit(-1);
	}
	
	public void Process_Blue_Wall() {
		LCD.clearDisplay();
		LCD.drawString("BLUE", 0, 1);
		EV3_mazeSolve.move.leftTurn(180);
		Delay.msDelay(2000);
		EV3_mazeSolve.move.forwardOneblock(OneBlock);
		Delay.msDelay(4000);
	}
	
	public void Process_Yellow_Wall() {
		LCD.clearDisplay();
		LCD.drawString("YELLOW", 0, 1);
		EV3_mazeSolve.move.leftTurn(180);
		Delay.msDelay(2000);
		EV3_mazeSolve.move.forwardOneblock(OneBlock);
		Delay.msDelay(4000);
		EV3_mazeSolve.move.rightTurn(180);
		Delay.msDelay(2000);
		EV3_mazeSolve.move.forwardOneblock(OneBlock);
		Delay.msDelay(4000);
	}
	
	@Override
	public boolean takeControl() {
		int color = EV3_mazeSolve.color.getColor();
		float light = EV3_mazeSolve.light.getLight();
		
		// Detect wall
		if (color >= 0 && mutex == false) {
			mutex = true;
			if (color == RED){
				Process_Red_Wall();
			}
			else if (color == BLACK){
				Process_Black_Wall();
			}
			else if (color == BLUE){
				Process_Blue_Wall();
			}
			else if (color == YELLOW){
				Process_Yellow_Wall();
			}
			else if (color == GREEN) {
				System.exit(-1);
			}
			mutex = false;
			return false;
		}
		
		// Sense White
		if (light > 0.5) {
			white_flag = true;
			return true;
		}
		else {
			white_flag = false;
			rotate_count = 0;
			return true;
		}
	}

	@Override
	public void suppress() {
	}
	
	@Override
	public void action() {
		if (white_flag == false) {
			EV3_mazeSolve.move.forward();
			Float v = EV3_mazeSolve.light.getLight();
			LCD.drawString(v.toString(), 0, 4);
		}
		else {
			if (rotate_count < 7) {
				EV3_mazeSolve.move.leftTurn(20);
				Delay.msDelay(1000);
				LCD.clearDisplay();
				LCD.drawString(rotate_count.toString(), 0, 5);
				rotate_count++;
			}
			else {
				EV3_mazeSolve.move.rightTurn(60);
				Delay.msDelay(2000);
				LCD.clearDisplay();
				LCD.drawString(rotate_count.toString(), 0, 5);
				rotate_count++;
				if (rotate_count > 20)
					rotate_count = 0;
			}
		}
	}
}