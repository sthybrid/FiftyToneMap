package com.sthybrid.fiftytonemap.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {

	//Path to the device folder with database
	public static String DB_PATH;
	
	//Database filename
	public static String DB_NAME;
	public SQLiteDatabase database;
	public final Context context;

	public SQLiteDatabase getDb() {
	    return database;
	}

	public MyDatabaseOpenHelper(Context context, String databaseName) {
	    super(context, databaseName, null, 1);
	    this.context = context;
	        DB_NAME = databaseName;
            DB_PATH =context.getDatabasePath(DB_NAME).getPath() ;
	    openDataBase();

	}

	// Create a database if its not yet created
	public void createDataBase() {
	    boolean dbExist = checkDataBase();
	    if (!dbExist) {
	        this.getReadableDatabase();
	        try {
	            copyDataBase();
	        } catch (IOException e) {
	            Log.e(this.getClass().toString(), "Copying error!");
	            throw new Error("Error copying database!");
	        }
	    } else {
	        Log.i(this.getClass().toString(), "Database already exists");
	    }
	}

	//Performing a database existence check
	private boolean checkDataBase(){
	    SQLiteDatabase checkDb = null;
	    try {
	        checkDb = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
	    } catch (SQLException e){
	        Log.e(this.getClass().toString(), "Error while checking db");
	    }

	    if (checkDb != null){
	        checkDb.close();
	    }
	    return checkDb !=null;
	}

	private void copyDataBase() throws IOException {
		
	    //Open a stream for reading, located in the assets
	    InputStream externalDbStream = context.getAssets().open(DB_NAME);
	    
	    
	    //Create a stream for writing the database byte by byte
	    OutputStream localDbStream = new FileOutputStream(DB_PATH);

	    //Copy the database
	    byte[] buffer = new byte[1024];
	    int bytesRead;
	    while((bytesRead = externalDbStream.read(buffer)) > 0){
	        localDbStream.write(buffer, 0, bytesRead);
	    }

	    //Flush the out stream
	    localDbStream.flush();

	    //Close the streams
	    localDbStream.close();
	    externalDbStream.close();
	}

	SQLiteDatabase openDataBase() throws SQLException {

	    if (database == null) {
	        createDataBase();
	        database = SQLiteDatabase.openDatabase(DB_PATH, null,
	                SQLiteDatabase.OPEN_READWRITE);
	    }
	    return database;
	}

	@Override
	public synchronized void close(){
	    if (database != null){
	        database.close();
	    }
	    super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    // TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    // TODO Auto-generated method stub

	}
}
