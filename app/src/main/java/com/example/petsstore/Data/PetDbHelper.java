package com.example.petsstore.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PetDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;

    public PetDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a database.....
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetsContract.PetEntry.TABLE_NAME
                +"("
                + PetsContract.PetEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetsContract.PetEntry.COLUMN_PET_NAME +" TEXT NOT NULL, "
                + PetsContract.PetEntry.COLUMN_PET_BREED +" TEXT NOT NULL, "
                + PetsContract.PetEntry.COLUMN_PET_GENDER +" INTEGER NOT NULL DEFAULT 0, "
                + PetsContract.PetEntry.COLUMN_PET_WEIGHT +" INTEGER NOT NULL DEFAULT 0)";
        ;
        //execute sql statement....
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nothing for now as db version id still 1....
    }
}
