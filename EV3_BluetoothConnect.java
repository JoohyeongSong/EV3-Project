import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.NXTConnection;

public class EV3_BluetoothConnect extends Thread{
	static boolean OnOff_Flag = false;
	static NXTConnection btc;
	static boolean End_flag = false;
	static DataInputStream input;
	static DataOutputStream output;
	
	
	public boolean Connect() throws IOException, InterruptedException {		
	
		// Main loop   
		while (true)
		{
			LCD.clearDisplay();
			LCD.drawString("waiting",0,1);
			LCD.refresh();

			// Listen for incoming connection
			btc = Bluetooth.getNXTCommConnector().waitForConnection(10, NXTConnection.RAW);
		 
			if(btc != null){
				LCD.clear();
				LCD.drawString("connected",0,2);
				LCD.refresh();
				
				output = btc.openDataOutputStream();
				input = btc.openDataInputStream();
				
				break;
			}
		}
		
		return true;
	}
	
	public static void DeliverPath (int[] SendData) throws IOException {
		
		LCD.drawString("Path Print!", 0, 6);
		
		for (int i = 0; i <= SendData[0]; i++) {
			output.writeInt(SendData[i]);
		}
		
		LCD.clearDisplay();
	}
	
	public static void DeliverPathByte (Byte[] SendData) throws IOException {
		
		LCD.drawString("Path: ", 0, 7);
		int length = SendData[0];
		
		for (int i = 0; i <= length; i++) {
			output.writeByte(SendData[i]);
		}
		
		 
		LCD.clearDisplay();
	}

	public void run() {
		// Loop for read data  
		Byte n = 0;
		
		while (true) {
			try {
				n = input.readByte();
				//Thread.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			LCD.drawInt(n, 0, 4);
			if (n == 1)
				OnOff_Flag = true;
			else
				OnOff_Flag = false;
		}
	
	}
	
}
