package com.globallogic.futbol.example.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.globallogic.futbol.example.R;
import com.globallogic.futbol.example.fragments.CreateDeviceFragment;
import com.globallogic.futbol.example.fragments.DeleteDeviceFragment;
import com.globallogic.futbol.example.fragments.DeletePageFragment;
import com.globallogic.futbol.example.fragments.GetDeviceFragment;
import com.globallogic.futbol.example.fragments.GetDevicesFragment;
import com.globallogic.futbol.example.fragments.GetExampleTimeOutFragment;
import com.globallogic.futbol.example.fragments.GetFileFragment;
import com.globallogic.futbol.example.fragments.GetPageFragment;
import com.globallogic.futbol.example.fragments.PostPageFragment;
import com.globallogic.futbol.example.fragments.PutExampleUpdateDeviceFragment;
import com.globallogic.futbol.example.fragments.PutPageFragment;

import java.util.Locale;

public class DashboardActivity extends Activity implements GetPageFragment.ICallback, PostPageFragment.ICallback, DeletePageFragment.ICallback, PutPageFragment.ICallback {
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (findViewById(R.id.activity_dashboard_detail) != null)
            isTwoPane = true;

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        ViewPager vViewPager = (ViewPager) findViewById(R.id.activity_dashboard_viewpager);
        vViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void addFragment(Fragment fragment, String tag) {
        getFragmentManager().beginTransaction().replace(R.id.activity_dashboard_detail, fragment, tag).addToBackStack(null).commit();
    }

    private void startActivity(String tag) {
        startActivity(GenericExampleActivity.generateIntent(this, tag));
    }

    @Override
    public void onExampleGetFile() {
        if (isTwoPane)
            addFragment(GetFileFragment.newInstance(), GetFileFragment.TAG);
        else
            startActivity(GetFileFragment.TAG);
    }

    @Override
    public void onExampleGetListSuccess() {
        if (isTwoPane)
            addFragment(GetDevicesFragment.newInstance(), GetDevicesFragment.TAG);
        else
            startActivity(GetDevicesFragment.TAG);
    }
    @Override
    public void onExampleGetSuccess() {
        if (isTwoPane)
            addFragment(GetDeviceFragment.newInstance(), GetDeviceFragment.TAG);
        else
            startActivity(GetDeviceFragment.TAG);
    }
    @Override
    public void onExampleGetTimeout() {
        if (isTwoPane)
            addFragment(GetExampleTimeOutFragment.newInstance(), GetExampleTimeOutFragment.TAG);
        else
            startActivity(GetExampleTimeOutFragment.TAG);
    }
    @Override
    public void onExamplePostSingleString() {
        if (isTwoPane)
            addFragment(CreateDeviceFragment.newInstance(), CreateDeviceFragment.TAG);
        else
            startActivity(CreateDeviceFragment.TAG);
    }
    @Override
    public void onExampleDeleteItem() {
        if (isTwoPane)
            addFragment(DeleteDeviceFragment.newInstance(), DeleteDeviceFragment.TAG);
        else
            startActivity(DeleteDeviceFragment.TAG);
    }
    @Override
    public void onExamplePutSingleString() {
        if (isTwoPane)
            addFragment(PutExampleUpdateDeviceFragment.newInstance(), PutExampleUpdateDeviceFragment.TAG);
        else
            startActivity(PutExampleUpdateDeviceFragment.TAG);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                default:
                case 0:
                    return GetPageFragment.newInstance();
                case 1:
                    return PostPageFragment.newInstance();
                case 2:
                    return PutPageFragment.newInstance();
                case 3:
                    return DeletePageFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section_get).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section_post).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section_put).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section_delete).toUpperCase(l);
            }
            return "";
        }
    }
}