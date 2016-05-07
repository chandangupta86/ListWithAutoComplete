package chandan.test.com.listwithautocomplete;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import chandan.test.com.listwithautocomplete.Connection.HttpsConnectionFactory;


public class WebTask extends AsyncTask<ArrayList<NameValuePair>, Void, String> {
	
	Context _context;
	String _activityURL;
	private ProgressDialog _dialog;
	private String _dialogText;
	private int mShowLoaderText = 1;
	public HttpClient client;
	public WebTask(Context context, String activityURL) {
		_context = context;
		_activityURL = activityURL;
		_dialogText = "Loading...";
	}
	
	public WebTask(Context context, String activityURL,String dialogText) {
		_context = context;
		_activityURL = activityURL;
		_dialogText = dialogText;
	}
	
	public WebTask(Context context, String activityURL , int showLoaderText) {
		_context = context;
		_activityURL = activityURL;
		_dialogText = "";
		mShowLoaderText = showLoaderText;
	}
	
	@Override
	protected void onPreExecute() {
		Log.e("test","showing dialog");
		if(mShowLoaderText==1)
			_dialog = ProgressDialog.show(_context, "", _dialogText, true);
		
	}

    @Override
    protected String doInBackground(ArrayList<NameValuePair>... uri) {
        try {
        	for(int i=0;i<uri[0].size();i++)
        	{
        		NameValuePair temp=uri[0].get(i);

        	}

        	client = HttpsConnectionFactory.createHttpClient();
            HttpPost request = new HttpPost(_activityURL);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(uri[0]);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            return result;
            
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
        if(mShowLoaderText==1)
          _dialog.dismiss();
    }
}