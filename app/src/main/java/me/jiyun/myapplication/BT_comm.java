package me.jiyun.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

/**
 * Created by JIYUN on 2017. 6. 12..
 */


public class BT_comm extends Thread{
    //Target NXTs for communication
    final String nxt1 = "00:16:53:48:16:7B";

    BluetoothAdapter localAdapter;
    static BluetoothSocket socket_nxt1;
    boolean success = false;

    // Enables Bluetooth if not enabled
    public void enableBT(){
        localAdapter = BluetoothAdapter.getDefaultAdapter();
        // If Bluetooth not enable then do it
        if (!localAdapter.isEnabled()) {
            localAdapter.enable();
            while(!(localAdapter.isEnabled()));
        }
    }

    // Connect to both NXTs
    public boolean connectToNXTs() {
        // Get the BluetoothDevice of the NXT
        BluetoothDevice nxt_1 = localAdapter.getRemoteDevice(nxt1);
        // Try to connect to the nxt
        try {

            socket_nxt1 = nxt_1.createRfcommSocketToServiceRecord(UUID
                    .fromString("00001101-0000-1000-8000-00805F9B34FB"));

            socket_nxt1.connect();

            success = true;

        } catch (IOException e) {
            Log.d("Bluetooth","Err: Device not found or cannot connect");
            success=false;
        }

        return success;
    }

    public void writeMessage(byte msg) throws InterruptedException {
        BluetoothSocket connSock;

        // Swith nxt socket
        connSock = socket_nxt1;


        if (connSock!=null) {
            try {

                OutputStreamWriter out = new OutputStreamWriter(connSock.getOutputStream());
                out.write(msg);
                out.flush();

                Thread.sleep(1000);

            } catch (IOException e) {
                // TODO: Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // Error
        }
    }

    public int readMessage(String nxt) {
        BluetoothSocket connSock;
        int n;

        // Swith nxt socket
        connSock=socket_nxt1;

        try {

            InputStreamReader in = new InputStreamReader(connSock.getInputStream());
            n = in.read();
            return n;

        } catch (IOException e) {
            // TODO: Auto-generated catch block
            e.printStackTrace();
            return -1;
        }

    }
}
