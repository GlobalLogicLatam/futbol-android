package com.globallogic.futbol.core.operation.database;

/**
 * Created by Agustin Larghi on 13/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OperationDatabaseHelper extends SQLiteOpenHelper {
    private static String TAG = "OperationDatabaseHelper"; // Tag just for the LogCat window
    //destination path (location) of our database on device
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private String mDbName;

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public OperationDatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, 1);// 1? its Database Version
        mDbName = dbName;
        this.mContext = context;
    }

    public void createDataBase() throws IOException {
        //If database not exists copy it from the assets
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            try {
                //Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
                openDataBase();
                close();
            } catch (Exception exception) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase() {
        return mContext.getDatabasePath(mDbName).exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(getAssetPath());
        // Path to the just created empty db
        File databaseFile = mContext.getDatabasePath(mDbName);
        if (!databaseFile.getParentFile().exists()) {
            databaseFile.getParentFile().mkdir();
        }
        String outFileName = databaseFile.getAbsolutePath();
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private String getAssetPath() {
        return "databases" + File.separatorChar + mDbName;
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(mContext.getDatabasePath(mDbName).getAbsolutePath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

}
