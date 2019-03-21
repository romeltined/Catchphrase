package goldenclaw.catchphrase.plus;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

//import com.goldenclaw.catchphrase.plus.R;

//import com.example.romeltined.catchphrase.R;

public class Configure extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar TIMERbar,DISTANCEbar, RATINGbar; // declare seekbar object variable
    private TextView TIMERtextProgress,DISTANCEtextProgress, RATINGtextProgress;
    private Switch swSound;
    private KeyStore store;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        setupActionBar();
        setTitle("Settings");

        String realtimeset;
        realtimeset = Integer.toString(setinitProgress(getTime()));
        TIMERtextProgress = (TextView)findViewById(R.id.TIMERtextViewProgressID);
        TIMERtextProgress.setText("Timer: "+ realtimeset + " seconds");

        int timeset;
        timeset =  setProgress(getTime());
        TIMERbar = (SeekBar)findViewById(R.id.TIMERseekBarID); // make seekbar object
        TIMERbar.setProgress(timeset);
        TIMERbar.setOnSeekBarChangeListener(this);

        TIMERbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                progress=progress+30;
                TIMERtextProgress = (TextView)findViewById(R.id.TIMERtextViewProgressID);
                TIMERtextProgress.setText("Timer: "+progress + " seconds");
                saveTime(progress);
            }
        });


        String sound;
        sound = getSound().toString();
        swSound = (Switch) findViewById(R.id.swSound);
        if(sound.equals("ON")){
            swSound.setChecked(true);
        }else{
            swSound.setChecked(false);
        }
        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    store.put("Sound", "ON");
                }
                else { //swEnable.setChecked(true);
                    store.put("Sound", "OFF");
                }
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    public void openMain(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void saveTime(int progress) {
        store = KeyStore.getInstance(getBaseContext());//Creates or Gets our key pairs.  You MUST have access to current context!
        progress=progress*1000;
        store.put("Timelength", Integer.toString(progress));
    }

    protected int setProgress(String progress) {
        int value = Integer.parseInt(progress);
        value = value/1000;
        return (value-30);
    }

    protected int setinitProgress(String progress) {
        int value = Integer.parseInt(progress);
        value = value/1000;
        return (value);
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

}
