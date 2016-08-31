package com.froist_inc.josh.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerDialog extends android.support.v4.app.DialogFragment
{
    private Date mDate;
    public static final String EXTRA_DATE =
            "com.froist_inc.josh.criminalintent.DatePickerDialog.Date";
    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        mDate = ( Date ) getArguments().getSerializable( EXTRA_DATE );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( mDate );
        final int year = calendar.get( Calendar.YEAR ),
                month = calendar.get( Calendar.MONTH ),
                day = calendar.get( Calendar.DAY_OF_MONTH );
        DatePicker datePicker = new DatePicker( getActivity() );
        datePicker.init( year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged( DatePicker view, int year, int monthOfYear,
                                       int dayOfMonth )
            {
                mDate = new GregorianCalendar( year, monthOfYear, dayOfMonth ).getTime();
                getArguments().putSerializable( EXTRA_DATE, mDate );
            }
        });

        return new AlertDialog.Builder( getActivity() )
                .setTitle( R.string.date_of_crime )
                .setView( datePicker )
                .setPositiveButton( android.R.string.ok, okListener )
                .setNegativeButton( android.R.string.cancel, null )
                .create();
    }

    private DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick( DialogInterface dialog, int which )
        {
            if( getTargetFragment() == null ) return;

            Intent intent = new Intent();
            intent.putExtra( EXTRA_DATE, mDate );
            getTargetFragment().onActivityResult( CrimeFragment.REQUEST_CODE,
                    Activity.RESULT_OK, intent );
        }
    };

    public static DatePickerDialog newInstance( Date date )
    {
        Bundle extraData = new Bundle();
        extraData.putSerializable( EXTRA_DATE, date );

        DatePickerDialog instance = new DatePickerDialog();
        instance.setArguments( extraData );
        return instance;
    }
}
