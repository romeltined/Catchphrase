package goldenclaw.catchphrase.plus;

import android.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

//import com.example.romeltined.catchphrase.R;

//import com.goldenclaw.catchphrase.plus.R;

import java.util.ArrayList;


/**
 * Created by romeltined on 10/3/17.
 */

public class CategoryArrayAdapter extends ArrayAdapter<CategoryItem> {
    public CategoryArrayAdapter(Context context, ArrayList<CategoryItem> categoryitem) {
        super(context, 0, categoryitem);
    }
    private SQLiteAdapter mySQLiteAdapter;
    private String category;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CategoryItem categoryItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvCategory);
        final Switch swEnable = (Switch) convertView.findViewById(R.id.swEnable);
        tvName.setText(categoryItem.category);

        if(categoryItem.enable ==0){
            swEnable.setChecked(true);
        }else{
            swEnable.setChecked(false);
        }

        swEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View v = (View) buttonView.getParent();
                TextView t = null;
                t = (TextView) v.findViewById(R.id.tvCategory);
                if(t != null){
                    mySQLiteAdapter = new SQLiteAdapter(getContext());
                    mySQLiteAdapter.openToWrite();
                    mySQLiteAdapter.updateEnable(t.getText().toString(), isChecked);
                    mySQLiteAdapter.close();
                }
            }
        });


//        tvName.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                //String vname = v.toString();
//                //Toast.makeText(getContext(), t.getText().toString(), Toast.LENGTH_SHORT).show();
//                TextView t = null;
//                t = (TextView) v.findViewById(R.id.tvCategory);
//                //Toast.makeText(getContext(), t.getText().toString(), Toast.LENGTH_SHORT).show();
//                category = t.getText().toString();
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
//                alertDialogBuilder.setTitle("Warning!");
//                //alertDialogBuilder.setIcon(R.drawable.ic_delete);
//
//                // set dialog message
//                alertDialogBuilder
//                        .setMessage("Delete " + t.getText().toString() + " ?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,int id) {
//                                // if this button is clicked, close
//                                // current activity
//                                //MainActivity.this.finish();
//                                mySQLiteAdapter = new SQLiteAdapter(getContext());
//                                mySQLiteAdapter.openToWrite();
//                                mySQLiteAdapter.deleteCategory(category);
//                                mySQLiteAdapter.close();
//                            }
//                        })
//                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,int id) {
//                                // if this button is clicked, just close
//                                // the dialog box and do nothing
//                                dialog.cancel();
//                            }
//                        });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                // show it
//                alertDialog.show();
//                return false;
//            }
//        });

        return convertView;
    }


}