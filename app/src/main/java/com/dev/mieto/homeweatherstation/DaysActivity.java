package com.dev.mieto.homeweatherstation;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class DaysActivity extends AppCompatActivity {

    /*Tag do debugu*/
    public static final String TAG = DaysActivity.class.getSimpleName();
    public static final String ENDPOINT = "http://192.168.0.87:5000/api/v1/";

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mViewPager != null){
            setupViewPager(mViewPager);
        }
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        if (mTabLayout != null){
            mTabLayout.setupWithViewPager(mViewPager);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TempDaysFragment(), "TEMP");
        adapter.addFragment(new LightDaysFragment(), "LIGHT");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmLst = new ArrayList<>();
        private final List<String> mFragmTitleLst = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmLst.get(position);
        }

        @Override
        public int getCount() {
            return mFragmLst.size();
        }

        public void addFragment(Fragment fragment, String title){
            mFragmLst.add(fragment);
            mFragmTitleLst.add(title);
        }

        public CharSequence getPageTitle(int position){
            return mFragmTitleLst.get(position);
        }
    }
}
