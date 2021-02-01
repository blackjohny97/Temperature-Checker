package com.example.test2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseClass extends SQLiteOpenHelper {

    public static final String TABEL_PERSOANE = "TABEL_PERSOANE";
    public static final String COLOANA_NUME = "NUME";
    public static final String COLOANA_DATA = "DATA";
    public static final String COLOANA_TEMPERATURA = "TEMPERATURA";
    public static final String COLOANA_ID = "ID";

    public DatabaseClass(@Nullable Context context) {
        super(context, "persoane.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABEL_PERSOANE + " (" + COLOANA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLOANA_NUME + " TEXT, " + COLOANA_DATA + " TEXT, " + COLOANA_TEMPERATURA + " FLOAT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean Adauga (ModelareTabel modelareTabel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLOANA_NUME,modelareTabel.getNume());
        cv.put(COLOANA_DATA,modelareTabel.getData());
        cv.put(COLOANA_TEMPERATURA,modelareTabel.getTemperature());

        long insert = db.insert(TABEL_PERSOANE, null, cv);
        if(insert == -1) {
            return false;
        }
        else{
            return true;
        }
    }

    public boolean sterge(ModelareTabel modelareTabel){
        SQLiteDatabase db = this.getWritableDatabase();
        String deletestring= "DELETE FROM " + TABEL_PERSOANE + " WHERE " + COLOANA_ID + " = " + modelareTabel.getId();
        Cursor query = db.rawQuery(deletestring, null);

        if(query.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public List<ModelareTabel>getEveryone(){

        List<ModelareTabel> returnList = new ArrayList<>();

        String querrystring = "SELECT * FROM "+ TABEL_PERSOANE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querrystring, null);

        if(cursor.moveToFirst()){
            do{

                int pid = cursor.getInt(0);
                String pnume = cursor.getString(1);
                String pdata = cursor.getString(2);
                float ptemp = cursor.getFloat(3);

                ModelareTabel persoana = new ModelareTabel(pid,pnume,pdata,ptemp);
                returnList.add(persoana);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<ModelareTabel>search(String nume){

        List<ModelareTabel> cautatlist = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String searchstring = "SELECT * FROM " + TABEL_PERSOANE + " WHERE " + COLOANA_NUME + " LIKE '%" + nume + "%'";
        Cursor cursor1 = db.rawQuery(searchstring, null);

        if(cursor1.moveToFirst()) {
            do {
                int pcid = cursor1.getInt(0);
                String pcnume = cursor1.getString(1);
                String pcdata = cursor1.getString(2);
                float pctemp = cursor1.getFloat(3);

                ModelareTabel persoana = new ModelareTabel(pcid,pcnume,pcdata,pctemp);
                cautatlist.add(persoana);

            } while (cursor1.moveToNext());
        }
        cursor1.close();
        db.close();
        return cautatlist;
    }

    public Cursor getData(String persnume){
        SQLiteDatabase db = this.getWritableDatabase();
        String query =  "SELECT * FROM " + TABEL_PERSOANE + " WHERE " + COLOANA_NUME + " LIKE '%" + persnume + "%'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}