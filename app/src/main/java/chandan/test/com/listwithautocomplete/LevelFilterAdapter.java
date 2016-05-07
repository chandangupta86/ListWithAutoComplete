package chandan.test.com.listwithautocomplete;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;




import chandan.test.com.listwithautocomplete.test.paging.GsonListing;

public class LevelFilterAdapter extends BaseAdapter  {
	private ArrayList<GsonListing> mStringList;
	private ArrayList<GsonListing> mStringFilterList;
	private LayoutInflater mInflater;
	
	public LevelFilterAdapter(ArrayList<GsonListing> mStringList, Context context) {
		this.mStringList = mStringList;
		this.mStringFilterList = mStringList;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mStringList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mStringList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder viewHolder;
		if (convertView == null) {
			viewHolder = new Holder();
			convertView = mInflater.inflate(R.layout.autofielditems, null);
			viewHolder.tv_autogradientsname = (TextView) convertView.findViewById(R.id.tv_autogradientsname);
			convertView.setTag(viewHolder);

		} else {

			viewHolder = (Holder) convertView.getTag();
		}
		viewHolder.tv_autogradientsname.setText(mStringList.get(position).name);
		return convertView;
	}
	
	/*public String getDataFromUrl(String term,String user_id,String url)
	{
		try
		{
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("term", term));
			postParameters.add(new BasicNameValuePair("user_id", user_id));

			HttpClient httpClient = HttpsConnectionFactory.createHttpClient();
	        HttpPost request = new HttpPost(url);
	        
	        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
	        request.setEntity(formEntity);
	        HttpResponse response = httpClient.execute(request);
	        String result = EntityUtils.toString(response.getEntity(), "UTF-8");
	        return result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}*/
	
	private class Holder {
		TextView tv_autogradientsname;
	}



}