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
        mCrimes = new ArrayList<Crime>();
        mAppContext = context;
        for ( int i = 0; i < 100; i++ ) {
            Crime crime = new Crime();
            crime.setTitle( "Crime #" + i );
            crime.setSolved( i % 2 == 0 );
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
