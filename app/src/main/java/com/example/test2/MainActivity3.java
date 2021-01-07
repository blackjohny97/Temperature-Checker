package com.example.test2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity {

    Button cautare;
    EditText persgrafic;

    LineGraphSeries<DataPoint> series;
    DatabaseClass mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        cautare = findViewById(R.id.cautare);
        persgrafic = findViewById(R.id.persgrafic);

        mDatabaseHelper = new DatabaseClass(this);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();

        cautare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //la apasarea butonului de cautare se va lua numele introdus si se va apela baza de date pentru a lua datele cautate
                String numegrafic = persgrafic.getText().toString();
                Cursor data = mDatabaseHelper.getData(numegrafic);
                String y;
                Float z;
                Date d1;
                //cate intrari exista in baza de date, dupa numele cautat
                //verificarea si scoaterea String-ului de data calendaristica si formatarea pt label-ul din grafic
                int rows = data.getCount();
                data.moveToFirst();
                for(int i = 0;  i <rows; i++){
                    data.moveToNext();
                    String x = data.getString(2);
                    y = data.getString(3);
                    z = Float.parseFloat(y);
                    Date date1 = null;
                    try {
                        date1 = new SimpleDateFormat("dd/MM/yyyy").parse(x);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//adaugarea la grafic a datelor gasite
                    series.appendData(new DataPoint(date1, z), true, 25);
                }
                graph.addSeries(series);
            }
        });

        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    public void onBackPressed(){
        Intent i = new Intent(MainActivity3.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        return;
    }


}