package me.jiyun.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView switchStatus;
    private Switch mySwitch;
    private Button Connect;
    BT_comm BTconnect = new BT_comm();

    // Connector connector;
    public TextView text;
    static boolean ConnectionFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //added
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        Connect = (Button) findViewById(R.id.Connect);

        Connect.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                   text = (TextView) findViewById(R.id.text);

                   BTconnect.enableBT();
                   if (BTconnect.connectToNXTs()) {
                       Toast toast = Toast.makeText(MainActivity.this, "Connection Successed!", Toast.LENGTH_SHORT);
                       toast.show();

                       ConnectionFlag = true;
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
}
