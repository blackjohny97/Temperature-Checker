package com.example.test2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button scan, Databtn, butongrafice;
    EditText NameTxt;
    TextView DateTxt;
    MainActivity2 PaginaDoi = new MainActivity2();
    MainActivity3 PaginaTrei = new MainActivity3();
    private static final String TAG ="MainActivity";
    public DatePickerDialog.OnDateSetListener mDateSetListener;
// butonul scan este cel care adauga in baza de date,
// butonul Databtn este cel care face tranzitia la screen-ul 2 in care este baza de date,
// butonul butongrafice este cel pentru tranzitia la screen-ul cu grafice,
// datapicker este utilizat pentru un pop-up dialog pentru a alege data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan = findViewById(R.id.scan);
        Databtn = findViewById(R.id.DataBtn);
        NameTxt = findViewById(R.id.NameTxt);
        butongrafice = findViewById(R.id.butongrafice);

        DateTxt = (TextView)findViewById(R.id.DateTxt);

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
            @Override
            public void onClick(View v) {
//butonul care adauga in baza de date formand un obiect de tip modelare tabel pentru a fi introdus in baza de date,
// valoarea float 0.0 va fi inlocuita de o variabila double care va fi primita prin bluetooth de la un Arduino(modificare care mai trebuie facuta)
                ModelareTabel modelareTabel;

                try {
                    modelareTabel = new ModelareTabel(-1, NameTxt.getText().toString(), DateTxt.getText().toString(), (float) 0.0);
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

    }
}