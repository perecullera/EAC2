package com.ioc.eac2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements OnItemClickListener {
	
	// Constants personals
	static final String TWITTER_API_KEY = "3osOY45EfHgAaM7ancZUlg";
	static final String TWITTER_API_SECRET = "cNc5PDqKMU0SAF1RRHUUgpaJ5UNjv54AEl3v51ricoI";

	 // Aquesta és la que hem definit abans al scheme
	 static final String CALLBACK = "tinytwit:///";
	 
	 ListView lv;
	 //
	 TwitterFactory factory;
	 Twitter twitter;
	 private static RequestToken requestToken;
	 AccessToken accessToken;
	 
	 String verifier;
	 
	 List<String> tweets;
	 
	 List<twitter4j.Status> LlistaTweets ;
	 
	 ProgressBar progress;
	 
	 static DBInterface bd1;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progress = (ProgressBar)findViewById(R.id.progress);
		 progress.setIndeterminate(true);
		//progress.setVisibility(View.INVISIBLE);
		lv = (ListView)findViewById(R.id.list);
		lv.setOnItemClickListener(this);
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(TWITTER_API_KEY);
		cb.setOAuthConsumerSecret(TWITTER_API_SECRET);
		Configuration conf = cb.build();
		factory = new TwitterFactory(conf);
		twitter = factory.getInstance();
		tweets = new ArrayList<String>();
		new autoritza().execute();
	}
	protected class autoritza extends AsyncTask <Void, Void, Void>{

		@Override
		protected Void doInBackground(Void...args ) {
			// TODO Auto-generated method stub
			try {
				//Twitter twitter = twitters[0];
				requestToken = twitter.getOAuthRequestToken(CALLBACK);
				String rt = requestToken.getToken();
				rt = requestToken.getAuthenticationURL();
				Log.d(rt, rt);
				return null;
				 //System.out.println(requestToken.getToken()); 
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.print(e);
			}
			return null;
		}
		@Override
		protected void onPostExecute (Void res){
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
			startActivity(intent);
		}
		
		
	}
	@Override
		protected void onNewIntent(Intent intent) {


		Uri uri = intent.getData();
		// Si no és null i comença amb "tinytwit:///"
		if (uri != null && uri.toString().startsWith(CALLBACK)) {

			// Guardem el verificador (és un String)
			 verifier = uri.getQueryParameter("oauth_verifier");

			// AsyncTask per obtenir l'AccesToken
			new AccessTokenAsyncTask().execute(requestToken,  verifier);
		}
	}
	public class AccessTokenAsyncTask extends AsyncTask<Object, Void, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
				new obtenirTuits().execute(tweets);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
	
	public class obtenirTuits  extends AsyncTask <List<String>, Integer, List<String> > {
		
		 @Override
		protected void onPreExecute()
		    {
			 //progress.setVisibility(View.VISIBLE);    
		    };
		@Override
		protected List<String> doInBackground(List<String>... params) {
			// TODO Auto-generated method stub
			//Twitter[] twt = tw;
			params[0].clear();
			
			twitter4j.Status unTweet;//Unallistadetweets
			//Paging paging = new Paging(50);
			try {
				LlistaTweets = twitter.getHomeTimeline();
				ListIterator<twitter4j.Status> i = LlistaTweets.listIterator();
				int myProgressCount = 0;
				while(i.hasNext())
				{
				//Obtenimuntweet
				unTweet = i.next();
				 
				//Obtenim el text
				String t = unTweet.getText();
				String a = unTweet.getUser().getScreenName();
				String s = a+": "+t;
				 
				//Afegim a lallista d'Strings deresultat
				params[0].add(s);
				myProgressCount =+5;
				publishProgress(myProgressCount);
				}
				
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return tweets;
		}
		@Override
	    protected void onProgressUpdate(Integer... values) {			
				progress.setProgress(values[0]);
	    }
		protected void onPostExecute(List<String> result){		
			lv.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.element_llista, result));
			progress.setVisibility(View.GONE);
		}
		
	}
	public void consultaTuit (int i){
		twitter4j.Status tweet;
		tweet =	LlistaTweets.get(i);
		guardaTweet(tweet);
		Intent intent = new Intent("com.ioc.eac2.Twitejar");
		startActivity(intent);
	}
	public void guardaTweet(Status tweet) {
		// TODO Auto-generated method stub
		String nom = tweet.getUser().getScreenName();
		String nomcomplet =tweet.getUser().getName();
		String tweettxt = tweet.getText();
		int favs = tweet.getFavoriteCount();
		int rts = tweet.getRetweetCount();
		String data = tweet.getCreatedAt().toString();
		String url = tweet.getURLEntities().toString();
		
		bd1 = new DBInterface(this);
		bd1.obre();
		bd1.insereixTweet(nom,nomcomplet,tweettxt,favs,rts,data,url);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		// TODO Auto-generated method stub
		Object listItem = lv.getItemAtPosition(position);
		consultaTuit(position);
	}

}
