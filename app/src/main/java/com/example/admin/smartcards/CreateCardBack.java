package com.example.admin.smartcards;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.daos.CardDBAdapter;
import com.example.admin.daos.DeckDBAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class CreateCardBack extends Activity {


    private TessOCR mTessOCR;
    //    private TextView mResult;
    private ProgressDialog mProgressDialog;
    //    private ImageView mImage;
    private String mCurrentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_PICK_PHOTO = 2;



    private void uriOCR(Uri uri) {
        Log.e("uriOCR", "Start of uriOCR");
        if (uri != null) {
            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                //mImage.setImageBitmap(bitmap);
                doOCR(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        Log.e("uriOCR", "End of uriOCR");
    }

    @Override
    protected void onResume() {
        Log.e("onResume", "Start of onResume");
        // TODO Auto-generated method stub
        super.onResume();

        Intent intent = getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            Uri uri = (Uri) intent
                    .getParcelableExtra(Intent.EXTRA_STREAM);
            uriOCR(uri);
        }
        Log.e("onResume", "End of onResume");
    }

    @Override
    protected void onPause() {
        Log.e("onPause", "Start of onPause");

        // TODO Auto-generated method stub
        super.onPause();
        Log.e("onPause", "End of onPause");

    }


    @Override
    protected void onDestroy() {
        Log.e("onDestroy", "Start of onDestroy");

        // TODO Auto-generated method stub
        super.onDestroy();

        mTessOCR.onDestroy();
        Log.e("onDestroy", "End of onDestroy");

    }

    private void dispatchTakePictureIntent() {
        //Log.e("dispatchTakePictureIntent", "Start of dispatchTakePictureIntent");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        //Log.e("dispatchTakePictureIntent", "End of dispatchTakePictureIntent");

    }

    /**
     * http://developer.android.com/training/camera/photobasics.html
     */
    private File createImageFile() throws IOException {
        Log.e("createImageFile", "Start of createImageFile");

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory()
                + "/TessOCR";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
        //Log.e("createImageFile", "End of createImageFile");

    }


    private void setPic() {
        Log.e("setPic", "Start of setPic");

        // Get the dimensions of the View

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        // Determine how much to scale down the image
        //int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        //bmOptions.inSampleSize = scaleFactor << 1;
        bmOptions.inPurgeable = true;

        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        //Bitmap scaledBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        //Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        bitmapglobe = Bitmap.createBitmap((BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)) , 0, 0, (BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)) .getWidth(), (BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)) .getHeight(), matrix, true);        //Bitmap bitmap = rotatedBitmap;

        doOCR(bitmapglobe);

        Log.e("setPic", "End of setPic");

    }

    Bitmap bitmapglobe = null;
    EditText CardBack;

    private void doOCR(final Bitmap bitmap) {
        Log.e("doOCR", "Start of doOCR");

        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "Doing OCR...", true);
        }
        else {
            mProgressDialog.show();
        }

        new Thread(new Runnable() {
            public void run() {

                final String result = mTessOCR.getOCRResult(bitmap);

                //final String[] results =  result.split("[ \\n ]");

//                List<String> results2 = new LinkedList<String>();
                //              for(String s:results)
                //            {
                //               if(s != " ")
                //                 results2.add(s);
                //       }
                final String results3 = result;
                //     results2.toArray(results3);




                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //for(String s:results)
                        //{
                        Log.e("words", result);
                        //}
                        if (result != null && !result.equals("")) {


                            match_text_dialog = new Dialog(CreateCardBack.this);
                            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
                            match_text_dialog.setTitle("Select Matching Text");
                            textlist = (ListView)match_text_dialog.findViewById(R.id.list);
                            matches_text = new ArrayList<String>(Arrays.asList(results3));
                            ArrayAdapter<String> adapter =    new ArrayAdapter<String>(CreateCardBack.this,
                                    android.R.layout.simple_list_item_1, matches_text);
                            textlist.setAdapter(adapter);
                            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    enterCardBack.setText(matches_text.get(position));

                                    match_text_dialog.hide();
                                }
                            });
                            match_text_dialog.show();
                        }

                        mProgressDialog.dismiss();
                    }

                });

            };
        }).start();
        Log.e("doOCR", "Start of doOCR");

    }




    TextView ViewTitle;
    TextView ViewCourse;


    String title;
    String course;
    String cardFront;
    String cardBack;
    int newDeck = 0;

    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_TAKE_PHOTO
                && resultCode == Activity.RESULT_OK) {
            setPic();
        }
        else if (requestCode == REQUEST_PICK_PHOTO
                && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                uriOCR(uri);
            }
        }



        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            match_text_dialog = new Dialog(CreateCardBack.this);
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle("Select Matching Text");
            textlist = (ListView)match_text_dialog.findViewById(R.id.list);
            matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter =    new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, matches_text);
            textlist.setAdapter(adapter);
            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Speech.setText(matches_text.get(position));
                    match_text_dialog.hide();
                }
            });
            match_text_dialog.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static final int REQUEST_CODE = 1234;
    ImageButton Start;
    EditText Speech;
    Dialog match_text_dialog;
    ListView textlist;
    ArrayList<String> matches_text;
    EditText enterCardBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card_back);
        mTessOCR = new TessOCR();
        Start = (ImageButton)findViewById(R.id.button5);
        enterCardBack = (EditText)findViewById(R.id.enterCardBack);
        EditText as = (EditText)findViewById(R.id.enterCardBack);
        as.requestFocus();

        Speech = as;


        //Toast.makeText(getApplicationContext(),
        //                          deck, Toast.LENGTH_LONG)
        //                       .show();

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Speech == null && isConnected())
                {
                    Toast.makeText(getApplicationContext(), "Please select field", Toast.LENGTH_SHORT).show();
                }
                else if(isConnected()){
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
                }}
        });

        enterCardBack = (EditText)findViewById(R.id.enterCardBack);

        ViewTitle = (TextView) findViewById(R.id.viewTitle);
        ViewCourse = (TextView) findViewById(R.id.viewCourse);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        course = intent.getStringExtra("course");
        cardFront = intent.getStringExtra("cardFront");
        newDeck = intent.getIntExtra("newDeck", 0);

        ViewTitle.setText(title);
        ViewCourse.setText(course);
    }

    public void goDone (View view)
    {
        cardBack = enterCardBack.getText().toString();

        DeckDBAdapter deckHelper = new DeckDBAdapter(this);
        CardDBAdapter cardHelper = new CardDBAdapter(this);

        deckHelper.open();

        if (newDeck == 0) {
            newDeck = (int) deckHelper.createDeck(title, course);
        }

        cardHelper.open();

        cardHelper.createCard(newDeck, cardFront, cardBack);

        Intent intent = new Intent(CreateCardBack.this, DeckList.class);
        startActivity(intent);
    }

    public void nextCard (View view)
    {
        cardBack = enterCardBack.getText().toString();

        DeckDBAdapter deckHelper = new DeckDBAdapter(this);
        CardDBAdapter cardHelper = new CardDBAdapter(this);

        deckHelper.open();

        if (newDeck == 0) {
            newDeck = (int) deckHelper.createDeck(title, course);
        }

        cardHelper.open();

        cardHelper.createCard(newDeck, cardFront, cardBack);

        Intent intent = new Intent(CreateCardBack.this, CreateCardFront.class);
        intent.putExtra("title", title);
        intent.putExtra("course", course);
        intent.putExtra("newDeck", newDeck);
        if(bitmapglobe != (null)) {
            bitmapglobe.recycle();
        }
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_card_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ocrrecord (View view)
    {
        dispatchTakePictureIntent();
    }

}

