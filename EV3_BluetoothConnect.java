import java.io.DataInputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.NXTConnection;

public class EV3_BluetoothConnect extends Thread{
	static boolean OnOff_Flag = false;
	static NXTConnection btc;
	
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
				break;
			}
		}
		
		return true;
	}

	public void run() {
		// Loop for read data  
		DataInputStream dis = btc.openDataInputStream();
		Byte n = 0;
		
		while (true) {
			try {
				n = dis.readByte();
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
