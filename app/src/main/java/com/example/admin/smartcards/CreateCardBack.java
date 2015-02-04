package com.example.admin.smartcards;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CreateCardBack extends Activity {

    TextView ViewTitle;
    EditText Classtitle;
    EditText Entertitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card_back);
        ViewTitle = (TextView) findViewById(R.id.ViewTitle);
        Classtitle = (EditText) findViewById(R.id.EnterCourse);
        Entertitle = (EditText) findViewById(R.id.EnterTitle);

        String ctitle = "CHECK";

                        //Toast.makeText(getApplicationContext(), value,
                          //  Toast.LENGTH_SHORT).show();

                //Classtitle.getText().toString();
        ViewTitle.setText(ctitle);
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
}
