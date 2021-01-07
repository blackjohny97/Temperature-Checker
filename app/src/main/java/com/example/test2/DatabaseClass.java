package com.example.test2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
// Clasa in care se afla toate metodele prin care se modifica baza de date,
// SQLiteOpenHelper este o clasa care ajuta la comunicarea cu baza de date SQLite
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
        //crearea tabelului
        String createTable = "CREATE TABLE " + TABEL_PERSOANE + " (" + COLOANA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLOANA_NUME + " TEXT, " + COLOANA_DATA + " TEXT, " + COLOANA_TEMPERATURA + " FLOAT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
// functia de adaugare a datelor in tabel
    public boolean Adauga (ModelareTabel modelareTabel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
// content values populeaza tabelul in functie de coloane
        cv.put(COLOANA_NUME,modelareTabel.getNume());
        cv.put(COLOANA_DATA,modelareTabel.getData());
        cv.put(COLOANA_TEMPERATURA,modelareTabel.getTemperature());
// se verifica insertia
        long insert = db.insert(TABEL_PERSOANE, null, cv);
        if(insert == -1) {
            return false;
        }
        else{
            return true;
        }
    }
// fuctia de stergere prin apasarea pe obiectul din lista afisata a bazei de date
    public boolean sterge(ModelareTabel modelareTabel){
        SQLiteDatabase db = this.getWritableDatabase();
        String deletestring= "DELETE FROM " + TABEL_PERSOANE + " WHERE " + COLOANA_ID + " = " + modelareTabel.getId();
        Cursor query = db.rawQuery(deletestring, null);
//cursorul este folosit asemenea i-ului din for
//dupa crearea query-ului care cauta in baza de date obiectul selectat se va merge la prima optiune compatibila
// verificand baza de date de la primul obiect
        if(query.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }
//lista care se va afisa pe screen-ul 2, in acesta vor fi afisate toate obiectele introduse in baza de date
    public List<ModelareTabel>getEveryone(){

        List<ModelareTabel> returnList = new ArrayList<>();

        String querrystring = "SELECT * FROM "+ TABEL_PERSOANE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querrystring, null);
//apelarea bazei de date si formeaza un obiect de tip modelare tabel, dupa aceea este pus in lista
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
//lista de search este folosita pentru a cauta in baza de date dupa nume si afisarea listei restranse
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
//metoda folosita pentru a cauta in baza de date dupa nume pentru a lua datele necesare graficului
    public Cursor getData(String persnume){
        SQLiteDatabase db = this.getWritableDatabase();
        String query =  "SELECT * FROM " + TABEL_PERSOANE + " WHERE " + COLOANA_NUME + " LIKE '%" + persnume + "%'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

   /* public Cursor readEntry(){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] allColumns = new String[]{
                DatabaseClass.COLOANA_DATA,
                DatabaseClass.COLOANA_TEMPERATURA,
        };

        Cursor c = db.query(DatabaseClass.TABEL_PERSOANE, allColumns, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }*/
}