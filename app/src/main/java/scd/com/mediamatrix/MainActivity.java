package scd.com.mediamatrix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

<<<<<<< HEAD
import java.util.ArrayList;

=======
import com.firebase.client.Firebase;
>>>>>>> c7c7109c2a7dbb1d7fd0da0a5e91c0bf480f6d6e


public class MainActivity extends Activity implements View.OnClickListener
{
    Button createMatrix, joinMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        //swipeView = new SwipeView(this);
        //setContentView(swipeView);
        //swipeView.requestFocus();

        //test code
//        new Device(1, 3, false);
//        new Device(2, 2, true);
//        new Device(2, 3, false);
//        new Device(3, 1, true);
//        ArrayList<Device> row = new ArrayList<Device>();
//        row.add(WorldCoordSystem.devices.get(0));
//        row.add(WorldCoordSystem.devices.get(1));
//        row.add(WorldCoordSystem.devices.get(2));
//        WorldCoordSystem.addRow(row);
//        row.clear();
//        row.add(WorldCoordSystem.devices.get(0));
//        row.add(WorldCoordSystem.devices.get(3));
//        WorldCoordSystem.addRow(row);
//
//        WorldCoordSystem.setWorldCoordSystem();
        //Log.d("test", "World width = " + WorldCoordSystem.worldWidth + " World height = " + WorldCoordSystem.worldHeight);

        createMatrix = (Button) findViewById(R.id.create_action);
        createMatrix.setOnClickListener(this);
        joinMatrix = (Button) findViewById(R.id.join_action);
        joinMatrix.setOnClickListener(this);

    }

    public void onClick(View view)
    {
        if(view.equals(createMatrix))
        {
            Intent intent = new Intent(this, MatrixInitialization.class);
            startActivity(intent);
        }
        else if(view.equals(joinMatrix))
        {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
