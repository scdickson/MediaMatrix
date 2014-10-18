package scd.com.mediamatrix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
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

/**
 * Created by sdickson on 10/18/14.
 */
public class MatrixInitialization extends Activity
{
    SwipeView swipeView;
    static String SESSION_ID;
    static boolean isMaster;
    static Firebase myFirebaseRef;
    static int numConnected = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

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
        myFirebaseRef.child(Build.SERIAL).child("json").setValue(deviceparams.toString());

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
                    // Open up the activity that contains the photo selection process
                    Intent photoActivityIntent = new Intent(MatrixInitialization.this, PhotoActivity.class);
                    startActivity(photoActivityIntent);
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
                        numConnected = data.length;
                    }
                    catch(Exception e){}

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
            sessionHelp.setText("Waiting for all users to join...");

            imagePerson.setVisibility(View.GONE);
            actionDone.setVisibility(View.GONE);
        }

        //SwipeView container = (SwipeView) findViewById(R.id.swipe_view);
        //container = new SwipeView(this);
        //container.requestFocus();

    }

    protected void onDestroy()
    {
        super.onDestroy();
        Firebase condemned = new Firebase("https://mediamatrix.firebaseio.com/" + SESSION_ID + "/" + Build.SERIAL);
        condemned.removeValue();
    }





}
