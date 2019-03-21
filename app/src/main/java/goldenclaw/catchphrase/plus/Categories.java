package goldenclaw.catchphrase.plus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.example.romeltined.catchphrase.R;

//import com.goldenclaw.catchphrase.plus.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Categories extends AppCompatActivity {
    private SQLiteAdapter mySQLiteAdapter;
    private ListView lvItems;
    ArrayList<String> list;
    private boolean hasNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setTitle("Categories");
        //final Context context = this;

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();
        ArrayList<CategoryItem> categoryArray = mySQLiteAdapter.getCategoryArray();
        mySQLiteAdapter.close();

        lvItems = (ListView) findViewById(R.id.lvItems);
        CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(this,categoryArray );
        lvItems.setAdapter(categoryAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_add) {
            // Do something in response to button
            Intent intent = new Intent(this, AddCategory.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_download) {
            // Do something in response to button
            UpdateCategories();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected  void UpdateCategories(){
        String url = "https://big9hq4vj3.execute-api.us-west-2.amazonaws.com/prod/getCategories";
        final ArrayList<CategoryNewItem> newlist = new ArrayList<CategoryNewItem>();

        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //String jsonResponse;
                        try {
                            String jsonResponse;
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject catItem = (JSONObject) response
                                        .get(i);
                                newlist.add(new CategoryNewItem(catItem.getInt("id"),catItem.getString("name"),catItem.getString("content")));
                            }

                            for (CategoryNewItem g: newlist)
                            {
                                AddNewCategory(g.Name,g.Content);
                            }

                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);

                            if(hasNew) {
                                Toast mToast = Toast.makeText(getApplicationContext(), "Download complete.", Toast.LENGTH_SHORT);
                                mToast.show();
                            }
                            else {
                                Toast mToast = Toast.makeText(getApplicationContext(), "No updates.", Toast.LENGTH_SHORT);
                                mToast.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(getBaseContext()).add(jsonRequest);


    }

    protected void AddNewCategory(String category, String content) {
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();
        String word = "";
        word=mySQLiteAdapter.checkCategoryExist(category);
        mySQLiteAdapter.close();
        if(word=="NotExist"){
            hasNew = true;
            list = null;
            list = new ArrayList(Arrays.asList(content.split("\\s*;\\s*")));

            mySQLiteAdapter = new SQLiteAdapter(this);
            mySQLiteAdapter.openToWrite();
            for (int i = 0; i < list.size(); i++) {
                //System.out.println(list.get(i));
                mySQLiteAdapter.insert(list.get(i), category.trim());
            }
            mySQLiteAdapter.close();
        }

    }
}
