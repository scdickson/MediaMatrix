package scd.com.mediamatrix;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sdickson on 10/18/14.
 */
public class MatrixInitialization extends Activity
{
    Context context;
    SwipeView swipeView;
    static String SESSION_ID;
    static boolean isMaster;
    static Firebase myFirebaseRef;
    static int numConnected = 0;
    static int width = 0;
    ArrayList<Device> devices = new ArrayList<Device>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        this.context = this;
        SESSION_ID = getIntent().getStringExtra("SESSION_ID");
        isMaster = getIntent().getBooleanExtra("IS_MASTER", false);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        JSONObject deviceparams = new JSONObject();
        try
        {
            deviceparams.put("SERIAL", Build.SERIAL);
            deviceparams.put("WIDTH", size.x);
            deviceparams.put("HEIGHT", size.y);
            deviceparams.put("IS_VERTICAL", false);
            deviceparams.put("IS_FLIPPED", false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://mediamatrix.firebaseio.com/" + SESSION_ID + "/");

        if(!isMaster) {
            myFirebaseRef.child(Build.SERIAL).child("json").setValue(deviceparams.toString());
        }

        setContentView(R.layout.swipe_view);
        ImageView mainImage = (ImageView) findViewById(R.id.image);

        final ImageView imageNumber = (ImageView) findViewById(R.id.image_number);
        ImageView imagePerson = (ImageView) findViewById(R.id.person_icon);
        TextView sessionHelp = (TextView) findViewById(R.id.session_help);
        Button actionDone = (Button) findViewById(R.id.action_done);


        if(isMaster)
        {
            imageNumber.setVisibility(View.VISIBLE);
            imagePerson.setVisibility(View.VISIBLE);
            sessionHelp.setVisibility(View.VISIBLE);
            sessionHelp.setText("Join With Code " + SESSION_ID + " and Press 'Done' When Finished!");
            actionDone.setVisibility(View.VISIBLE);
            actionDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(devices.size() < 1)
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("Error")
                                .setMessage("You can't display an image with one or fewer devices...")
                                .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .show();
                    }
                    else {
                        Log.d("mm", "Calling SAF with ArrayList size " + devices.size() + " and width " + width);
                        SortAndFill saf = new SortAndFill(devices, width);
                        saf.Pack();
                        // Open up the activity that contains the photo selection process
                        Intent photoActivityIntent = new Intent(MatrixInitialization.this, PhotoActivity.class);
                        startActivity(photoActivityIntent);
                    }
                }
            });

            myFirebaseRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot snapshot)
                {
                    try
                    {
                        String data[] = snapshot.getValue().toString().split(", ");
                        Log.d("mm", snapshot.getValue().toString());

                        if(snapshot.getValue().toString().contains(", "))
                        {
                            for(String line : data)
                            {
                                if(!line.contains("IMAGE")) {
                                    JSONObject json = new JSONObject(line.substring(line.indexOf("json=") + "json=".length(), line.length() - 1));
                                    Device dev = new Device(json);
                                    //Log.d("mm", dev.toString());
                                    width += dev.width;

                                    if (!devices.contains(dev)) {
                                        Log.d("mm", "ADD");
                                        devices.add(dev);
                                    }
                                }
                            }
                        }
                        else //Catches case where only one device is in matrix
                        {
                            JSONObject json = new JSONObject(data[0].substring(data[0].indexOf("json=") + "json=".length(), data[0].length()-1));
                            Device dev = new Device(json);
                            //Log.d("mm", dev.toString());
                            width += dev.width;

                            if(!devices.contains(dev))
                            {
                                Log.d("mm", "ADD");
                                devices.add(dev);
                            }
                        }

                        numConnected = data.length;
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    String stringRsc = null;

                    switch (numConnected) {
                        case 1:
                            stringRsc = "scd.com.mediamatrix:drawable/one";
                            break;
                        case 2:
                            stringRsc = "scd.com.mediamatrix:drawable/two";
                            break;
                        case 3:
                            stringRsc = "scd.com.mediamatrix:drawable/three";
                            break;
                        case 4:
                            stringRsc = "scd.com.mediamatrix:drawable/four";
                            break;
                        case 5:
                            stringRsc = "scd.com.mediamatrix:drawable/five";
                            break;
                        case 6:
                            stringRsc = "scd.com.mediamatrix:drawable/six";
                            break;
                        case 7:
                            stringRsc = "scd.com.mediamatrix:drawable/seven";
                            break;
                        case 8:
                            stringRsc = "scd.com.mediamatrix:drawable/eight";
                            break;
                        case 9:
                            stringRsc = "scd.com.mediamatrix:drawable/nine";
                            break;
                        default:
                            stringRsc = "scd.com.mediamatrix:drawable/ten";
                            break;
                    }

                    if (stringRsc != null) {
                        int id = getResources().getIdentifier(stringRsc, null, null);
                        imageNumber.setImageResource(id);
                    }
                }

                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
        else
        {
            int id = getResources().getIdentifier("scd.com.mediamatrix:drawable/hourglass", null, null);
            imageNumber.setImageResource(id);
            //sessionHelp.setText("Waiting for all users to join...");

            imagePerson.setVisibility(View.GONE);
            actionDone.setVisibility(View.GONE);

            myFirebaseRef = new Firebase("https://mediamatrix.firebaseio.com/" + SESSION_ID + "/IMAGE/");
            myFirebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot)
                {
                    try
                    {
                        String rawImage = snapshot.getValue().toString();
                        //if (rawImage != null && rawImage.contains("IMAGE"))
                        if (rawImage != null)
                        {
                            //Log.d("mm", rawImage.substring(rawImage.indexOf("{IMAGE=") + "{IMAGE=".length()));
                            Bitmap b = MainActivity.decodeBase64(rawImage);
                            imageNumber.setImageBitmap(b);

                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }

        //SwipeView container = (SwipeView) findViewById(R.id.swipe_view);
        //container = new SwipeView(this);
        //container.requestFocus();

    }

    protected void onDestroy()
    {
        super.onDestroy();

        if(isMaster)
        {
            devices.clear();
            numConnected = 0;
            width = 0;
            Firebase condemned = new Firebase("https://mediamatrix.firebaseio.com/" + SESSION_ID);
            condemned.removeValue();
        }
        else
        {
            Firebase condemned = new Firebase("https://mediamatrix.firebaseio.com/" + SESSION_ID + "/" + Build.SERIAL);
            condemned.removeValue();
        }
    }





}
