package com.froist_inc.josh.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

public class ViewPagerActivity extends FragmentActivity
{
    private ArrayList<Crime> mCrimes;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        ViewPager viewPager = new ViewPager( this );
        viewPager.setId( R.id.viewPage );
        setContentView( viewPager );
        mCrimes = CrimeLab.get( this ).getCrimes();

        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter( new FragmentStatePagerAdapter( fm ) {
            @Override
            public Fragment getItem( int position ) {
                return CrimeFragment.newInstance( mCrimes.get( position ).getId() );
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        viewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle( mCrimes.get( position ).getTitle() );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        UUID uid = ( UUID ) getIntent().getSerializableExtra( CrimeFragment.EXTRA_ID );
        for ( int i = 0; i < mCrimes.size(); i++ ) {
            if( mCrimes.get( i ).getId().equals( uid ) ){
                viewPager.setCurrentItem( i );
                break;
            }
        }
    }
}
