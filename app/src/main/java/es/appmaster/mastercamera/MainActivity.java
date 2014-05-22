package es.appmaster.mastercamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import es.appmaster.mastercamera.utils.FileUtils;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    private Button takeAPictureButton;
    private Button pickAPictureButton;
    private ImageView thumbView;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takeAPictureButton = (Button) findViewById(R.id.take_a_picture);
        takeAPictureButton.setOnClickListener(this);

        pickAPictureButton = (Button) findViewById(R.id.pick_a_picture);
        pickAPictureButton.setOnClickListener(this);

        thumbView = (ImageView) findViewById(R.id.thumb_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_a_picture:
                dispatchTakePictureIntent();
                break;
            case R.id.pick_a_picture:
                dispatchPickPictureIntent();
                break;
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = FileUtils.getOutputMediaFileUri(FileUtils.MEDIA_TYPE_IMAGE); // crea un archivo y devuelve la URI
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // con este extra, el intent devuelto es null
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK);
        pickPictureIntent.setType("image/*");
        if (pickPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPictureIntent, REQUEST_PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            thumbView.setImageBitmap(imageBitmap);*/

            try {
                /*stream = getContentResolver().openInputStream(fileUri);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);*/

                Bitmap bitmap = FileUtils.decodeUri(this, fileUri);
                thumbView.setImageBitmap(bitmap);

                FileUtils.galleryAddPic(this, fileUri.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = FileUtils.decodeUri(this, data.getData());
                thumbView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
