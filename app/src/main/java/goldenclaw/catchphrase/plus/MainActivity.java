package goldenclaw.catchphrase.plus;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;



//import com.example.romeltined.catchphrase.R;
//import com.goldenclaw.catchphrase.plus.R;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "timelength";
    private SQLiteAdapter mySQLiteAdapter;
    private KeyStore store;
    private String timelength;
    ArrayList<String> list;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Menu");

        //InitializeKeyStore();
        //Initialize Db
        InitializeDb();


        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, "ca-app-pub-7782738271291118~6734658589");

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("50B021F09390E0D80323F58CBC171476")
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_categories) {
            // Do something in response to button
            Intent intent = new Intent(this, Categories.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_configure) {
            // Do something in response to button
            Intent intent = new Intent(this, Configure.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openCatchphrase(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Catchphrase.class);
        intent.putExtra(EXTRA_MESSAGE, timelength);
        startActivity(intent);
    }

    public void openHow(View view) {
        // Do something in response to button
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("How to play?");

        String alert0 = "The game is played in two teams.\n";
        String alert1 = "1. Press Start to play.\n";
        String alert2 = "2. Get your teammate to guess the clue ASAP!\n";
        String alert3 = "3. Then handover the buzzer next to your right.\n";
        String alert4 = "4. You can make any physical gesture.\n";
        String alert5 = "5. You can say anything you want.\n";
        String alert6 = "6. BUT you CANNOT say any word on the actual clue.\n";
        String alert7 = "7. Swipe left for the next clue.\n";
        String alert8 = "8. If you're holding the buzzer when it goes off, you lose!";

        // set dialog message
        alertDialogBuilder
                .setMessage(alert0+alert1+alert2+alert3+alert4+alert5+alert6+alert7+alert8)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void openAbout(View view) {
        // Do something in response to button
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("About");

        // set dialog message
        alertDialogBuilder
                .setMessage("Created by Golden Claw Entertainment Pte. Ltd. v4.0" )
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                    }
                });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
    }


    protected void InitializeDb() {
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();
        String word = mySQLiteAdapter.checkData();
        mySQLiteAdapter.close();
        if(word=="Populate"){
            PopulateDatabase();
            InitializeKeyStore();
        }
    }

    protected void InitializeKeyStore() {
        store = KeyStore.getInstance(getBaseContext());//Creates or Gets our key pairs.  You MUST have access to current context!
        String initialize;
        store.put("Initialize", "A");
        store.put("Timelength", "30000");
        store.put("Sound", "ON");
        //timelength=store.get("Timelength");
    }



    protected void PopulateDatabase() {
        /*
         *  Create/Open a SQLite database
         *  and fill with dummy content
         *  and close it
         */

//        mySQLiteAdapter = new SQLiteAdapter(this);
//        mySQLiteAdapter.openToWrite();
//        //mySQLiteAdapter.deleteAll();
//        mySQLiteAdapter.insert("Chair","Home");
//        mySQLiteAdapter.insert("Robot","Home");
//        mySQLiteAdapter.insert("Sleep","Home");
//        mySQLiteAdapter.insert("Spoon","Home");
//        mySQLiteAdapter.insert("Head","Home");
//        mySQLiteAdapter.close();

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        DefaultData data = new DefaultData();
        String random = data.random;
        list = new ArrayList(Arrays.asList(random.split("\\s*;\\s*")));
        for (int i = 0; i < list.size(); i++) {
            mySQLiteAdapter.insert(list.get(i), "Random");
        }

        String buzzword = data.buzzword;
        list = new ArrayList(Arrays.asList(buzzword.split("\\s*;\\s*")));
        for (int i = 0; i < list.size(); i++) {
            mySQLiteAdapter.insert(list.get(i), "Buzzword");
        }

        mySQLiteAdapter.close();
    }



}
