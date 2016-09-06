package com.froist_inc.josh.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    public static final String EXTRA_ID =
            "com.froist_inc.josh.criminalintent.crime_list_fragment.EXTRA_ID";
    public static final int REQUEST_CODE = 0;
    public static final int PICTURE_REQUEST = 1;
    private Button mDateButton;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        UUID position = ( UUID ) getArguments().getSerializable( EXTRA_ID );
        mCrime = CrimeLab.get( getActivity() ).getCrime( position );
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch ( item.getItemId() )
        {
            case android.R.id.home:
                if( NavUtils.getParentActivityName( getActivity() ) != null ){
                    NavUtils.navigateUpFromSameTask( getActivity() );
                }
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }
    
    @TargetApi( 11 )
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState )
    {
        final View view = inflater.inflate( R.layout.fragment_crime, container, false );
        setHasOptionsMenu( true );

        /*
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
            if ( NavUtils.getParentActivityName( getActivity() ) != null ) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled( true );
            }
        }
        */
        EditText editText = ( EditText ) view.findViewById( R.id.crime_text_edit );
        editText.setText( mCrime.getTitle() );
        editText.addTextChangedListener( new TextWatcher() {
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

        mDateButton = ( Button ) view.findViewById( R.id.crime_date );
        mDateButton.setText( mCrime.getDate().toString() );
        mDateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v )
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DialogFragment dialog = DatePickerDialog.newInstance( mCrime.getDate() );
                dialog.setTargetFragment( CrimeFragment.this, CrimeFragment.REQUEST_CODE );
                dialog.show( fragmentManager, "dialog_date" );
            }
        });

        CheckBox solvedCheckBox = ( CheckBox ) view.findViewById( R.id.crime_solved );
        solvedCheckBox.setChecked( mCrime.isSolved() );
        solvedCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
                mCrime.setSolved( isChecked );
            }
        });
        ImageButton openCameraButton = ( ImageButton ) view.findViewById( R.id.crime_camera_imageButton );
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v )
            {
                Intent newIntent = new Intent( getActivity(), CrimeCameraActivity.class );
                startActivityForResult( newIntent, PICTURE_REQUEST );
            }
        });
        PackageManager packageManager = getActivity().getPackageManager();
        boolean hasCamera = packageManager.hasSystemFeature( PackageManager.FEATURE_CAMERA )
                || packageManager.hasSystemFeature( PackageManager.FEATURE_CAMERA_FRONT );
        openCameraButton.setEnabled( hasCamera );
        return view;
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if( resultCode != Activity.RESULT_OK ) return;

        if( requestCode == REQUEST_CODE ) {
            Date date = ( Date ) data.getSerializableExtra( DatePickerDialog.EXTRA_DATE );
            mCrime.setDate( date );
            mDateButton.setText( mCrime.getDate().toString() );
        } else if( requestCode == PICTURE_REQUEST ) {
            String filename = data.getStringExtra( CameraViewFragment.PICTURE_FILENAME );
            if( filename != null ) {
                Toast.makeText( getActivity(), filename, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static CrimeFragment newInstance( UUID id )
    {
        Bundle extra = new Bundle();
        extra.putSerializable( EXTRA_ID, id );

        CrimeFragment newCrimeFragment = new CrimeFragment();
        newCrimeFragment.setArguments( extra );
        return newCrimeFragment;
    }
}
