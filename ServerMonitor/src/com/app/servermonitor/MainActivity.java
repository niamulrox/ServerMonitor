package com.app.servermonitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE ="com.example.DerekksFirstApp.message";
	TextView serverStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		serverStatus = (TextView)findViewById(R.id.textView1);
	}
	public void showPopup(View v) {
	    PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.display_message, popup.getMenu());
	    popup.show();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
/*	*//** Called when the user clicks the Send button *//*
	public void sendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}*/
	
	public void sendMessage(View view) {
		
	}
	public void checkIfHexxitServerRunning(View view) {
		new SocketFinder().execute("");
	}

	private class SocketFinder extends AsyncTask<String, Void, String> {
		String status = "";
		@Override
		protected String doInBackground(String... arg0) {
			Log.w("DEREK", "Checking for server");
			String ipAddress = "75.109.14.220";
			int port = 25565;
			Socket serverSok = null;
			try {
				serverSok = new Socket();
				serverSok.setReuseAddress(true);
				SocketAddress sAddress = new InetSocketAddress(ipAddress, port);
				serverSok.connect(sAddress, 3000);

				Log.w("DEREK", "Port in use: " + port);
			} catch (Exception e) {
				if( e instanceof UnknownHostException){
					Log.w("DEREK", "Unknown Host");
				}
				if( e instanceof SocketTimeoutException ){
					Log.w("DEREK", "Connect timeout");
				}
			} finally {
				if (serverSok != null){
					if(serverSok.isConnected()){
						status = "Server is up!";
						//serverStatus.setText("Connection established");
					} else {
						status = "Server is down!";
						//serverStatus.setText("Connection failed, port not reachable");
					}
					try {
						serverSok.close();
					} catch (IOException e){
						
					}
				}
			}
			//Log.w("DEREK", "The selected port is not in use");
			return null;
		}
		
	@Override
	protected void onPostExecute(String result) {
		TextView tempStatus = (TextView)findViewById(R.id.textView1);
		tempStatus.setText(status);
	}
	}
}
