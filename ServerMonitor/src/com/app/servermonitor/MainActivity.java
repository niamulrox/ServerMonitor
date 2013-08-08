/*
 * @authors		Garry Ledford, garry.ledford@gmail.com
 * 				Derek Ledford,
 * @version		1.0
 * @since		2013-08-06
 */

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.DerekksFirstApp.message";
	TextView serverStatus;

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		serverStatus = (TextView) findViewById(R.id.statusOfServer_textView);
	}

	/*
	 * Don't know what this does
	 */
	public void showPopup(View v) {
		PopupMenu popup = new PopupMenu(this, v);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.display_message, popup.getMenu());
		popup.show();
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * Runs the SocketFinder function for server checkings
	 * 
	 * @param	View
	 * @return 	None
	 */
	public void checkIfHexxitServerRunning(View view) {
		new SocketFinder().execute("");
	}

	/*
	 * Class for running a thread and checking if a server is active
	 * TODO might want to move this to another class
	 */
	private class SocketFinder extends AsyncTask<String, Void, String> {
		String status = "";

		@Override
		protected String doInBackground(String... arg0) {
			String ipAddress = "75.109.14.220";
			int port = 25565;
			Socket serverSok = null;
			try {
				serverSok = new Socket();
				serverSok.setReuseAddress(true);
				SocketAddress sAddress = new InetSocketAddress(ipAddress, port);
				serverSok.connect(sAddress, 3000);
			} catch (Exception e) {
				if (e instanceof UnknownHostException) {
					//TODO Catch exception
				}
				if (e instanceof SocketTimeoutException) {
					//TODO Catch exception
				}
			} finally {
				if (serverSok != null) {
					if (serverSok.isConnected()) {
						status = "Server is up!";
					} else {
						status = "Server is down!";
					}
					try {
						serverSok.close();
					} catch (IOException e) {

					}
				}
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			TextView tempStatus = (TextView) findViewById(R.id.statusOfServer_textView);
			tempStatus.setText(status);
		}
	}
}
