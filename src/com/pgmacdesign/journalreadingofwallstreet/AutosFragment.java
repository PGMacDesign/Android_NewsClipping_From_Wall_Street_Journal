package com.pgmacdesign.journalreadingofwallstreet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
//import com.google.ads.AdView;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.pgmacdesign.journalreadingofwallstreet.RefreshableInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AutosFragment extends Fragment implements RefreshableInterface {
	
	ListView earnings_list;
	TextView earnings_updated_as_of;
	static TextView earnings_temp_text_view;
	
	private enum RSSXMLTag {
		TITLE, DATE, LINK, CONTENT, GUID, IGNORETAG;
	}

	private ArrayList<PostData> listData;
	private String urlString = "http://online.wsj.com/xml/rss/3_7088.xml"; //Adjust the URL string here for the XML parser
	private RefreshableListView postListView;
	private PostItemAdapter postAdapter;
	private int pagnation = 1; // start from 1
	private boolean isRefreshLoading = true;
	private boolean isLoading = false;
	private ArrayList<String> guidList;

	//private AdView adView;
	private Tracker googleTracker;
	private GoogleAnalytics googleAnalytics;
	private final static String PREFERENCE_FILENAME = "journalreadingofwallstreet";
	private Intent postviewIntent;

	//private static Context mContext;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_postlist, container, false);
		// View rootView = inflater.inflate(R.layout.fragment_earnings,
		// container, false);
		
		//setUpdatedTime();
		
		//googleAnalytics = GoogleAnalytics.getInstance();
		//googleTracker = googleAnalytics.getTracker("UA-23293636-5");

		// check installation
		/*  For checking on googletracker stuff
		SharedPreferences settings = this.getActivity().getSharedPreferences(PREFERENCE_FILENAME,
				0);
		boolean isFirstRun = settings.getBoolean("isFirstRun", false);
		if (!isFirstRun) {
			// google analytics
			googleTracker.sendEvent("installation", "install", null, null);

			isFirstRun = true;
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("isFirstRun", isFirstRun);

			// Commit the edits!
			editor.commit();
		}
		*/

		// add google admob
		//Commenting out for now
		
		/*
		adView = new AdView(this, AdSize.SMART_BANNER, "a151cfacdc9a91e");
		LinearLayout adContainer = (LinearLayout) this
				.findViewById(R.id.adsContainer);
						adContainer.addView(adView);
		AdRequest adRequest = new AdRequest();
		Set<String> keywordsSet = new HashSet<String>();
		keywordsSet.add("game");
		keywordsSet.add("dating");
		keywordsSet.add("money");
		keywordsSet.add("girl");
		adRequest.addKeywords(keywordsSet);
		adView.loadAd(adRequest);	 
		*/
		
		guidList = new ArrayList<String>();
		listData = new ArrayList<PostData>();
		postListView = (RefreshableListView) rootView.findViewById(R.id.postListView);
		postAdapter = new PostItemAdapter(this.getActivity(), R.layout.postitem, listData);
		postListView.setAdapter(postAdapter);
		//postListView.setOnRefresh(this);     /////////////////////Not working atm :(
		postListView.onRefreshStart();
		postListView.setOnItemClickListener(onItemClickListener);
		
		L.myLog("Line 140 Works");////////////////////////////////////
		startFresh();
		L.myLog("Line 142 Works");////////////////////////////////////
		startLoadMore();
		L.myLog("Line 144 Works");////////////////////////////////////
		return rootView;

	}

	
	
	/**
	 * ListView Item Onclick Handler
	 * 
	 * When user clicks on an item in LiveView, this function will be called.
	 * It will switch to PostViewActivity with setting content and link in Bundle.
	 * 
	 */
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			PostData data = listData.get(arg2 - 1);

			Bundle postInfo = new Bundle();
			postInfo.putString("content", data.postContent);
			postInfo.putString("link", data.postLink);

			//Cant get this to work :(  //////////////////////////////////////////////////////////////////////////////////
		 	//This section of code is essential to passing the address to the web browser and creating an intent out
			if (postviewIntent == null) {
				postviewIntent = new Intent(getActivity(), PostViewActivity.class);
				L.myLog("Line 174 Works");////////////////////////////////////
			}
			L.myLog("Line 176 Works");////////////////////////////////////  
			postviewIntent.putExtras(postInfo);
			L.myLog("Line 178 Works");////////////////////////////////////
			startActivity(postviewIntent);
		}
	};

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.actionAbout:
			String appString = null;
			try {
				
				appString = getActivity().getPackageManager().getPackageInfo(
						//"com.pgmacdesign.journalreadingofwallstreet", 0).versionName;
						getActivity().getPackageName(), 0).versionName;
				appString = "Journal Reader of Wall Street " + appString;
			} catch (NameNotFoundException e) {
				Toast.makeText(this.getActivity(), "Get Version Name Error", Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(this.getActivity(), appString, Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class RssDataController extends
			AsyncTask<String, Integer, ArrayList<PostData>> {
		private RSSXMLTag currentTag;

		@Override
		protected ArrayList<PostData> doInBackground(String... params) {
			// TODO Auto-generated method stub
			L.myLog("Line 218 Works");////////////////////////////////////
			String urlStr = params[0];
			InputStream is = null;
			ArrayList<PostData> postDataList = new ArrayList<PostData>();

			URL url;
			try {
				url = new URL(urlStr);
				L.myLog("Line 226 Works");////////////////////////////////////
				
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setReadTimeout(10 * 1000);
				connection.setConnectTimeout(10 * 1000);
				connection.setRequestMethod("GET");
				connection.setDoInput(true);
				connection.connect();
				int response = connection.getResponseCode();
				Log.d("debug", "The response is: " + response);
				is = connection.getInputStream();

				// parse xml
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(is, null);

				int eventType = xpp.getEventType();
				PostData pdData = null;
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"EEE, DD MMM yyyy HH:mm:ss", Locale.US);
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {

						L.myLog("Line 252 Works");////////////////////////////////////
					} else if (eventType == XmlPullParser.START_TAG) {
						if (xpp.getName().equals("item")) {
							pdData = new PostData();
							currentTag = RSSXMLTag.IGNORETAG;
						} else if (xpp.getName().equals("title")) {
							currentTag = RSSXMLTag.TITLE;
						} else if (xpp.getName().equals("link")) {
							currentTag = RSSXMLTag.LINK;
						} else if (xpp.getName().equals("pubDate")) {
							currentTag = RSSXMLTag.DATE;
						} else if (xpp.getName().equals("encoded")) {
							currentTag = RSSXMLTag.CONTENT;
						} else if (xpp.getName().equals("guid")) {
							currentTag = RSSXMLTag.GUID;
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						if (xpp.getName().equals("item")) {
							// format the data here, otherwise format data in
							// Adapter
							Date postDate = dateFormat.parse(pdData.postDate);
							pdData.postDate = dateFormat.format(postDate);
							postDataList.add(pdData);
							L.myLog("Line 275 Works");////////////////////////////////////
						} else {
							currentTag = RSSXMLTag.IGNORETAG;
						}
					} else if (eventType == XmlPullParser.TEXT) {
						String content = xpp.getText();
						content = content.trim();
						L.myLog("Line 282 Works");////////////////////////////////////
						if (pdData != null) {
							switch (currentTag) {
							case TITLE:
								if (content.length() != 0) {
									if (pdData.postTitle != null) {
										pdData.postTitle += content;
										L.myLog("Line 289 Works");////////////////////////////////////
									} else {
										pdData.postTitle = content;
									}
								}
								break;
							case LINK:
								if (content.length() != 0) {
									if (pdData.postLink != null) {
										pdData.postLink += content;
										L.myLog("Line 299 Works");////////////////////////////////////
									} else {
										pdData.postLink = content;
									}
								}
								break;
							case DATE:
								if (content.length() != 0) {
									if (pdData.postDate != null) {
										pdData.postDate += content;
										L.myLog("Line 309 Works");////////////////////////////////////
									} else {
										pdData.postDate = content;
									}
								}
								break;
							case CONTENT:
								if (content.length() != 0) {
									if (pdData.postContent != null) {
										pdData.postContent += content;
										L.myLog("Line 319 Works");////////////////////////////////////
									} else {
										pdData.postContent = content;
									}
								}
								break;
							case GUID:
								if (content.length() != 0) {
									if (pdData.postGuid != null) {
										pdData.postGuid += content;
										L.myLog("Line 329 Works");////////////////////////////////////
									} else {
										pdData.postGuid = content;
									}
								}
								break;
							default:
								break;
							}
						}
					}

					eventType = xpp.next();
				}
				Log.v("tst", String.valueOf(postDataList.size()));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				// new URL exception
				googleTracker.sendEvent("debug", "MalformedURLException",
						e.toString(), null);
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				// setRequestMethod exception
				googleTracker.sendEvent("debug", "ProtocolException",
						e.toString(), null);
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				// XmlPullParserFactory.newInstance()
				googleTracker.sendEvent("debug", "XmlPullParserException",
						e.toString(), null);
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				// dateFormat.parse(pdData.postDate);
				googleTracker.sendEvent("debug", "ParseException",
						e.toString(), null);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// openConnection()
				// connection.getResponseCode()
				// connection.connect();
				// connection.getInputStream()
				// xpp.next()
				googleTracker.sendEvent("debug", "IOException", e.toString(),
						null);
				e.printStackTrace();
			}
			L.myLog("Line 379 Works");////////////////////////////////////
			return postDataList;
		}

		@Override
		protected void onPostExecute(ArrayList<PostData> result) {
			// TODO Auto-generated method stub
			boolean isupdated = false;
			for (int i = 0; i < result.size(); i++) {
				// check if the post is already in the list
				if (guidList.contains(result.get(i).postGuid)) {
					continue;
				} else {
					isupdated = true;
					guidList.add(result.get(i).postGuid);
				}

				if (isRefreshLoading) {
					listData.add(i, result.get(i));
				} else {
					listData.add(result.get(i));
				}
			}
			L.myLog("Line 402 Works");////////////////////////////////////

			if (isupdated) {
				postAdapter.notifyDataSetChanged();
				L.myLog("Line 406 Works");////////////////////////////////////
			}

			isLoading = false;

			if (isRefreshLoading) {
				postListView.onRefreshComplete();
				L.myLog("Line 413 Works");////////////////////////////////////
			} else {
				postListView.onLoadingMoreComplete();
				L.myLog("Line 416 Works");////////////////////////////////////
			}

			super.onPostExecute(result);
		}
	}

	public void startFresh() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			isRefreshLoading = true;
			isLoading = true;
			L.myLog("Line 428 Works");////////////////////////////////////
			L.myLog("Line 429 Works");////////////////////////////////////
			/*
			 * Pagination: 
			 * 
			 * If your rss feed looks like: 
			 * 
			 * "http://jmsliu.com/feed?paged="
			 * 
			 * You can try follow code for pagination. 
			 * 
			 * new RssDataController().execute(urlString + 1);
			 */
			
			new RssDataController().execute(urlString);
		} else {
			postListView.onRefreshComplete();
			L.myLog("Line 445 Works");////////////////////////////////////
		}
	}

	public void startLoadMore() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			isRefreshLoading = false;
			isLoading = true;
			
			/*
			 * Pagination: 
			 * 
			 * If your rss feed source looks like "http://jmsliu.com/feed?paged=", 
			 * you can try follow code for pagination:
			 * 
			 * new RssDataController().execute(urlString + (++pagnation));
			 * 
			 * Otherwise, please use this:
			 * 
			 * new RssDataController().execute(urlString);
			 */
			new RssDataController().execute(urlString); 
					//+ (++pagnation)); LEAVING THIS OFF FOR NOW, will be useful for multiple pages 
			
			L.myLog("Line 470 Works");
		} else {
			postListView.onLoadingMoreComplete();
		}
	}

	//public static void setContext(Context context) {
		//mContext = context;
	//}
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////
	
	
		//Simple class that makes a popup (toast) with text
		public void makeToast(String activityChosen){
			Toast.makeText(this.getActivity(), activityChosen, Toast.LENGTH_SHORT).show();
		}
		
		public void setUpdatedTime(){
			Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();
			
			int month = today.month + 1;
			earnings_updated_as_of.setText("Updated as of " + today.year + "-" + month + "-" + today.monthDay + " : " + today.format("%k:%M"));
		}
		
		
		
	   
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Called when the Fragment is attached to its parent Activity.
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Get a reference to the parent Activity.
	}

	// Called to do the initial creation of the Fragment.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
	}

	// Called once the parent Activity and the Fragment's UI have
	// been created.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Complete the Fragment initialization – particularly anything
		// that requires the parent Activity to be initialized or the
		// Fragment's view to be fully inflated.
		
		
	}

	// Called at the start of the visible lifetime.
	@Override
	public void onStart() {
		super.onStart();
		// Apply any required UI change now that the Fragment is visible.
		
	}

	// Called at the start of the active lifetime.
	@Override
	public void onResume() {
		super.onResume();
		// Resume any paused UI updates, threads, or processes required
		// by the Fragment but suspended when it became inactive.
	}

	// Called at the end of the active lifetime.
	@Override
	public void onPause() {
		// Suspend UI updates, threads, or CPU intensive processes
		// that don't need to be updated when the Activity isn't
		// the active foreground activity.
		// Persist all edits or state changes
		// as after this call the process is likely to be killed.
		super.onPause();
	}

	// Called to save UI state changes at the
	// end of the active lifecycle.
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate, onCreateView, and
		// onCreateView if the parent Activity is killed and restarted.
		super.onSaveInstanceState(savedInstanceState);
	}

	// Called at the end of the visible lifetime.
	@Override
	public void onStop() {
		// Suspend remaining UI updates, threads, or processing
		// that aren't required when the Fragment isn't visible.
		super.onStop();
	}

	// Called when the Fragment's View has been detached.
	@Override
	public void onDestroyView() {
		// Clean up resources related to the View.
		super.onDestroyView();
	}



	// Called when the Fragment has been detached from its parent Activity.
	@Override
	public void onDetach() {
		super.onDetach();
	}
}