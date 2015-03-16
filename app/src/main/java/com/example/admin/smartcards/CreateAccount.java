package com.example.admin.smartcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class CreateAccount extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Parse.initialize(this, "oV6qA91rZR1xA35KeC3qu3N2tLqHCUGFEsEvjxY7", "N2Irz0ifsTeSFV0YrU0ayVpOMK29so6a8aW8fb2l");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
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

    public static boolean isWhite(final String string){
        return string != null && !string.isEmpty() && !string.trim().isEmpty();
    }

    public void signUp(View view) {

        EditText username   = (EditText)findViewById(R.id.editText1);
        EditText password   = (EditText)findViewById(R.id.editText2);
        EditText repassword   = (EditText)findViewById(R.id.editText4);
        EditText email   = (EditText)findViewById(R.id.editText5);

        ParseObject gameScore = new ParseObject("Usernames");
        String p = password.getText().toString();
        String e = email.getText().toString();
        String accountName = username.getText().toString();
        String re_p = repassword.getText().toString();
        final Context context = this;;

        if(p.equals(re_p) && !(e).equals("")){
            gameScore.put("usernames",accountName );
            gameScore.put("passwords", p);
            gameScore.put("email", e);
            gameScore.saveInBackground();

            Intent intent = new Intent(this, DeckList.class);
            startActivity(intent);
        }else if(!p.equals(re_p)) {
            Toast.makeText(context, "Password didn't match", Toast.LENGTH_LONG).show();
        }else if(e.equals("")) {
            Toast.makeText(context, "Enter Email", Toast.LENGTH_LONG).show();
        }

    }


}
