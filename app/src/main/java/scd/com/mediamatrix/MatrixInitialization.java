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

import com.firebase.client.Firebase;

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
        myFirebaseRef = new Firebase("https://mediamatrix.firebaseio.com/");

        setContentView(R.layout.swipe_view);
        Button newRow = (Button) findViewById(R.id.new_row_action);

        if(isMaster)
        {
            newRow.setVisibility(View.VISIBLE);
            newRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        else
        {
            newRow.setVisibility(View.GONE);
        }

        SwipeView container = (SwipeView) findViewById(R.id.swipe_view);
        container = new SwipeView(this);
        container.requestFocus();

    }





}
