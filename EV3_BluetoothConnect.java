import java.io.DataInputStream;
import java.io.IOException;

import lejos.hardware.Bluetooth;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.NXTConnection;

public class EV3_BluetoothConnect {
	char command = 'a';

	public void Connect() throws IOException, InterruptedException {		
		Boolean isrunning = true;

		// Main loop   
		while (true)
		{
			LCD.clearDisplay();
			LCD.drawString("waiting",0,1);
			LCD.refresh();

			// Listen for incoming connection
			NXTConnection btc = Bluetooth.getNXTCommConnector().waitForConnection(10, NXTConnection.RAW);
		 
			LCD.clear();
			LCD.drawString("connected",0,2);
			LCD.refresh();  


			// The InputStream for read data 
			DataInputStream dis = btc.openDataInputStream();


			// Loop for read data  
			while (isrunning) {
				Byte n = dis.readByte();
				LCD.drawInt(n, 0, 4);
			}

			dis.close();

			// Wait for data to drain
			Thread.sleep(100); 

			LCD.clear();
			LCD.drawString("closing",0,3);
			LCD.refresh();

			btc.close();

			LCD.clear();
		}
	}
}
