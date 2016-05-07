package chandan.test.com.listwithautocomplete;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;



import com.google.gson.Gson;

import chandan.test.com.listwithautocomplete.test.paging.GsonListing;
import chandan.test.com.listwithautocomplete.test.paging.GsonResponseData;
import chandan.test.com.listwithautocomplete.test.paging.GsonSummary;

public class MainActivity extends Activity {
    ListView lv_cat;
    EditText et_serach;
    ProgressBar pb_search;
    int currentPage = 1;
    GsonSummary mGsonSummary;
    View footerView;
    LevelFilterAdapter mLevelFilterAdapter;                                                // adapter
    ArrayList<GsonListing> autofielditems;                                                 // response data
    ArrayList<GetAutoCompleteData> Arr_Lst_Task1 = new ArrayList<GetAutoCompleteData>();   // ArrayList for http task
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		mGsonSummary = new GsonSummary();
		if (autofielditems == null) {
			autofielditems = new ArrayList<GsonListing>();
		} else {
			if (autofielditems.size() <= 0) {
				autofielditems = new ArrayList<GsonListing>();
			}
		}
		mLevelFilterAdapter = new LevelFilterAdapter(autofielditems, getApplicationContext());
		lv_cat.setAdapter(mLevelFilterAdapter);
	}

	
	 // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
      // This method probably sends out a network request and appends new data items to your adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
    	int currentPage = 1; 
    	int lastPage = 100; 
    	try
    	{
    		currentPage = Integer.parseInt(mGsonSummary.currentPage); 
        	lastPage = Integer.parseInt(mGsonSummary.pageCount); 
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	Log.e("test","currentPage="+currentPage+" lastPage="+lastPage);
    	if((currentPage+1)<=lastPage)
    	{
    		String url = "http://api.sandbox.yellowapi.com/FindBusiness/?what="+et_serach.getText().toString()+"&where=Toronto&pgLen=20&pg="+(currentPage+1)+"&dist=1&fmt=JSON&lang=en&UID="+System.currentTimeMillis()+"&apikey=jmtwnfv3cpxy8kxtc7ahz7hv";
    		GetAutoCompleteData task1 = new GetAutoCompleteData(MainActivity.this,url,true);
    		Arr_Lst_Task1.add(task1);
    		for (int i = 0; i < Arr_Lst_Task1.size() - 1; i++) {
    			if (Arr_Lst_Task1.get(i).getStatus() == Status.RUNNING) {
    				Arr_Lst_Task1.get(i).cancel(true);
    			}
    			Arr_Lst_Task1.remove(i);
    		}
    		task1.execute();
    	}
    	else
    	{
    		Log.e("test","customLoadMoreDataFromApi=");
    	}
    }

	public void initView()
	{
		pb_search = (ProgressBar)findViewById(R.id.pb_search);
		lv_cat    = (ListView)findViewById(R.id.lv_cat);
		footerView =  ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
		lv_cat.addFooterView(footerView);
        
		lv_cat.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
		    	Log.e("test","onLoadmore......."+totalItemsCount);
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
		        customLoadMoreDataFromApi(page); 
	                // or customLoadMoreDataFromApi(totalItemsCount); 
		    }
	    });
		
	    
		et_serach = (EditText)findViewById(R.id.et_serach);
		et_serach.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				try {
					String val = s.toString();
					try
					{
						mGsonSummary.currentPage = "1";
					}
					catch(Exception e){
						e.printStackTrace();
					}
					String url = "http://api.sandbox.yellowapi.com/FindBusiness/?what="+val+"&where=Toronto&pgLen=20&pg=2&dist=1&fmt=JSON&lang=en&UID="+System.currentTimeMillis()+"&apikey=jmtwnfv3cpxy8kxtc7ahz7hv";
					GetAutoCompleteData task1 = new GetAutoCompleteData(MainActivity.this,url,false);
					Arr_Lst_Task1.add(task1);
					for (int i = 0; i < Arr_Lst_Task1.size() - 1; i++) {
						if (Arr_Lst_Task1.get(i).getStatus() == Status.RUNNING) {
							Arr_Lst_Task1.get(i).cancel(true);
						}
						Arr_Lst_Task1.remove(i);
					}
					task1.execute();
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("test","error "+e.getMessage());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() >= 3) {
				} else {
					autofielditems.clear();
				}

			}
		});
	}

	private class GetAutoCompleteData extends FetchGetDataWebTask 
	{
		Boolean mIsLoadMore = false;
		public GetAutoCompleteData(Context context, String activityURL,Boolean isLoadMore) 
		{
			super(context, activityURL,false);
			mIsLoadMore = isLoadMore;
		}
		
		protected void onPreExecute() {
			if(mIsLoadMore)
			{
				footerView.setVisibility(View.VISIBLE);
				pb_search.setVisibility(View.GONE);
			}
			else
			{
				footerView.setVisibility(View.GONE);
				pb_search.setVisibility(View.VISIBLE);
			}
				
			
		}

		protected void onPostExecute(String result) {
			footerView.setVisibility(View.GONE);
			pb_search.setVisibility(View.GONE);
			
			if(!mIsLoadMore)
			   autofielditems.clear();
			try {
				Log.e("test","reposnse is "+result);
				GsonResponseData mGsonResponseData = new Gson().fromJson(result,GsonResponseData.class);
				mGsonSummary = mGsonResponseData.summary;
				for (GsonListing temp : mGsonResponseData.listings) {
					autofielditems.add(temp);
				}
				mLevelFilterAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
