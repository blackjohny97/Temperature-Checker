package com.example.test2;

import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public final static String MODULE_MAC = "00:20:08:00:28:6F";
    public final static int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothAdapter bta;                 //bluetooth stuff
    BluetoothSocket mmSocket;             //bluetooth stuff
    BluetoothDevice mmDevice;             //bluetooth stuff
    ConnectedThread btt = null;           //Our custom thread
    public Handler mHandler;              //this receives messages from thread
    Button scan, Databtn, butongrafice, reset, connect;
    EditText NameTxt;
    TextView DateTxt;
    TextView temperatura;
    MainActivity2 PaginaDoi = new MainActivity2();
    MainActivity3 PaginaTrei = new MainActivity3();
    private static final String TAG ="MainActivity";
    public DatePickerDialog.OnDateSetListener mDateSetListener;
    String comanda = "A";
// butonul scan este cel care adauga in baza de date,
// butonul Databtn este cel care face tranzitia la screen-ul 2 in care este baza de date,
// butonul butongrafice este cel pentru tranzitia la screen-ul cu grafice,
// datapicker este utilizat pentru un pop-up dialog pentru a alege data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan = findViewById(R.id.scan);
        connect = findViewById(R.id.connect);
        Databtn = findViewById(R.id.DataBtn);
        NameTxt = (EditText) findViewById(R.id.NameTxt);
        butongrafice = findViewById(R.id.butongrafice);
        reset = findViewById(R.id.reset);
        temperatura = (TextView) findViewById(R.id.temperatura);
        DateTxt = (TextView) findViewById(R.id.DateTxt);

        DateTxt.setOnClickListener(new View.OnClickListener() {
//setarile ferestrei de dialog pentru alegerea datei
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, day, month, year);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
//obiectul returnat de datapicker convertit ca string pentru a putea fi introdus in baza de date
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dataPicker, int year, int month, int day) {
                month++;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);
                String date = day + "." + month + "." + year;
                DateTxt.setText(date);
            }
        };

        Databtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity2.class);
                startActivity(intent);

                DatabaseClass databaseClass = new DatabaseClass(MainActivity.this);

                PaginaDoi.ArataPersoaneInLista(databaseClass);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
//butonul care adauga in baza de date formand un obiect de tip modelare tabel pentru a fi introdus in baza de date,
// valoarea float 0.0 va fi inlocuita de o variabila double care va fi primita prin bluetooth de la un Arduino(modificare care mai trebuie facuta)
                /*if (mmSocket.isConnected() && btt != null) {
                    btt.write(comanda.getBytes());
                }
*/
                ModelareTabel modelareTabel;

                try {
                    modelareTabel = new ModelareTabel(-1, NameTxt.getText().toString(), DateTxt.getText().toString(), Float.valueOf(temperatura.getText().toString()));
                    Toast.makeText(MainActivity.this, modelareTabel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    modelareTabel = new ModelareTabel(-1,"error","error", (float) 0.0);
                }

                DatabaseClass databaseClass = new DatabaseClass(MainActivity.this);
//adaugare, iar toast este un mesaj de confirmare care va aparea la finalizarea executiei in aplicatie
                boolean adaugat = databaseClass.Adauga(modelareTabel);
                Toast.makeText(MainActivity.this, "A mers" + adaugat, Toast.LENGTH_SHORT).show();
                
            }
        });

        butongrafice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), MainActivity3.class);
                startActivity(intent1);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temperatura.setText("");
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mmSocket.isConnected() && btt != null) {
                    btt.write(comanda.getBytes());
                }
            }
        });

        bta = BluetoothAdapter.getDefaultAdapter();
        //if bluetooth is not enabled then create Intent for user to turn it on
        if(bta == null){
            Toast.makeText(getApplicationContext(), "Bluetooth not available", Toast.LENGTH_LONG).show();
        }
        else {
            if (!bta.isEnabled()) {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
            } else {
                switch(initiateBluetoothProcess()){
                    case 0:
                        temperatura.append("Connected");
                        break;
                    case 1:
                        temperatura.append("Can\\'t establish connection\n");
                        break;
                    case 2:
                        temperatura.append("Can\\'t close mmSocket\n");
                        break;
                    default:
                        temperatura.append("Unknown status\n");
                        break;
                }
            }
        }
    }

    @Override //when you return from another activity, this is the first function that it's called
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            initiateBluetoothProcess();
        }
    }

    public int initiateBluetoothProcess(){
        if (bta.isEnabled()) {
            if(btt!=null) {
                btt.cancel();
            }
            //attempt to connect to bluetooth module
            BluetoothSocket tmp;
            mmDevice = bta.getRemoteDevice(MODULE_MAC);

            //create socket
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket = tmp;
                mmSocket.connect();
                Log.i("[BLUETOOTH]", "Connected to: " + mmDevice.getName());
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException c) {
                    return 2; //mmSocket close error
                }
                return 1; //Connection error
            }

            Log.i("[BLUETOOTH]", "Creating handler");
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);
                    if (msg.what == ConnectedThread.RESPONSE_MESSAGE) {
                        String txt = (String) msg.obj;
                        temperatura.append(txt + "\n");
                    }
                }
            };
            Log.i("[BLUETOOTH]", "Creating and running Thread");
            btt = new ConnectedThread(mmSocket, mHandler);
            btt.start();
        } else {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 3);
        }
        return 0;
    }

}