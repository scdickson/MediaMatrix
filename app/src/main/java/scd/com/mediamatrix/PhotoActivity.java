package scd.com.mediamatrix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class PhotoActivity extends Activity {
    private static final int SELECT_PHOTO = 100;

    Context context;
    static Firebase myFirebaseRef;
    ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.photo_view);
        context = this;

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://mediamatrix.firebaseio.com/");

        TextView castHelpView = (TextView) findViewById(R.id.cast_help);
        castHelpView.setText("Arrange devices as shown above, then cast away!");

        imagePreview = (ImageView) findViewById(R.id.preview);

        Button castButton = (Button) findViewById(R.id.action_cast);
        castButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open up the image gallery and get an image
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    //Log.d("mm", yourSelectedImage.getWidth() + " " + yourSelectedImage.getHeight());


                    // Update the image preview to be the selected image
                    imagePreview.setImageBitmap(yourSelectedImage);
                    // Encode the selected image and store it into FireBase
                    String encodedImage = MainActivity.encodeTobase64(yourSelectedImage);
                    //Log.d("mm", encodedImage);

                    myFirebaseRef.child(MatrixInitialization.SESSION_ID).child("IMAGE").setValue(encodedImage);

                    SortAndFill.setImagePoints(yourSelectedImage.getWidth(), yourSelectedImage.getHeight());

                    for(Device device : MatrixInitialization.devices)
                    {
                        String line = device.imagePoint.x + ";" + device.imagePoint.y + ";" + device.imageHeight + ";" + device.imageWidth;
                        myFirebaseRef.child(MatrixInitialization.SESSION_ID).child(device.deviceID).child("coords").setValue(line);
                    }

                    // Hide the device layout preview
                    scd.com.mediamatrix.DeviceLayoutView dv = (scd.com.mediamatrix.DeviceLayoutView) findViewById(R.id.deviceLayout);
                    dv.setVisibility(View.INVISIBLE);

                    // Unhide the image view
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.photoPreviewRelativeLayout);
                    rl.setVisibility(View.VISIBLE);
                }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }
}
