package com.ioc.eac2;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBInterface {
	public static final String CLAU_ID = "_id";
	public static final String CLAU_NOM = "nom";
	public static final String CLAU_NOM_COMPLET = "nom_complet";
	public static final String CLAU_TWEET = "tweet";
	public static final String CLAU_FAVS = "favs";
	public static final String CLAU_RTS = "rts";
	public static final String CLAU_DATA = "data";
	public static final String CLAU_URL = "url";
	
	public static final String TAG = "DBInterface";
	
	public static final String BD_NOM = "BDTweet";
	public static final String BD_TAULA = "tweets";
	public static final int VERSIO = 3;
	
	public static final String BD_CREATE =
			"create table "+ BD_TAULA + "( " + CLAU_ID + " integer primary key autoincrement, "+ CLAU_NOM + " text , " + CLAU_NOM_COMPLET + " text, " + CLAU_TWEET + " text, " + CLAU_FAVS + " integer, "+ CLAU_RTS + " integer, "+ CLAU_DATA + " text, " + CLAU_URL + " text );";
	private final Context context;
	private AjudaBD ajuda;
	static SQLiteDatabase bd;
	
	public DBInterface(Context con)
	{
		this.context = con;
		ajuda = new AjudaBD(context);
	}
	public DBInterface obre() throws SQLException
	{
		bd = ajuda.getWritableDatabase();
	return this;
	}
	public void insereixTweet(String nom, String nomcomplet, String tweet, int favs, int RT, String data, String url)
			{
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(CLAU_NOM, nom);
			initialValues.put(CLAU_NOM_COMPLET, nomcomplet);
			initialValues.put(CLAU_TWEET, tweet);
			initialValues.put(CLAU_FAVS, favs);
			initialValues.put(CLAU_RTS, RT);
			initialValues.put(CLAU_DATA, data);
			initialValues.put(CLAU_URL, url);
			bd.insert(BD_TAULA ,null, initialValues);
			 
			}
	//Retorna tots els contactes
	public Cursor obtenirTweet()
	{
	return bd.query(BD_TAULA, new String[] {CLAU_ID, CLAU_NOM, CLAU_NOM_COMPLET, CLAU_TWEET, CLAU_FAVS, CLAU_RTS, CLAU_DATA,CLAU_URL}, null, null, null, null, null);
	}
	//Tanca la BD
	public void tanca()
	{
	ajuda.close();
	}
}
