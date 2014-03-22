package com.ioc.eac2;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class Twitejar extends Activity {
	private Cursor cursor;
	TextView twttext;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.tweet);
	twttext = (TextView) findViewById(R.id.textView1);
	cursor = MainActivity.bd1.obtenirTweet();
	cursor.moveToFirst();
	twttext.setText(cursor.getString(1));
	}
	 
	
	
}