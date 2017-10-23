package com.andarpoblacion.andrade.andar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andrade on 12/3/2016.
 */

public class Database extends SQLiteOpenHelper {

    public static final String dbName = "AndarDB1";
    public static final String QRTableName = "QRTbl";
    public static final String EmContTableName = "ContTbl";
    public static final String MessTableName = "MessageTbl";
    public static final String CallTableName = "CallTbl";
    public static final String UserTableName = "UserTbl";
    public static final String UColID = "id";
    public static final String UCol1 = "comp_name";
    public static final String UCol2 = "phone_number";
    public static final String UCol3 = "bday";
    public static final String UCol4 = "address";
    public static final String UCol5 = "email";
    public static final String UCol6 = "password";
    public static final String UCol7 = "status";
    public static final String UCol8 = "userType";
    public static final String UCol9 = "others";
    public static final String QRCol1 = "qr";
    public static final String EmContCol1 = "name";
    public static final String EmContCol2 = "number";
    public static final String MCol1 = "id";
    public static final String MCol2 = "receiver";
    public static final String MCol3 = "oras";
    public static final String MCol4 = "remarks";
    public static final String MCol5 = "body";
    public static final String CCol1 = "id";
    public static final String CCol2 = "contact";
    public static final String CCol3 = "department";
    public static final String CCol4 = "araw";
    public static final String CCol5 = "oras";


    public Database(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+QRTableName+" (qr BLOB)");
        db.execSQL("create table "+EmContTableName+" (name TEXT, number TEXT)");
        db.execSQL("create table "+MessTableName+" (id INTEGER PRIMARY KEY, body TEXT, receiver TEXT, oras TEXT, remarks TEXT)");
        db.execSQL("create table "+CallTableName+" (id INTEGER PRIMARY KEY, contact TEXT, department TEXT, araw TEXT, oras TEXT)");
        db.execSQL("create table "+UserTableName+" (id INTEGER PRIMARY KEY, comp_name TEXT, phone_number TEXT, bday TEXT, address TEXT, email TEXT, password TEXT, status TEXT, userType TEXT, others TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+QRTableName);
        db.execSQL("DROP TABLE IF EXISTS "+EmContTableName);
        db.execSQL("DROP TABLE IF EXISTS "+MessTableName);
        db.execSQL("DROP TABLE IF EXISTS "+UserTableName);
        onCreate(db);
    }

    public boolean insertUser(String id, String comp_name, String phone_number, String bday, String address, String email, String password, String status, String userType){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cont = new ContentValues();
        cont.put(UColID, id);
        cont.put(UCol1, comp_name);
        cont.put(UCol2, phone_number);
        cont.put(UCol3, bday);
        cont.put(UCol4, address);
        cont.put(UCol5, email);
        cont.put(UCol6, password);
        cont.put(UCol7, status);
        cont.put(UCol8, userType);
        long res = db.insert(UserTableName, null, cont);
        if(res == -1)
            return false;
        else
            return true;
    }

    public boolean insertUserOther(String oth){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cont = new ContentValues();
        cont.put(UCol9, oth);
        long res = db.insert(UserTableName, null, cont);
        if(res == -1)
            return false;
        else
            return true;
    }

    public boolean insertMessage(String tanggap, String oras, String rem, String body){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cont = new ContentValues();
        cont.put(MCol2, tanggap);
        cont.put(MCol3, oras);
        cont.put(MCol4, rem);
        cont.put(MCol5, body);
        long res = db.insert(MessTableName, null, cont);
        if(res == -1)
            return false;
        else
            return true;
    }

    public boolean insertCall(String contact, String dept,String araw, String oras){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cont = new ContentValues();
        cont.put(CCol2, contact);
        cont.put(CCol3, dept);
        cont.put(CCol4, araw);
        cont.put(CCol5, oras);
        long res = db.insert(CallTableName, null, cont);
        if(res == -1)
            return false;
        else
            return true;
    }


    public boolean insertQR(byte[] qr){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cont = new ContentValues();
        cont.put(QRCol1, qr);
        long res = db.insert(QRTableName, null, cont);
        if(res == -1)
            return false;
        else
            return true;
    }

    public Cursor getMyQR(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+QRTableName, null);
        return res;
    }

    public void deleteMyQr(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(QRTableName, null, null);
    }

    public boolean insertContact(String name, String num){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cont = new ContentValues();
        cont.put(EmContCol1, name);
        cont.put(EmContCol2, num);
        long res = db.insert(EmContTableName, null, cont);
        if(res == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllUserData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+UserTableName, null);
        return res;
    }

    public Cursor getPreUserData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT comp_name, phone_number, bday, address, email FROM "+UserTableName, null);
        return res;
    }

    public Cursor getPreUserDataOther(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT others FROM "+UserTableName, null);
        return res;
    }

    public Cursor getUserEmail(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT email FROM "+UserTableName, null);
        return res;
    }

    public Cursor getUserNameAddress(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT comp_name, address FROM "+UserTableName, null);
        return res;
    }

    public Cursor getUserType(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT userType FROM "+UserTableName, null);
        return res;
    }

    public Cursor getUserDataForQR(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT comp_name, phone_number, bday, address, email, others FROM "+UserTableName, null);
        return res;
    }

    public Cursor getUserName(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT comp_name FROM "+UserTableName, null);
        return res;
    }

    public Cursor getAllContact(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+EmContTableName, null);
        return res;
    }

    public Cursor getAllMessages(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+MessTableName+" ORDER BY id DESC", null);
        return res;
    }

    public Cursor getAllCalls(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+CallTableName+" ORDER BY id DESC", null);
        return res;
    }

    public Integer deleteDataEmCont(String cont){

        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmContCol2, cont);
        return dbName.delete(EmContTableName, "number=?", new String[]{cont});

    }

    public boolean updateDataEmContDisp (String d, String n){
        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmContCol1,d);
        contentValues.put(EmContCol2,n);
        dbName.update(EmContTableName,contentValues, "number = ?", new String[] {n});
        return true;
    }

    public boolean updateDataEmContNum (String d, String n){
        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmContCol1,d);
        contentValues.put(EmContCol2,n);
        dbName.update(EmContTableName,contentValues, "name = ?", new String[] {d});
        return true;
    }

    public boolean updateUserOther (String email, String others){
        SQLiteDatabase dbName = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UCol5,email);
        contentValues.put(UCol9,others);
        dbName.update(UserTableName,contentValues, "email = ?", new String[] {email});
        return true;
    }


}
