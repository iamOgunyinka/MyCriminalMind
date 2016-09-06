package com.froist_inc.josh.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SuppressWarnings( "deprecation" )
public class CameraViewFragment extends Fragment
{
    private Camera mCamera;
    public static final String PICTURE_FILENAME =
            "com.froist_inc.josh.criminalintent.EXTRA_PICTURE_FILENAME";

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.fragment_crime_camera, container, false );
        final View progressContainer = ( View ) view.findViewById( R.id.crime_camera_progressContainer );
        progressContainer.setVisibility( View.INVISIBLE );

        Button cameraTakeButton = ( Button ) view.findViewById( R.id.crime_camera_take_pictureButton );
        cameraTakeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v )
            {
                mCamera.takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {
                        progressContainer.setVisibility(View.VISIBLE);
                    }
                }, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken( byte[] data, Camera camera )
                    {
                        String filename = UUID.randomUUID() + ".jpg";
                        FileOutputStream os = null;
                        boolean success = true;
                        try {
                            os = getActivity().openFileOutput( filename, Context.MODE_PRIVATE );
                            os.write( data );
                        } catch( Exception except ) {
                            Toast.makeText(getActivity(), except.getMessage(),
                                    Toast.LENGTH_SHORT ).show();
                            success = false;
                        } finally {
                            if( os != null )
                            {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    Toast.makeText( getActivity(), e.getMessage(),
                                            Toast.LENGTH_SHORT ).show();
                                }
                            }
                        }
                        if ( success )
                        {
                            Toast.makeText(getActivity(), "File saved!",
                                    Toast.LENGTH_SHORT ).show();
                            Intent intent = new Intent();
                            intent.putExtra( PICTURE_FILENAME, filename );
                            getActivity().setResult( Activity.RESULT_OK, intent );
                        } else {
                            getActivity().setResult( Activity.RESULT_CANCELED );
                        }
                        getActivity().finish();
                    }
                });
            }
        });

        SurfaceView surfaceView = ( SurfaceView )
                view.findViewById( R.id.crime_camera_surfaceView );
        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType( SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );

        surfaceHolder.addCallback( new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if( mCamera != null ){
                        mCamera.setPreviewDisplay( surfaceHolder );
                    }
                } catch ( IOException except )
                {
                    Log.d( "", "Unable to preview camera", except );
                    mCamera = null;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height )
            {
                if( mCamera == null ) return;

                try {
                    Camera.Parameters parameters = mCamera.getParameters();
                    Camera.Size s = getBestPreviewSize(parameters.getSupportedPictureSizes());
                    parameters.setPreviewSize(s.width, s.height);
                    mCamera.setParameters( parameters );
                } catch ( Exception except ){
                    Toast.makeText( getActivity(), except.toString(),
                            Toast.LENGTH_SHORT ).show();
                }

                try {
                    mCamera.startPreview();
                } catch ( Exception exception )
                {
                    Log.d( "", "Could not start previewing with Camera", exception );
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                if( mCamera != null )
                {
                    mCamera.stopPreview();
                    mCamera = null;
                }
            }
        });
        return view;
    }

    @TargetApi(9)
    @Override
    public void onResume()
    {
        super.onResume();
        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ){
            mCamera = Camera.open();
        } else {
            mCamera = Camera.open( 0 );
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if( mCamera != null )
        {
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.Size getBestPreviewSize( List<Camera.Size> sizes )
    {
        StringBuilder builder = new StringBuilder();
        if( sizes.size() < 1 ) return null;

        Camera.Size size = sizes.get( 0 );
        int largestSize = size.width * size.height;
        for( Camera.Size s : sizes )
        {
            builder.append( "Size.h: " + s.height + ", Size.width: " + s.width + "\n" );
            int area = s.height * s.width;
            if( area > largestSize )
            {
                size = s;
                largestSize = area;
            }
        }
        Toast.makeText(getActivity(), builder.toString(), Toast.LENGTH_LONG ).show();
        return size;
    }
}
