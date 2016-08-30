package com.froist_inc.josh.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    public static String EXTRA_ID =
            "com.froist_inc.josh.criminalintent.crime_list_fragment.EXTRA_ID";

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        UUID position = (UUID) getArguments().getSerializable(EXTRA_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(position);
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState )
    {
        final View view = inflater.inflate( R.layout.fragment_crime, container, false );
        EditText titleView = (EditText) view.findViewById(R.id.crime_title);
        titleView.setText(mCrime.getTitle());
        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after )
            {

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count )
            {
                mCrime.setTitle( s.toString() );
            }

            @Override
            public void afterTextChanged( Editable s )
            {

            }
        });

        Button dateButton = ( Button ) view.findViewById( R.id.crime_date );
        dateButton.setText( mCrime.getDate().toString() );
        dateButton.setEnabled( false );

        CheckBox solvedCheckBox = ( CheckBox ) view.findViewById( R.id.crime_solved );
        solvedCheckBox.setChecked(mCrime.isSolved());
        solvedCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
                mCrime.setSolved( isChecked );
            }
        });
        return view;
    }

    public static CrimeFragment newInstance(UUID id) {
        Bundle extra = new Bundle();
        extra.putSerializable(EXTRA_ID, id);

        CrimeFragment newCrimeFragment = new CrimeFragment();
        newCrimeFragment.setArguments(extra);
        return newCrimeFragment;
    }
}
