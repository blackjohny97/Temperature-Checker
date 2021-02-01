package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    ListView tabel;
    ArrayAdapter arrayAdapter;
    DatabaseClass databaseClass;
    Button btncautare;
    EditText numecautat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tabel = findViewById(R.id.tabel);
        btncautare = findViewById(R.id.btncautare);
        numecautat = findViewById(R.id.numecautat);

        databaseClass = new DatabaseClass(MainActivity2.this);
        ArataPersoaneInLista(databaseClass);

        tabel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelareTabel perselectata = (ModelareTabel) parent.getItemAtPosition(position);
                databaseClass.sterge(perselectata);
                ArataPersoaneInLista(databaseClass);
                Toast.makeText(MainActivity2.this,"S-a sters " + perselectata.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btncautare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cautat = numecautat.getText().toString();
                databaseClass.search(cautat);
                ArataPersoanacautata(databaseClass,cautat);
            }
        });
    }

    public void ArataPersoaneInLista(DatabaseClass databaseClass2) {
        arrayAdapter = new ArrayAdapter<ModelareTabel>(MainActivity2.this, android.R.layout.simple_list_item_1, databaseClass2.getEveryone());
        tabel.setAdapter(arrayAdapter);
    }

    public void ArataPersoanacautata(DatabaseClass databaseClass2, String cautatul) {
        arrayAdapter = new ArrayAdapter<ModelareTabel>(MainActivity2.this, android.R.layout.simple_list_item_1, databaseClass2.search(cautatul));
        tabel.setAdapter(arrayAdapter);
    }

    public void onBackPressed(){
        Intent i = new Intent(MainActivity2.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        return;
    }

}