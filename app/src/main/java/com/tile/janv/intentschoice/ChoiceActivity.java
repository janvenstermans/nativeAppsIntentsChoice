package com.tile.janv.intentschoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceActivity extends AppCompatActivity {

    public static final String WEBPAGE = "http://tinfbo2.hogent.be/nativeapps/index.php";
    public static final String LOG_TAG = "ChoiceActivity";

    //---------------------
    // lifecyle methods
    //---------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        ButterKnife.bind(this);
    }

    //---------------------
    // menu methods
    //---------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choice, menu);
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

    //---------------------
    // button methods
    //---------------------

    @OnClick(R.id.button_open_web_page)
    protected void onOpenWebPage() {
        Log.i(LOG_TAG, "Clicked button to open web page.");
        Intent openWebPageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WEBPAGE));
        tryStartingIntent(openWebPageIntent, R.string.open_web_page_fail);
    }

    @OnClick(R.id.button_open_contacts)
    protected void onOpenContacts() {
        Log.i(LOG_TAG, "Clicked button to open contacts.");
        Intent openContactsIntent = new Intent(Intent.ACTION_VIEW,  ContactsContract.Contacts.CONTENT_URI);
        tryStartingIntent(openContactsIntent, R.string.open_contacts_fail);
    }

    @OnClick(R.id.button_open_dialer)
    protected void onOpenDialer() {
        Log.i(LOG_TAG, "Clicked button to open dialer.");
        Intent openDialerIntent = new Intent(Intent.ACTION_DIAL);
        tryStartingIntent(openDialerIntent, R.string.open_dialer_fail);
    }

    @OnClick(R.id.button_search_google)
    protected void onSearchGoogle() {
        Log.i(LOG_TAG, "Clicked button to search google.");
        //http://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.search_google_dialog_title));
        alertDialog.setMessage(getString(R.string.search_google_dialog_message));
        final EditText input = new EditText(ChoiceActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton(getString(R.string.search_google_dialog_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String keywords = input.getText().toString();
                        Log.i(LOG_TAG, "Keywords entered to search in google: " + keywords);
                        searchGoogle(keywords);
                    }
                });
        alertDialog.show();
    }

    @OnClick(R.id.button_voice_command)
    protected void startVoiceCommand() {
        Log.i(LOG_TAG, "Clicked button to start voice command.");
        Intent intent = new Intent(Intent.ACTION_VOICE_COMMAND);
        tryStartingIntent(intent, R.string.voice_command_fail);
    }

    //---------------------
    // private methods
    //---------------------

    private void tryStartingIntent(Intent intent, int failStringId) {
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(failStringId), Toast.LENGTH_SHORT).show();
        }
    }

    private void searchGoogle(String keywords) {
        try {
            //http://stackoverflow.com/questions/13853375/how-to-search-a-string-on-google-using-default-browser
            String query = URLEncoder.encode(keywords, "utf-8");
            String url = "http://www.google.com/search?q=" + query;
            Intent searchGoogleIntent = new Intent(Intent.ACTION_VIEW);
            searchGoogleIntent.setData(Uri.parse(url));
            tryStartingIntent(searchGoogleIntent, R.string.search_google_fail);
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, getString(R.string.search_google_string_encoding_fail), Toast.LENGTH_SHORT).show();
        }
    }
}
