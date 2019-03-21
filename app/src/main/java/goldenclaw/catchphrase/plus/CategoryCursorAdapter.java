package goldenclaw.catchphrase.plus;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//import com.goldenclaw.catchphrase.plus.R;

//import com.example.romeltined.catchphrase.R;

/**
 * Created by romeltined on 9/3/17.
 */

public class CategoryCursorAdapter extends CursorAdapter {
    public CategoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow("Category"));
        // Populate fields with extracted properties
        tvCategory.setText(body);


        final Switch switch1 = (Switch) view.findViewById(R.id.switch1);

        int boolenable = cursor.getInt(cursor.getColumnIndexOrThrow("Enable"));
        if (boolenable == 0) {
            switch1.setChecked(true);
        }
        else {
            switch1.setChecked(false);
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View v = (View) buttonView.getParent();
                TextView t = null;
                t = (TextView) v.findViewById(R.id.tvCategory);
                if (t != null) {
                    Toast.makeText(context, t.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
