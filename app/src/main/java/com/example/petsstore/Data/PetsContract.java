package com.example.petsstore.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PetsContract {

    private PetsContract(){
    }

    //Pet Provider Constants
    public static final String CONTENT_AUTHORITY = "com.example.petsstore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_PETS = "pets";

    //Class for pets table in the database....
    public static final class PetEntry implements BaseColumns {
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PETS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PETS;
        public static final String TABLE_NAME = "pets";
        public static final String _ID = "_id";
        public static final String COLUMN_PET_NAME = "Pet_name";
        public static final String COLUMN_PET_BREED = "Pet_breed";
        public static final String COLUMN_PET_GENDER = "Pet_gender";
        public static final String COLUMN_PET_WEIGHT = "pet_weight";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);
        //possible weight of the pets.....
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_UNKNOWN =3;

        public static boolean isValidGender(int gender){
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }

    }
}
