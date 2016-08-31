package com.froist_inc.josh.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        getActivity().setTitle( R.string.crime_title );
        mCrimes = CrimeLab.get( getActivity() ).getCrimes();

        CrimeAdapter adapter = new CrimeAdapter( mCrimes );
        setListAdapter( adapter );
    }

    @Override
    public void onListItemClick( ListView l, View v, int position, long id )
    {
        Intent intent = new Intent( getActivity(), ViewPagerActivity.class );
        intent.putExtra( CrimeFragment.EXTRA_ID, mCrimes.get( position ).getId() );
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ( ( CrimeAdapter )getListAdapter() ).notifyDataSetChanged();
    }

    private class CrimeAdapter extends ArrayAdapter<Crime>
    {
        public CrimeAdapter( ArrayList<Crime> crimes )
        {
            super( getActivity(), 0, crimes );
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if( convertView == null )
            {
                convertView = getActivity().getLayoutInflater()
                        .inflate( R.layout.list_item_crimes, null );
            }
            Crime crime = getItem( position );
            TextView title = ( TextView ) convertView.findViewById(
                    R.id.crime_list_item_titleTextView );
            title.setText( crime.getTitle() );
            TextView dateDetail = ( TextView ) convertView.findViewById(
                    R.id.crime_list_item_dateTextView );
            dateDetail.setText( crime.getDate().toString() );
            CheckBox crimeSolved = ( CheckBox ) convertView.findViewById(
                    R.id.crime_list_item_solvedCheckBox );
            crimeSolved.setChecked( crime.isSolved() );

            return convertView;
        }
    }
}
