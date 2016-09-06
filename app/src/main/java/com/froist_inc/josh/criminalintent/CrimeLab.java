package com.froist_inc.josh.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private ArrayList<Crime> mCrimes;
    private Context mAppContext;

    private CrimeLab( Context context )
    {
        mCrimes = new ArrayList<>();
        mAppContext = context;

        for ( int x = 0; x < 15; x++ ) {
            Crime crime = new Crime();
            crime.setSolved( x % 2 == 0 );
            crime.setTitle( "Crime #" + x );
            mCrimes.add( crime );
        }
    }

    public static CrimeLab get( Context context )
    {
        if( sCrimeLab == null )
        {
            sCrimeLab = new CrimeLab( context.getApplicationContext() );
        }
        return sCrimeLab;
    }

    public void addCrime( Crime crime )
    {
        mCrimes.add( crime );
    }
    public void removeCrime( Crime crime )
    {
        mCrimes.remove( crime );
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }
    public Crime getCrime( UUID id )
    {
        for ( Crime crime: mCrimes ) {
            if( crime.getId().equals( id ) ) return crime;
        }
        return null;
    }
}
