package com.froist_inc.josh.criminalintent;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    private static String TAG = "CrimeListFragment";

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
        super.onListItemClick( l, v, position, id );
        Crime crime = ( ( CrimeAdapter ) getListAdapter() ).getItem( position );
        Log.d( TAG, crime.getTitle() + " was clicked" );
        Toast.makeText( getContext(), crime.getTitle() + " was clicked",
                Toast.LENGTH_SHORT ).show();
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
