package com.pgmacdesign.journalreadingofwallstreet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			//Notes fragment activity
			return new AutosFragment();
		case 1:
			//Events fragment activity
			return new EarningsFragment();
		case 2:
			//Tasks fragment activity
			return new EconomyFragment();
		case 3:
			//Notes fragment activity
			return new USBusiness();
		case 4:
			//Events fragment activity
			return new HealthFragment();
		case 5:
			//Tasks fragment activity
			return new LawFragment();
		case 6:
			//Notes fragment activity
			return new ManagementFragment();
		case 7:
			//Events fragment activity
			return new MediaAndMarketing();
		case 8:
			//Tasks fragment activity
			return new PoliticsAndCampaign();			
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 9;
	}

}
