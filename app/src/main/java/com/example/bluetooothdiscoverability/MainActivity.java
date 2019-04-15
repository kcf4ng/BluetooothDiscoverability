package com.example.bluetooothdiscoverability;


import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";
    BluetoothAdapter btAdapter;
    Button btn1;
    Button btn2;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG,"onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "BroadcrastReceiver1 : state turning OFF");
                        break;
                    case  BluetoothAdapter.STATE_ON:
                        Log.d(TAG,"mBroadcastReceiver1:state_ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1:State turning ON");
                        break;
                }
            }
        }
    };

//bradcast Receiver for change mode to BT states sch as:
//1) discoverability mode on or off.

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,BluetoothAdapter.ERROR);

                switch (mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG," BluetoothAdapter2 Discoverability enabled");
                        break;

                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG," BluetoothAdapter2 Discoverability disable. Able to connections.");
                        break;

                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG," BluetoothAdapter2 Discoverability disabled. Not able ti receive connections.");
                        break;

                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG," BluetoothAdapter2 Connectiong...");
                        break;

                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG," BluetoothAdapter2 connected");


                }

            }
        }
    };


    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy called");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = BluetoothAdapter.getDefaultAdapter();


        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button enable/disable");
                enableDisableBT();

                btn2 =   (Button) findViewById(R.id.btn2);


            }
        });

    }

    private void enableDisableBT() {
        if(btAdapter == null)
        {

            Log.d(TAG, "缺少藍牙裝置 ");
        }
        if(!btAdapter.isEnabled()){
            Log.d(TAG,"bt enale");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableIntent);
            IntentFilter btIntent =new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, btIntent);
        }
        if(btAdapter.isEnabled()){
            Log.d(TAG,"BT disable");
            btAdapter.disable();
            IntentFilter btIntent =new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, btIntent);
        }
    }

    public void btnEnavleDisable_Discoverable(View view) {
        Log.d(TAG,"btnEnableDisable:Making device discoverable for 300 sec.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);

        IntentFilter intentFilter = new IntentFilter(btAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);



    }
}

