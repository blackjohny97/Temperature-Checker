package com.example.test2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity3 extends AppCompatActivity {

    Button cautare;
    GraphView graph;
    EditText persgrafic;
    DatabaseClass data;
    SQLiteDatabase db;

    LineGraphSeries<DataPoint> cardinale = new LineGraphSeries<>(new DataPoint[0]);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        cautare = findViewById(R.id.cautare);
        graph = findViewById(R.id.graph);
        persgrafic = findViewById(R.id.persgrafic);

        data = new DatabaseClass(MainActivity3.this);
        db = data.getWritableDatabase();

        graph.addSeries(cardinale);

        cautare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numegrafic = persgrafic.getText().toString();

                cardinale.resetData(GrabData());
            }
        });
    }

    private DataPoint[] GrabData(){
        String[] coloane = {"COLOANA_TEMPERATURA", "COLOANA_DATA"};
        @SuppressLint("Recycle") Cursor cursorul = db.query("TABEL_PERSOANE", coloane, null, null,null,null,null);
        DataPoint[] dataPoints = new DataPoint[cursorul.getCount()];

        for(int i = 0; i<cursorul.getCount(); i++){
            cursorul.moveToNext();
            dataPoints[i] = new DataPoint(cursorul.getFloat(0), Double.parseDouble(cursorul.getString(1)));
        }
        return dataPoints;
    }
    public void onBackPressed(){
        Intent i = new Intent(MainActivity3.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        return;
    }


}