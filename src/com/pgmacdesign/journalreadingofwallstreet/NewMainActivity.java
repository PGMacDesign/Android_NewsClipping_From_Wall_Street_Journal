package com.pgmacdesign.journalreadingofwallstreet;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



public class NewMainActivity extends Activity {
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	
	    // ActionBar
	    ActionBar actionbar = getActionBar();
	    actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
	    // create new tabs and set up the titles of the tabs
	    ActionBar.Tab mAutoTab = actionbar.newTab().setText("Autos");
	    ActionBar.Tab mEarningsTab = actionbar.newTab().setText("Earnings");
	    ActionBar.Tab mEconomyTab = actionbar.newTab().setText("Economy");
	    ActionBar.Tab mEnergyTab = actionbar.newTab().setText("Energy");
	    ActionBar.Tab mHealthTab = actionbar.newTab().setText("Health");
	    ActionBar.Tab mLawTab = actionbar.newTab().setText("Law");
	    ActionBar.Tab mManagementTab = actionbar.newTab().setText("Management");
	    ActionBar.Tab mSmallBusinessTab = actionbar.newTab().setText("Small Business");
	    ActionBar.Tab mStartupsTab = actionbar.newTab().setText("Startups");
	    
	    
	    //ActionBar.Tab mPartyTab = actionbar.newTab().setText(getString(R.string.ui_tabname_party)); //Sample

	} 
}