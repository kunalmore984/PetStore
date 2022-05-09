package com.example.petsstore;

import static com.example.petsstore.Data.PetsContract.PetEntry.COLUMN_PET_BREED;
import static com.example.petsstore.Data.PetsContract.PetEntry.COLUMN_PET_GENDER;
import static com.example.petsstore.Data.PetsContract.PetEntry.COLUMN_PET_NAME;
import static com.example.petsstore.Data.PetsContract.PetEntry.COLUMN_PET_WEIGHT;
import static com.example.petsstore.Data.PetsContract.PetEntry.CONTENT_URI;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petsstore.Data.PetDbHelper;
import com.example.petsstore.Data.PetsContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//Catalog activity......
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PetDbHelper mPetDbHelper;
    private String TAG = CatalogActivity.class.getName();
    private static final int URL_LOADER = 0;
    PetCursorAdapter mpetCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        mpetCursorAdapter = new PetCursorAdapter(this,null);

        // Find the ListView which will be populated with the pet data
        ListView petList = (ListView)findViewById(R.id.pet_list);
        petList.setAdapter(mpetCursorAdapter);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petList.setEmptyView(emptyView);
        mPetDbHelper = new PetDbHelper(CatalogActivity.this);
        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
        petList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PetEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.pets/pets/2"
                // if the pet with ID 2 was clicked on.
                Uri currentPetUri = ContentUris.withAppendedId(PetsContract.PetEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(URL_LOADER,null,this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                inserPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    private void inserPet(){
        //using content values.
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PET_NAME,"moto");
        contentValues.put(COLUMN_PET_BREED,"Terrier");
        contentValues.put(COLUMN_PET_GENDER,1);
        contentValues.put(COLUMN_PET_WEIGHT,7);

        Uri link = getContentResolver().insert(CONTENT_URI,contentValues);
        Log.e(TAG,"Catalog activity : "+link);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PetsContract.PetEntry._ID,
                PetsContract.PetEntry.COLUMN_PET_NAME,
                PetsContract.PetEntry.COLUMN_PET_BREED,
                };
        // Perform a query on the provider using the Conte
        return new CursorLoader(this,CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //
        mpetCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mpetCursorAdapter.swapCursor(null);
    }
    //Delete the pet methods....
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_All_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*
     * Perform the deletion of the pet in the database.
     */
    private void deletePet() {
        // TODO: Implement this method
        int rowsdeleted = 0;
        rowsdeleted = getContentResolver().delete(CONTENT_URI,null,null);
        if (rowsdeleted ==0 ){
            Toast.makeText(this,R.string.editor_delete_pet_failed,Toast.LENGTH_LONG).show();
            Log.v(TAG, "Number of rows deleted : "+rowsdeleted);
        }else {
            Toast.makeText(this,R.string.editor_delete_pet_successful,Toast.LENGTH_LONG).show();
            Log.v(TAG, "Number of rows deleted : "+rowsdeleted);
        }
    }
}