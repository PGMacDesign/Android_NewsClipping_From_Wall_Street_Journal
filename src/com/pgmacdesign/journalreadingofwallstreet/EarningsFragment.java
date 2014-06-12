package com.pgmacdesign.journalreadingofwallstreet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EarningsFragment extends Fragment {
	
	ListView earnings_list;
	TextView earnings_updated_as_of;
	static TextView earnings_temp_text_view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_earnings, container, false);
		// View rootView = inflater.inflate(R.layout.fragment_earnings,
		// container, false);

		earnings_updated_as_of = (TextView) rootView.findViewById(R.id.earnings_updated_as_of);
		earnings_temp_text_view = (TextView) rootView.findViewById(R.id.earnings_temp_text_view);
		earnings_list = (ListView) rootView.findViewById(R.id.earnings_list);
		
		setUpdatedTime();
		
		PlaceholderFragment pl = new PlaceholderFragment();
		
		pl.onStart();
		
		
		
		
		
		return rootView;

		/*
		 * LinearLayout layout = (LinearLayout)
		 * rootView.findViewById(R.layout.fragment_earnings);
		 * 
		 * 
		 * View view = inflater.inflate(R.layout.fragment_earnings, null);
		 * mainlayout.addView(view);
		 * 
		 * // Inflate the layout for this fragment return
		 * inflater.inflate(R.layout.fragment_earnings, container, false);
		 */

	}

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
		
		
		
	    //A placeholder fragment containing a simple view.
	    
		
	    public static class PlaceholderFragment extends ListFragment {

	        private TextView mRssFeed;

	        public PlaceholderFragment() {
	        }

	        @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                                 Bundle savedInstanceState) {
	            View rootView1 = inflater.inflate(R.layout.fragment_earnings, container, false);
	            mRssFeed = (TextView) rootView1.findViewById(R.id.earnings_temp_text_view);
	            return rootView1;
	        }

	        public void onStart() {
	            super.onStart();
	            
	            Toast.makeText(getActivity(), "TEST 1", Toast.LENGTH_SHORT).show();
	            
	            
	            new GetAndroidPitRssFeedTask().execute();
	        }

	        private String getAndroidPitRssFeed() throws IOException {
	            InputStream in = null;
	            String rssFeed = null;
	            try {
	                URL url = new URL("http://online.wsj.com/public/page/0_0813.html?mod=fpp_rss");
	                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	                in = conn.getInputStream();
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                byte[] buffer = new byte[1024];
	                for (int count; (count = in.read(buffer)) != -1; ) {
	                    out.write(buffer, 0, count);
	                }
	                byte[] response = out.toByteArray();
	                rssFeed = new String(response, "UTF-8");
	            } catch (Exception e){
	            	Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
	            } finally {
	                if (in != null) {
	                    in.close();
	                }
	            }
	            return rssFeed;
	        }

	        private class GetAndroidPitRssFeedTask extends AsyncTask<Void, Void, List<String>> {

	            @Override
	            protected List<String> doInBackground(Void... voids) {
	                List<String> result = null;
	                try {
	                    String feed = getAndroidPitRssFeed();
	                    result = parse(feed);
	                } catch (XmlPullParserException e) {
	                    e.printStackTrace();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	                
	                //String[] strarray = result.toArray(new String[0]);
	                
	                //earnings_temp_text_view.setText(strarray[0]);
	                
	                return result;
	            }

	            private List<String> parse(String rssFeed) throws XmlPullParserException, IOException {
	                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	                XmlPullParser xpp = factory.newPullParser();
	                xpp.setInput(new StringReader(rssFeed));
	                xpp.nextTag();
	                return readRss(xpp);
	            }

	            private List<String> readRss(XmlPullParser parser)
	                    throws XmlPullParserException, IOException {
	                List<String> items = new ArrayList<>();
	                parser.require(XmlPullParser.START_TAG, null, "rss");
	                while (parser.next() != XmlPullParser.END_TAG) {
	                    if (parser.getEventType() != XmlPullParser.START_TAG) {
	                        continue;
	                    }
	                    String name = parser.getName();
	                    if (name.equals("channel")) {
	                        items.addAll(readChannel(parser));
	                    } else {
	                        skip(parser);
	                    }
	                }
	                return items;
	            }

	            private List<String> readChannel(XmlPullParser parser)
	                    throws IOException, XmlPullParserException {
	                List<String> items = new ArrayList<>();
	                parser.require(XmlPullParser.START_TAG, null, "channel");
	                while (parser.next() != XmlPullParser.END_TAG) {
	                    if (parser.getEventType() != XmlPullParser.START_TAG) {
	                        continue;
	                    }
	                    String name = parser.getName();
	                    if (name.equals("item")) {
	                        items.add(readItem(parser));
	                    } else {
	                        skip(parser);
	                    }
	                }
	                return items;
	            }

	            private String readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
	                String result = null;
	                parser.require(XmlPullParser.START_TAG, null, "item");
	                while (parser.next() != XmlPullParser.END_TAG) {
	                    if (parser.getEventType() != XmlPullParser.START_TAG) {
	                        continue;
	                    }
	                    String name = parser.getName();
	                    if (name.equals("title")) {
	                        result = readTitle(parser);
	                    } else {
	                        skip(parser);
	                    }
	                }
	                return result;
	            }

	            // Processes title tags in the feed.
	            private String readTitle(XmlPullParser parser)
	                    throws IOException, XmlPullParserException {
	                parser.require(XmlPullParser.START_TAG, null, "title");
	                String title = readText(parser);
	                parser.require(XmlPullParser.END_TAG, null, "title");
	                return title;
	            }

	            private String readText(XmlPullParser parser)
	                    throws IOException, XmlPullParserException {
	                String result = "";
	                if (parser.next() == XmlPullParser.TEXT) {
	                    result = parser.getText();
	                    parser.nextTag();
	                }
	                return result;
	            }

	            private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	                if (parser.getEventType() != XmlPullParser.START_TAG) {
	                    throw new IllegalStateException();
	                }
	                int depth = 1;
	                while (depth != 0) {
	                    switch (parser.next()) {
	                        case XmlPullParser.END_TAG:
	                            depth--;
	                            break;
	                        case XmlPullParser.START_TAG:
	                            depth++;
	                            break;
	                    }
	                }
	            }

	            @Override
	            protected void onPostExecute(List<String> rssFeed) {
	                if (rssFeed != null) {
	                    setListAdapter(new ArrayAdapter<>(
	                            getActivity(),
	                            android.R.layout.simple_list_item_1,
	                            android.R.id.text1,
	                            rssFeed));
	                }
	            }
	        }
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
		
		Toast.makeText(getActivity(), "TEST 2", Toast.LENGTH_SHORT).show();
		
	}

	// Called once the parent Activity and the Fragment's UI have
	// been created.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Complete the Fragment initialization – particularly anything
		// that requires the parent Activity to be initialized or the
		// Fragment's view to be fully inflated.
		
		Toast.makeText(getActivity(), "TEST 3", Toast.LENGTH_SHORT).show();
	}

	// Called at the start of the visible lifetime.
	@Override
	public void onStart() {
		super.onStart();
		// Apply any required UI change now that the Fragment is visible.
		Toast.makeText(getActivity(), "TEST 4", Toast.LENGTH_SHORT).show();
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

	// Called at the end of the full lifetime.
	@Override
	public void onDestroy() {
		// Clean up any resources including ending threads,
		// closing database connections etc.
		super.onDestroy();
	}

	// Called when the Fragment has been detached from its parent Activity.
	@Override
	public void onDetach() {
		super.onDetach();
	}
}