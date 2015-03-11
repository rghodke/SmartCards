package com.example.admin.smartcards;

import java.io.File;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessOCR {
    private TessBaseAPI mTess;

    public TessOCR() {
        // TODO Auto-generated constructor stub
        mTess = new TessBaseAPI();
        String datapath = Environment.getExternalStorageDirectory() + "/Android/data/com.example.admin.smartcards/files/";
        //Log.e("tag", datapath);

        String language = "eng";
        File dir = new File(datapath + "tessdata/");
        if (!dir.exists())
             dir.mkdirs();
        mTess.init(datapath, language);
    }

    public String getOCRResult(Bitmap bitmap) {

        mTess.setImage(bitmap);
        String result = mTess.getUTF8Text();
        //Log.e("result", result);
        return result;
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
    }

}