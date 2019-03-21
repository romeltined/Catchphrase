package goldenclaw.catchphrase.plus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

//import com.goldenclaw.catchphrase.plus.R;

import java.util.ArrayList;
import java.util.Arrays;

public class AddCategory extends AppCompatActivity {
    private SQLiteAdapter mySQLiteAdapter;
    EditText editText;
    String strName;
    String strValue;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        setTitle("Add new category");
        final Context context = this;
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_category_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_save) {
            editText   = (EditText)findViewById(R.id.txtName);
            strName = editText.getText().toString();
            editText   = (EditText)findViewById(R.id.txtValue);
            strValue = editText.getText().toString();

            if(!strName.trim().isEmpty() && !strValue.trim().isEmpty()) {
                list = null;
                list = new ArrayList(Arrays.asList(strValue.split("\\s*;\\s*")));

                mySQLiteAdapter = new SQLiteAdapter(this);
                mySQLiteAdapter.openToWrite();
                for (int i = 0; i < list.size(); i++) {
                    //System.out.println(list.get(i));
                    mySQLiteAdapter.insert(list.get(i), strName.trim());
                }
                mySQLiteAdapter.close();
                Intent intent = new Intent(this, Categories.class);
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void openCategories(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Categories.class);
        startActivity(intent);
    }

}
