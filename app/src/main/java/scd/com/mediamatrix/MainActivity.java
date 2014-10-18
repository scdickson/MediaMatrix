package scd.com.mediamatrix;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;


public class MainActivity extends Activity implements View.OnClickListener
{
    Button createMatrix, joinMatrix;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        context = this;

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
        if(view.equals(joinMatrix))
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Join a Media Matrix!");
            alert.setMessage("Enter Your Code:");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            alert.setView(input);

            alert.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    String value = input.getText().toString();
                    Intent intent = new Intent(context, MatrixInitialization.class);
                    intent.putExtra("SESSION_ID", value);
                    startActivity(intent);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();

        }
        else if(view.equals(createMatrix))
        {
            /*Firebase.setAndroidContext(this);
            Firebase myFirebaseRef = new Firebase("https://mediamatrix.firebaseio.com/");
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.doge);
            myFirebaseRef.child("DOGE").setValue(encodeTobase64(icon));*/
        }
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
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
