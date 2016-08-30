package com.froist_inc.josh.criminalintent;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment()
    {
        UUID id = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_ID);
        return CrimeFragment.newInstance(id);
    }
}