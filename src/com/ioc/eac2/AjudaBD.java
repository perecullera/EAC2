package com.ioc.eac2;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ioc.eac2.DBInterface;

public class AjudaBD extends SQLiteOpenHelper
{
	AjudaBD (Context con)
{
			
		super(con, DBInterface.BD_NOM, null, DBInterface.VERSIO);
}
 
@Override
public void onCreate(SQLiteDatabase db)
{
try
{
	db.execSQL(DBInterface.BD_CREATE);
}catch(SQLException e)
{
	e.printStackTrace();
}
}
 
@Override
public void onUpgrade(SQLiteDatabase db, int VersioAntiga, int VersioNova)
{
	Log.w(DBInterface.TAG, "Actualitzant Base de dades de la versió" + VersioAntiga + " a " + VersioNova + ". Destruirà totes les dades");
	db.execSQL("DROP TABLE IF EXISTS " + DBInterface.BD_TAULA);
 
	onCreate(db);
}
}
