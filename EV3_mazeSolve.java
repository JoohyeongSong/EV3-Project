import java.io.IOException;

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
	
	static EV3_BluetoothConnect connect = new EV3_BluetoothConnect();

	// EV3_mazeSolve Constructor
	public EV3_mazeSolve() {
		light.setDaemon(true);
		color.setDaemon(true);
		light.start();
		color.start();
		move.setSpeed(100);
		}
	
	// Main function
	public static void main(String[] args) throws IOException, InterruptedException {
		Behavior b2 = new SenseAndMove();
		if(connect.Connect()) {
			connect.start();
		}
		
		Behavior[] behaviorList = {b2};
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		
		LCD.drawString("Behavior Start!", 0, 2);
		arbitrator.start();
	}
}

// Move robot and detect wall
class SenseAndMove implements Behavior {
	final int OneBlock = 400;
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
	static boolean direction_flag = true;
	static Integer rotate_size = 0;
	
	static Integer forward_angle;
	static Integer previous_angle = 0;
	public static int[] path = new int[1000];
	//public static Byte[] path = new Byte[1000];
	public static int path_index = 1;
	
	static boolean write_mutex = false;
	int SendData[] = {10, 1, 2, 3, 2, 3, 3, 3, 2, 1, 1};
	//Byte SendDataByte[] = {10, 1, 2, 3, 2, 3, 3, 3, 2, 1, 1};
	
	
	public void Process_Blue_Wall() {
		LCD.clearDisplay();
		LCD.drawString("BLUE", 0, 1);
		EV3_mazeSolve.move.rightTurn(180);
		previous_angle = forward_angle;
		path[path_index++] = 3;
		Delay.msDelay(2000);
		/*
		EV3_mazeSolve.move.forwardOneblock(OneBlock);
		previous_angle = forward_angle;
		path[path_index++] = 1;
		Delay.msDelay(4000);
		*/
	}
	
	public void Process_Black_Wall() {
		LCD.clearDisplay();
		LCD.drawString("BLACK", 0, 1);
		
		/*
		for(int i = 0; i < path_index; i++){
			LCD.drawString("" + path[i], i, 6);					
		}
		*/
		
		EV3_mazeSolve.move.stop();
		
		try {
			if (!write_mutex) {
				write_mutex = true;
				path[0] = path_index-1;
				EV3_BluetoothConnect.DeliverPath(path);
				write_mutex = false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Delay.msDelay(5000);
		
		LCD.clearDisplay();
		LCD.drawString("Ending Program", 0, 2);
		
		for (Integer count = 5; count > 0; count--) {
			LCD.drawString(count.toString(), 0, 3);
			Delay.msDelay(1000);
		}
		
		System.exit(-1);
	}
	
	public void Process_Red_Wall() {
		LCD.clearDisplay();
		LCD.drawString("RED", 0, 1);
		EV3_mazeSolve.move.leftTurn(180);
		previous_angle = forward_angle;
		path[path_index++] = 2;
	
		Delay.msDelay(2000);
		
		EV3_mazeSolve.move.forwardOneblock(OneBlock);
		previous_angle = forward_angle;
		path[path_index++] = 1;
		
		Delay.msDelay(2000);
	}
	
	public void Process_Yellow_Wall() {
		LCD.clearDisplay();
		LCD.drawString("YELLOW", 0, 1);
		EV3_mazeSolve.move.leftTurn(180);
		previous_angle = forward_angle;
		path[path_index++] = 2;
		Delay.msDelay(2000);
		EV3_mazeSolve.move.forwardOneblock(OneBlock);
		previous_angle = forward_angle;
		path[path_index++] = 1;
		Delay.msDelay(4000);
		EV3_mazeSolve.move.rightTurn(180);
		previous_angle = forward_angle;
		path[path_index++] = 3;
		Delay.msDelay(2000);
		/*
		EV3_mazeSolve.move.forwardOneblock(OneBlock);
		previous_angle = forward_angle;
		path[path_index++] = 1;
		Delay.msDelay(4000);
		*/
	}
	
	@Override
	public boolean takeControl() {
		int color = EV3_mazeSolve.color.getColor();
		float light = EV3_mazeSolve.light.getLight();
		
		
		if (EV3_BluetoothConnect.OnOff_Flag == false) {
			EV3_mazeSolve.move.stop();
			return false;
		}
		
		// Detect wall
		if (color >= 0 && mutex == false) {
			mutex = true;
			if (color == RED){
				Process_Red_Wall();
			}
			/*
			else if (color == BLACK){
				Process_Black_Wall();
			}
			*/
			else if (color == BLUE){
				Process_Blue_Wall();
			}
			else if (color == YELLOW){
				Process_Yellow_Wall();
				
			}
			else if (color == BLACK) {
				for(int i = 0; i < path_index; i++){
					LCD.drawString("" + path[i], i, 6);					
				}
				
				EV3_mazeSolve.move.stop();
				try {
					for (int i = 0; i < 20; i++) {
					if (!write_mutex) {
						write_mutex = true;
						path[0] = path_index-1;
						EV3_BluetoothConnect.DeliverPath(path);
						write_mutex = false;
					}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				EV3_mazeSolve.move.stop();
				EV3_BluetoothConnect.End_flag = true;
				System.exit(-1);
			}
			mutex = false;
			
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
		// if Black, Go through
		if (white_flag == false) {
			
			
			forward_angle = EV3_mazeSolve.move.forward();
			
			
			if(forward_angle - previous_angle > OneBlock){
				LCD.drawString("" + path_index, 0, 5);
				previous_angle = forward_angle;
				path[path_index++] = 1;
				
			}
			Integer tmp = forward_angle - previous_angle;
			
			Float v = EV3_mazeSolve.light.getLight();
			//LCD.clearDisplay();
			LCD.drawString(v.toString(), 0, 4);
			LCD.drawString(tmp.toString(), 0, 7);
			rotate_size = 20;
		}
		// if White, Find path
		else {
			if (direction_flag) {
				EV3_mazeSolve.move.leftTurn(rotate_size);
				Delay.msDelay(rotate_size*20);
				rotate_size = rotate_size + 20;
				direction_flag = false;
				LCD.clearDisplay();
				LCD.drawString(rotate_size.toString(), 0, 5);
				rotate_count++;
			}
			else {
				EV3_mazeSolve.move.rightTurn(rotate_size);
				Delay.msDelay(rotate_size*20);
				rotate_size = rotate_size + 20;
				direction_flag = true;
				LCD.clearDisplay();
				LCD.drawString(rotate_size.toString(), 0, 5);
				rotate_count++;
				if (rotate_count > 20)
					rotate_count = 0;
			}
		}
	}
}
