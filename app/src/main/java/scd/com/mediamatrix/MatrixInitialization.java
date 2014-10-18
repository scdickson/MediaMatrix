package scd.com.mediamatrix;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.client.Firebase;

/**
 * Created by sdickson on 10/18/14.
 */
public class MatrixInitialization extends Activity
{
    SwipeView swipeView;
    static Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://mediamatrix.firebaseio.com/");

        swipeView = new SwipeView(this);
        setContentView(swipeView);
        swipeView.requestFocus();
    }



}
