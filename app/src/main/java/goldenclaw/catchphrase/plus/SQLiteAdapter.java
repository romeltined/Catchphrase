package goldenclaw.catchphrase.plus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by romeltined on 8/3/17.
 */

public class SQLiteAdapter {

    public static final String MYDATABASE_NAME = "MY_DATABASE";
    public static final String MYDATABASE_TABLE = "MY_TABLE1";
    public static final int MYDATABASE_VERSION = 1;
    public static final String KEY_ID = "_id";
    public static final String COL_WORD = "Word";
    public static final String COL_CAT = "Category";
    public static final String COL_FLAG = "Flag";
    public static final String COL_ENABLE = "Enable";

    //create table MY_DATABASE (ID integer primary key, Content text not null);
    private static final String SCRIPT_CREATE_DATABASE =
            "create table " + MYDATABASE_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + COL_WORD + " text not null, "
                    + COL_CAT + " text not null, "
                    + COL_FLAG + " integer not null, "
                    + COL_ENABLE + " integer not null);";
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    public SQLiteAdapter(Context c){
        context = c;
        //c.deleteDatabase(MYDATABASE_NAME);
    }

    public SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteAdapter openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public long insert(String word, String category){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_WORD, word);
        contentValues.put(COL_CAT, category);
        contentValues.put(COL_FLAG, 0);
        contentValues.put(COL_ENABLE, 0);
        return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
    }


    public long updateEnableTrue(String cat){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ENABLE, 0);
        return sqLiteDatabase.update(MYDATABASE_TABLE, contentValues, COL_CAT + " = ?",
                new String[] { String.valueOf(cat) });
    }

    public long updateEnableFalse(String cat){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ENABLE, 1);
        return sqLiteDatabase.update(MYDATABASE_TABLE, contentValues, COL_CAT + " = ?",
                new String[] { String.valueOf(cat) });
    }

    public long updateFlag(long id){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FLAG, 1);
        return sqLiteDatabase.update(MYDATABASE_TABLE, contentValues, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public long updateEnable(String category, boolean value){

        int enable;
        if(value){
            enable=0;
        }else{
            enable=1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ENABLE, enable);
        return sqLiteDatabase.update(MYDATABASE_TABLE, contentValues, COL_CAT + " = ?",
                new String[] { String.valueOf(category) });
    }

    public int deleteCategory(String category){
        return sqLiteDatabase.delete(MYDATABASE_TABLE, COL_CAT + " = ?", new String[] { String.valueOf(category) });
    }

    public long resetFlag(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FLAG, 0);
        return sqLiteDatabase.update(MYDATABASE_TABLE, contentValues, "", null);
    }

    public int deleteAll(){
        return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
    }

    public String queueAll(){
        String[] columns = new String[]{COL_WORD};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,
                null, null, null, null, null);
        String result = "";

        int index_CONTENT = cursor.getColumnIndex(COL_WORD);
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            result = result + cursor.getString(index_CONTENT) + "\n";
        }
        return result;
    }

    // Getting All User data
    public ArrayList<String> getAllData() {

        ArrayList<String> wordList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + MYDATABASE_TABLE;

        Cursor cursor = sqLiteDatabase.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                wordList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return user list
        return wordList;
    }

    public ArrayList<CategoryItem> getCategoryArray() {

        ArrayList<CategoryItem> categoryList = new ArrayList<CategoryItem>();
        // Select All Query
        String selectQuery = "SELECT  MAX(Category) as Category, MAX(Enable) As Enable FROM " + MYDATABASE_TABLE + " GROUP BY Category, Enable ";

        Cursor cursor = sqLiteDatabase.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                categoryList.add(new CategoryItem(cursor.getString(0),cursor.getInt(1)));
            } while (cursor.moveToNext());
        }

        // return user list
        return categoryList;
    }

    public String getRandom() {
        // Select All Query
        String selectQuery = "SELECT * FROM " + MYDATABASE_TABLE + " WHERE Enable<>1 AND Flag<>1 ORDER BY RANDOM() LIMIT 1";
        String word = "";
        Cursor cursor = sqLiteDatabase.rawQuery ( selectQuery, null );

        if ( cursor.moveToFirst() ) {
            cursor.moveToFirst();
            word = cursor.getString(1);
            long id = cursor.getInt(0);
            long result = updateFlag(id);
        }
        else {
            resetFlag();
            //word = "Q@#DSACZ";
            word = getRandom();
        }

        // return user list
        return word;
    }


    public String checkData() {
        // Select All Query
        String selectQuery = "SELECT COUNT(*) FROM " + MYDATABASE_TABLE + " ";
        String word = "";
        Cursor cursor = sqLiteDatabase.rawQuery ( selectQuery, null );
        int count;
        if ( cursor.moveToFirst() ) {
            cursor.moveToFirst();
            //word = cursor.getString(1);
            count = cursor.getInt(0);
            if (count==0) {
                word="Populate";
            }
        }
        // return user list
        return word;
    }


    public String checkCategoryExist(String category) {
        // Select All Query
        String selectQuery = "SELECT COUNT(1) FROM " + MYDATABASE_TABLE + " WHERE Category='" + category + "'";
        String word = "";
        Cursor cursor = sqLiteDatabase.rawQuery ( selectQuery, null );
        int count;
        if ( cursor.moveToFirst() ) {
            cursor.moveToFirst();
            //word = cursor.getString(1);
            count = cursor.getInt(0);
            if (count==0) {
                word="NotExist";
            }
        }
        // return user list
        return word;
    }

    public Cursor getCategoryCursor() {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        String selectQuery = "SELECT  MAX(_id) as _id, MAX(Category) AS Category, Max(Enable) as Enable FROM " + MYDATABASE_TABLE + " GROUP BY Category, Enable ORDER BY _id";
        Cursor cursor = sqLiteDatabase.rawQuery ( selectQuery, null );
        //Cursor cursor = sqLiteDatabase.query(true, MYDATABASE_TABLE, new String[] {COL_CAT, COL_ENABLE}, null, null, null, null, null, null);
        return  cursor;
    }

    public Cursor getWordCursor() {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + MYDATABASE_TABLE + " ";
        Cursor cursor = sqLiteDatabase.rawQuery ( selectQuery, null );
        return  cursor;
    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            //context.deleteDatabase(MYDATABASE_NAME);
            db.execSQL(SCRIPT_CREATE_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }

}
