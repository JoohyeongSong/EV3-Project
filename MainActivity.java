package me.jiyun.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView switchStatus;
    private Switch mySwitch;
    private Button Connect;
    BT_comm BTconnect = new BT_comm();
    Button button;
    // Connector connector;
    public TextView text;

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
                //text = (TextView) findViewById(R.id.text);
                BTconnect.enableBT();
                if (BTconnect.connectToNXTs()) {
                    Toast toast = Toast.makeText(MainActivity.this, "Connection Successed!", Toast.LENGTH_SHORT );
                    toast.show();

                    byte bt = 'c';
                    try {
                        BTconnect.writeMessage(bt);
                    } catch (InterruptedException e) {
                    }
                    //text.setText("Connected!");
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "Connection failed!", Toast.LENGTH_SHORT );
                    toast.show();
                    //text.setText("Connection failed!");
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
                }else{
                    switchStatus.setText("EV3 is currently OFF");
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
