package com.froist_inc.josh.criminalintent;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        startActivity( intent );
    }

    @Override
    public void onCreateContextMenu( ContextMenu menu, View v,
                                     ContextMenu.ContextMenuInfo menuInfo )
    {
        getActivity().getMenuInflater().inflate( R.menu.menu_list_context, menu );
    }

    @Override
    public boolean onContextItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case R.id.menu_context_delete_item:
                Crime crime = ( Crime ) getListAdapter().getItem(
                        getListView().getSelectedItemPosition() );
                CrimeLab.get( getActivity() ).removeCrime( crime );
                (( CrimeAdapter ) getListAdapter() ).notifyDataSetChanged();
                return true;
            case R.id.menu_context_just_testing:
                Toast.makeText( getActivity(), R.string.add_crime, Toast.LENGTH_SHORT ).show();
                return true;
            default:
                return super.onContextItemSelected( item );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = super.onCreateView( inflater, container, savedInstanceState );
        ListView listView = ( ListView ) v.findViewById( android.R.id.list );

        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
            registerForContextMenu( listView );
        } else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_list_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked( ActionMode mode, MenuItem item )
                {
                    switch ( item.getItemId() ) {
                        case R.id.menu_context_delete_item:
                            CrimeLab crimeLab = CrimeLab.get( getActivity() );
                            CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
                            for (int i = adapter.getCount() - 1; i >= 0; --i) {
                                if (getListView().isItemChecked(i)) {
                                    crimeLab.removeCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        case R.id.menu_context_just_testing:
                            Toast.makeText( getActivity(), R.string.add_crime,
                                    Toast.LENGTH_SHORT ).show();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        return v;
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
        public View getView( int position, View convertView, ViewGroup parent )
        {
            if( convertView == null )
            {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.list_item_crimes, null );
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
