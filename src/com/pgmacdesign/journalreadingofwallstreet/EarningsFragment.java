package com.pgmacdesign.journalreadingofwallstreet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class EarningsFragment extends Fragment {
    
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//View rootView = inflater.inflate(R.layout.fragment_earnings, container, false);
		//View rootView = inflater.inflate(R.layout.fragment_earnings, container, false);
		
		return inflater.inflate(R.layout.fragment_earnings, container, false);
		
		/*
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.layout.fragment_earnings);
		
		
		View view = inflater.inflate(R.layout.fragment_earnings, null);
		mainlayout.addView(view);
		
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_earnings, container, false);
		*/
		
		
	}
}