package chandan.test.com.listwithautocomplete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class FetchGetDataWebTask extends AsyncTask<Void, Void, String> {
	
	Context _context;
	String _activityURL;
	private ProgressDialog _dialog;
	private Boolean mShowDialog;
	
	public FetchGetDataWebTask(Context context, String activityURL) {
		_context = context;
		_activityURL = activityURL;
		mShowDialog = true;
	}
	
	public FetchGetDataWebTask(Context context, String activityURL,Boolean showDialog) {
		_context = context;
		_activityURL = activityURL;
		mShowDialog = showDialog;
	}
	
	@Override
	protected void onPreExecute(){
		if(mShowDialog)
		 _dialog = ProgressDialog.show(_context, "", "Loading...", true);
	}

    @Override
    protected String doInBackground(Void... uri) {
		StringBuffer chaine = new StringBuffer("");
        try {

				URL url = new URL(_activityURL);
				Log.e("test",_activityURL);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestProperty("User-Agent", "");
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.connect();

			InputStream inputStream = connection.getInputStream();

			BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			while ((line = rd.readLine()) != null) {
				chaine.append(line);
			}
			return chaine.toString();
        } catch (ClientProtocolException e) {
            Log.i("ClientProtocolException", e+"");
        } catch (IOException e) {
        	Log.i("IOException", e+"");
        }
		return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
        	if(mShowDialog)
        	{
        		_dialog.dismiss();
            	_dialog = null;
        	}
        } catch (Exception e) {
            // nothing
        }
        //finish();
    }//;
}