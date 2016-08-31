package com.froist_inc.josh.criminalintent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;

public class DatePickerDialog extends android.support.v4.app.DialogFragment
{
    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        return new AlertDialog.Builder( getActivity() )
                .setTitle( R.string.date_of_crime )
                .setView( new DatePicker( getActivity() ) )
                .setPositiveButton( android.R.string.ok, null )
                .setNegativeButton( android.R.string.cancel, null )
                .create();
    }

    private DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick( DialogInterface dialog, int which )
        {

        }
    };
}
