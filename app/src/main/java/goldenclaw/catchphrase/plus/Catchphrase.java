package goldenclaw.catchphrase.plus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

//import com.goldenclaw.catchphrase.plus.R;

//import com.example.romeltined.catchphrase.R;

public class Catchphrase extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private SQLiteAdapter mySQLiteAdapter;
    Button btn;
    long startSound=28000;
    private CountDownTimer count;
    private boolean started = false;
    private boolean stateAudio = false;
    private KeyStore store;
    private boolean swipeEnable=false;
    private static final int SWIPE_MIN_DISTANCE = 250; //120;  200,550,100 ok
    private static final int SWIPE_MAX_OFF_PATH = 850; //250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200; //200;
    private GestureDetector gestureScanner;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catchphrase);
        setTitle("Catchphrase");
        gestureScanner = new GestureDetector(getBaseContext(),this);
        setupActionBar();

        final MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.onebit);
        final MediaPlayer mp2 = MediaPlayer.create(getBaseContext(), R.raw.thirtysectimer);
        final Context context = this;
        long timelength=Long.parseLong(getTime());


        count= new CountDownTimer(timelength, 1) {
            public void onTick(long millisUntilFinished) {
                String sound = getSound();
                if (sound.equals("ON")) {
                    //stateAudio = true;
                    if (millisUntilFinished <= startSound) {
                        mp2.start();
                    }
                    else
                    {
                        mp.start();
                    }
                }
            }
            @Override
            public void onFinish() {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Sorry, you lose!!!");
                builder1.setCancelable(false);
                started=false;

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(getBaseContext(), Catchphrase.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        };

        final Button btnStart = (Button) findViewById((R.id.button));
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!started){
                    //btnStart = (Button) findViewById(R.id.button);
                    btnStart.setText("Stop");
                    //btnStart.setEnabled(false);
                    getRandomWord();
                    //stateAudio=false;
                    hideActionBar();
                    count.start(); // START COUNTDOWN TIMER
                    started = true;
                    //swipeEnable=true;
                }
                else {
                    //getRandomWord();
                    Toast mToast = Toast.makeText(getApplicationContext(), "Hold to Stop", Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });

        btnStart.setOnLongClickListener (new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(started){
                    mp.stop();
                    mp2.stop();
                    count.cancel();
                    Intent intent = new Intent(getBaseContext(), Catchphrase.class);
                    startActivity(intent);
                    //swipeEnable=true;
                }
                else {
                    //getRandomWord();
                }
                return true;
            }
        });



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

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void openMain(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void getRandomWord() {

        final TextView listContent = (TextView) findViewById(R.id.wordtoguess);
        mySQLiteAdapter = new SQLiteAdapter(getBaseContext());
        mySQLiteAdapter.openToRead();
        String word = mySQLiteAdapter.getRandom();
        mySQLiteAdapter.close();
        listContent.setText(word);
    }

    protected String getTime() {
        store = KeyStore.getInstance(getBaseContext());//Creates or Gets our key pairs.  You MUST have access to current context!
        String timelength;
        timelength=store.get("Timelength");
        return timelength;
    }

    protected String getSound() {
        store = KeyStore.getInstance(getBaseContext());//Creates or Gets our key pairs.  You MUST have access to current context!
        String sound;
        sound=store.get("Sound");
        return sound;
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {

        return gestureScanner.onTouchEvent(me);
    }
    //@Override
    public boolean onDown(MotionEvent e) {
        //viewA.setText("-" + "DOWN" + "-");
        return true;
    }
    //@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH || Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
                return false;

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // right to left swipe
                //Toast.makeText(getApplicationContext(), "Swipe right to left", Toast.LENGTH_SHORT).show();
                if(started){
                    getRandomWord();
                }
                else {
                    //getRandomWord();
                }
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // left to right swipe
                //Toast.makeText(getApplicationContext(), "Swipe left to right", Toast.LENGTH_SHORT).show();
            } else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // bottom to top
                //Toast.makeText(getApplicationContext(), "Swipe bottom to top", Toast.LENGTH_SHORT).show();
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // top to bottom
                //Toast.makeText(getApplicationContext(), "Swipe top to bottom", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            // nothing
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //Toast mToast = Toast.makeText(getApplicationContext(), "Long Press", Toast.LENGTH_SHORT);
        //mToast.show();
        //return true;
    }
    //@Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //viewA.setText("-" + "SCROLL" + "-");
        return true;
    }
    //@Override
    public void onShowPress(MotionEvent e) {
        //viewA.setText("-" + "SHOW PRESS" + "-");
    }
    //@Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(started) {
            Toast mToast = Toast.makeText(getApplicationContext(), "Swipe left for next clue!", Toast.LENGTH_SHORT);
            mToast.show();
        }
          return true;
    }


    @Override
    public void onBackPressed() {
        //Disable BackPressed
    }


}
