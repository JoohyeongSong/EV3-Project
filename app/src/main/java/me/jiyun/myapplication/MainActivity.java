package me.jiyun.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private TextView switchStatus;
    private Switch mySwitch;
    private Button Connect;
    BT_comm BTconnect = new BT_comm();
    private Button PathButton;

    private Receive RcvData;

    // Connector connector;
    public TextView text;
    static boolean ConnectionFlag = false;
    float startY = 1500;
    float startX = 100;
    int Xdir[] = {1, 0, -1, 0};
    int Ydir[] = {0, 1, 0, -1};
    float OneBlock = 200;
    Integer[] buf = new Integer[1000];
    int bufferLength = 0;
    String path_data = "{";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MyView vw = new MyView(this);

        setContentView(R.layout.activity_main);
        // sendPostData();

        //added
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        Connect = (Button) findViewById(R.id.Connect);
        PathButton = (Button) findViewById(R.id.PathView);

        PathButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                setContentView(vw);

                sendPostData();

                Toast.makeText(MainActivity.this, "Data POST", Toast.LENGTH_SHORT).show();
            }
        });

        Connect.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                text = (TextView) findViewById(R.id.text);

                BTconnect.enableBT();
                if (BTconnect.connectToNXTs()) {
                    Toast toast = Toast.makeText(MainActivity.this, "Connection Successed!", Toast.LENGTH_SHORT);
                    toast.show();

                    ConnectionFlag = true;

                    RcvData = new Receive(BT_comm.socket_nxt1);
                    RcvData.execute();

                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "Connection failed!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //set the switch to OFF
        mySwitch.setChecked(false);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    switchStatus.setText("EV3 is currently ON");

                    if(ConnectionFlag = true) {
                        byte bt = 1;
                        try {
                            BTconnect.writeMessage(bt);
                        } catch (InterruptedException e) {
                        }
                    }

                }else{
                    switchStatus.setText("EV3 is currently OFF");

                    if(ConnectionFlag = true) {
                        byte bt = 0;
                        try {
                            BTconnect.writeMessage(bt);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        });

        //check the current state before we display the screen
        if(mySwitch.isChecked()){
            switchStatus.setText("EV3 is currently ON");
        }
        else {
            switchStatus.setText("EV3 is currently OFF");
        }
    }

    public class Receive extends AsyncTask<Void, Void, Void> {
        //private TextView ViewPath = (TextView) findViewById(R.id.pathprint);
        BluetoothSocket socket;
        int n = 0;
        boolean bufLengthFlag = false;

        int inputCnt = 0;
        int tmp;

        public Receive(BluetoothSocket s) {
            socket = s;
        }
        @Override
        protected void onPostExecute(Void v) {
            String str = "";
            super.onPostExecute(v);


            Toast toast = Toast.makeText(MainActivity.this, "Message Received!", Toast.LENGTH_SHORT);
            toast.show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStreamReader in = null;

            try {
                in = new InputStreamReader(socket.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }

            inputCnt = 0;
            while(true) {

                try {

                    if(!bufLengthFlag){
                        bufferLength = in.read();

                        if(bufferLength != 0)
                            bufLengthFlag = true;

                    }
                    else {
                        buf[inputCnt] = in.read();


                        if (buf[inputCnt] != 0)
                            inputCnt++;

                        if (inputCnt == bufferLength)
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for(int i=0; i<bufferLength; i++)
                path_data = (path_data + buf[i] + " ");
            path_data += "}";

            return null;
        }

    }

    protected class MyView extends View {
        public MyView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            Paint Pnt = new Paint();
            Pnt.setTextSize(50);
            Pnt.setStrokeWidth(30);
            Pnt.setColor(Color.BLUE);
            canvas.drawPoint(startX, startY, Pnt);
            canvas.drawText("START", startX, startY + 50, Pnt);
            Pnt.setStrokeWidth(10);
            Pnt.setColor(Color.GREEN);

            float curX = startX;
            float curY = startY;
            int currentDir = 0;
            for (int i = 0; i < bufferLength; i++) {
                if (buf[i] == 1) {
                    float newX = curX + Xdir[currentDir] * OneBlock;
                    float newY = curY + Ydir[currentDir] * OneBlock;
                    canvas.drawLine(curX, curY, newX, newY, Pnt);

                    curX = newX;
                    curY = newY;
                }
                if (buf[i] == 2) {
                    if (currentDir == 0)
                        currentDir = 3;
                    else
                        currentDir -= 1;
                }
                if (buf[i] == 3) {
                    if (currentDir == 3)
                        currentDir = 0;
                    else
                        currentDir += 1;
                }
            }
            Pnt.setStrokeWidth(30);
            Pnt.setColor(Color.RED);
            canvas.drawPoint(curX, curY, Pnt);
            canvas.drawText("END", curX + 10, curY + 50, Pnt);

        }
    }


    public void sendPostData(){
        new Thread(){
            public void run(){
                try{


                    String link = "http://10.1.11.55:5000/path";
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(link);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("test", path_data));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                } catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                } catch(ClientProtocolException e){
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}