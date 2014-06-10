package com.pgmacdesign.journalreadingofwallstreet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EconomyFragment extends Fragment {
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_economy, container, false);
		
		return rootView;
		
		/*
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_economy, container, false);
		*/
		
	}
}