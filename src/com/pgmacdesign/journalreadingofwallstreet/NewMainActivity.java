package com.pgmacdesign.journalreadingofwallstreet;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;



public class NewMainActivity extends Activity {
	 
	private static final String TAB_KEY_INDEX = "tab_key";
	
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

	    
        // create the fragments
        Fragment mAutosFragment = new AutosFragment();
        Fragment mEarningsFragment = new EarningsFragment();
        Fragment mEconomyFragment = new EconomyFragment();
        Fragment mEnergyFragment = new EnergyFragment();
        Fragment mHealthFragment = new HealthFragment();
        Fragment mLawFragment = new LawFragment();
        Fragment mManagementFragment = new ManagementFragment();
        Fragment mSmallBusinessFragment = new SmallBusinessFragment();
        Fragment mStartupsFragment = new StartupsFragment();
        
        // bind the fragments to the tabs - set up tabListeners for each tab
        mAutoTab.setTabListener(new MyTabsListener(mAutosFragment, getApplicationContext()));
        mEarningsTab.setTabListener(new MyTabsListener(mEarningsFragment, getApplicationContext()));
        mEconomyTab.setTabListener(new MyTabsListener(mEconomyFragment, getApplicationContext()));
        mEnergyTab.setTabListener(new MyTabsListener(mEnergyFragment, getApplicationContext()));
        mHealthTab.setTabListener(new MyTabsListener(mHealthFragment, getApplicationContext()));
        mLawTab.setTabListener(new MyTabsListener(mLawFragment, getApplicationContext()));
        mManagementTab.setTabListener(new MyTabsListener(mManagementFragment, getApplicationContext()));
        mSmallBusinessTab.setTabListener(new MyTabsListener(mSmallBusinessFragment, getApplicationContext()));
        mStartupsTab.setTabListener(new MyTabsListener(mStartupsFragment, getApplicationContext()));
        
        // add the tabs to the action bar
        actionbar.addTab(mAutoTab);
        actionbar.addTab(mEarningsTab);
        actionbar.addTab(mEconomyTab);
        actionbar.addTab(mEnergyTab);
        actionbar.addTab(mHealthTab);
        actionbar.addTab(mLawTab);
        actionbar.addTab(mManagementTab);
        actionbar.addTab(mSmallBusinessTab);
        actionbar.addTab(mStartupsTab);
        
        if (savedInstanceState != null) {
            Toast.makeText(getApplicationContext(),
                    "tab is " + savedInstanceState.getInt(TAB_KEY_INDEX, 0),
                    Toast.LENGTH_SHORT).show();
 
            actionbar.setSelectedNavigationItem(savedInstanceState.getInt(
                    TAB_KEY_INDEX, 0));
        }
	} 
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.tabs_menu, menu);
                return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.menuitem_search:
                    Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
                    return true;
                    
                case R.id.menuitem_send:
                    Toast.makeText(this, "Send", Toast.LENGTH_LONG).show();
                    return true;
                    
                case R.id.menuitem_add:
                    Toast.makeText(this, "Add", Toast.LENGTH_LONG).show();
                    return true;
		                    
		        case R.id.menuitem_share:
                    Toast.makeText(this, "Share", Toast.LENGTH_LONG).show();
                    return true;
		                    
		        case R.id.menuitem_feedback:
		            Toast.makeText(this, "Feedback", Toast.LENGTH_LONG).show();
		            return true;
		            
				case R.id.menuitem_about:
		            Toast.makeText(this, "About", Toast.LENGTH_LONG).show();
		            return true;
				    
				case R.id.menuitem_quit:
				    Toast.makeText(this, "Quit", Toast.LENGTH_LONG).show();
				    return true;
				    
				    
                /*
                 * case R.id.menuitem_quit:
                 
                            Toast.makeText(this, getString(R.string.ui_menu_quit),
                                                    Toast.LENGTH_SHORT).show();
                            finish(); // close the activity
                            return true;
                     */
                }
                return false;
    }
    
 	//onSaveInstanceState() is used to "remember" the current state when a
    // configuration change occurs such screen orientation change. This
    // is not meant for "long term persistence". We store the tab navigation
 
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(
                this,
                "onSaveInstanceState: tab is"
                        + getActionBar().getSelectedNavigationIndex(),
                Toast.LENGTH_SHORT).show();
        outState.putInt(TAB_KEY_INDEX, getActionBar()
                .getSelectedNavigationIndex());
 
    }
    
    
  //TabListenr class for managing user interaction with the ActionBar tabs. The
  //application context is passed in pass it in constructor, needed for the
  //toast.

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;
		public Context context;

		public MyTabsListener(Fragment fragment, Context context) {
			this.fragment = fragment;
			this.context = context;

		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(context, "Reselected!", Toast.LENGTH_SHORT).show();

		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(context, "Selected!", Toast.LENGTH_SHORT).show();
			ft.replace(R.id.fragment_container, fragment);
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(context, "Unselected!", Toast.LENGTH_SHORT).show();
			ft.remove(fragment);
		}

		@Override
		public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

	}
}



