package scd.com.mediamatrix;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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
    SwipeView swipeView;
    static String SESSION_ID;
    static boolean isMaster;
    static Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        SESSION_ID = getIntent().getStringExtra("SESSION_ID");
        isMaster = getIntent().getBooleanExtra("IS_MASTER", false);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://mediamatrix.firebaseio.com/" + SESSION_ID + "/");

        setContentView(R.layout.swipe_view);
        Button newRow = (Button) findViewById(R.id.new_row_action);
        Button doneAction = (Button) findViewById(R.id.done_add_action);

        if(isMaster)
        {
            newRow.setVisibility(View.VISIBLE);
            doneAction.setVisibility(View.VISIBLE);
            newRow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    myFirebaseRef.child("+").setValue("NEW ROW");
                }
            });

            doneAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot)
                        {
                            String data[] = snapshot.getValue().toString().split(", ");

                            for(int i = 0; i < data.length; i++)
                            {
                                ArrayList<Device> row = new ArrayList<Device>();
                                while(i < data.length && !data[i].contains("+"))
                                {
                                    //Log.d("mm", data[i]);
                                    try
                                    {
                                        Device weboshi = new Device(new JSONObject(data[i].substring(data[i].indexOf("json=") + "json=".length(), data[i].length()-1)));
                                        row.add(weboshi);
                                    }
                                    catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    i++;
                                }
                                WorldCoordSystem.addRow(row);
                            }

                            for(ArrayList<Device> list : WorldCoordSystem.rows)
                            {
                                for(Device device : list)
                                {
                                    Log.d("mm", device.toString());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
            });
        }
        else
        {
            newRow.setVisibility(View.GONE);
            doneAction.setVisibility(View.GONE);
        }

        SwipeView container = (SwipeView) findViewById(R.id.swipe_view);
        container = new SwipeView(this);
        container.requestFocus();

    }





}
