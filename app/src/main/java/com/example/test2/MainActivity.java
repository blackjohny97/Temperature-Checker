package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button scan, Databtn, butongrafice;
    EditText NameTxt, DateTxt;
    MainActivity2 PaginaDoi = new MainActivity2();
    MainActivity3 PaginaTrei = new MainActivity3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan = findViewById(R.id.scan);
        Databtn = findViewById(R.id.DataBtn);
        NameTxt = findViewById(R.id.NameTxt);
        DateTxt = findViewById(R.id.DateTxt);

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

                boolean adaugat = databaseClass.Adauga(modelareTabel);
                Toast.makeText(MainActivity.this, "A mers" + adaugat, Toast.LENGTH_SHORT).show();



            }
        });

        butongrafice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity3.class);
                startActivity(intent);
            }
        });

    }
}