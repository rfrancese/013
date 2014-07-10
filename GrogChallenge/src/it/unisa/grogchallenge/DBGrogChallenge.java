package it.unisa.grogchallenge;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBGrogChallenge extends SQLiteOpenHelper {
private static final String  DATABASE_NAME = "Sql761525_1";
private static final String  EMAIL = "email";
private static final String  KEY_NAME = "nome";
private static final String  VALUE1 = "punteggio";
private static final String  VALUE2 = "first";
private static final String TABLE = "utente";


	public DBGrogChallenge(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		EMAIL + " TEXT, "+KEY_NAME+" TEXT, "+VALUE1+" INTEGER, "+VALUE2+" INTEGER);");
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
	public void inserisci(String email,String nome,int punteggio){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(EMAIL, email);
	    values.put(KEY_NAME, nome); 
	    values.put(VALUE1,punteggio); 
	    values.put(VALUE2,0); 
		 //Log.d("DBLocal","AGGIUNGO: " + email + " - " + nome + " - " + punteggio);
	    // Inserting Row
	    db.insert(TABLE, null, values);
	    db.close(); // Closing database connection
		
		
	}
	
	
	public int update(Utente utente) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(EMAIL, utente.getEmail());
	    values.put(KEY_NAME, utente.getNome());
	    
	    values.put(VALUE1,utente.getPunteggio());
	    values.put(VALUE2,utente.getFirst());
	    // updating row
	    return db.update(TABLE, values, "id = ?",
	            new String[] { String.valueOf(utente.getId()) });
	}
	
	
	
	
	/*
	 * getContact()
    // Getting single contact
public Contact getContact(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
 
    Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
            KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null)
        cursor.moveToFirst();
 
    Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
            cursor.getString(1), cursor.getString(2));
    // return contact
    return contact;
}
	 */
    
	public ArrayList<Utente> getAll() {
		ArrayList<Utente> utenti = new ArrayList<Utente>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	Utente utente = new Utente();
	        	utente.setId(Integer.parseInt(cursor.getString(0)));
	        	utente.setEmail(cursor.getString(1));
	        	utente.setNome(cursor.getString(2));
	        	utente.setPunteggio(Integer.parseInt(cursor.getString(3)));
	        	utente.setFirst(Integer.parseInt(cursor.getString(4)));
	            // Adding contact to list
	            utenti.add(utente);
	            //Log.d("UTENTE DB", utente.toString());
	            //System.out.println("!!!!UTENTE: " + utente.toString());
	        } while (cursor.moveToNext());
	    }
	    db.close();
	    return utenti;
	    
	
	}


	
	public int getUtentiCount() {
        String countQuery = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        // return count
        int count= cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
	
	
	

}
