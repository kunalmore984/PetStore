package com.example.petsstore;

import static com.example.petsstore.Data.PetsContract.PetEntry.COLUMN_PET_BREED;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.petsstore.Data.PetsContract;

public class PetCursorAdapter extends CursorAdapter {

    //constructor for adapter class
    public PetCursorAdapter(Context context, Cursor c){
        super(context,c,0);
    }
    // to inflate new view for new data
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }
    // recycle the old view for new data
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView Name =(TextView) view.findViewById(R.id.name);
        TextView Summary= (TextView) view.findViewById(R.id.summary);
        int name_index = cursor.getColumnIndex(PetsContract.PetEntry.COLUMN_PET_NAME);
        int breed_index = cursor.getColumnIndex(COLUMN_PET_BREED);
        String name = cursor.getString(name_index);
        String summary = cursor.getString(breed_index);
        Name.setText(name);
        if (TextUtils.isEmpty(summary)){
            summary = "Unknown breed";
        }
        Summary.setText(summary);
    }
}
