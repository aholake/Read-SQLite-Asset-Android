package vtl.com.readdatabasefile;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by TanLoc on 22/09/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context context;

    private static final String DB_NAME = "student.db";
    public static final String DB_PATH = "data/data/vtl.com.readdatabasefile/databases/";
    private SQLiteDatabase mDatabase;

    //TABLE INFO
    public static final String TABLE_NAME = "students";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AGE = "age";
    public static final String KEY_SCORE = "score";
    public static final String[] ALL_KEY = {KEY_ID, KEY_NAME, KEY_AGE, KEY_SCORE};

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public void createDatabase() {
        boolean checkExist = checkDatabase();
        if (checkExist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase chkDatabase = null;
        String myPath = DB_PATH + DB_NAME;
//        chkDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
//        if (chkDatabase != null)
//            chkDatabase.close();
//        return chkDatabase != null ? true : false;
        File file = new File(myPath);
        return file.exists();
    }

    public void copyDatabase() throws IOException {
        InputStream mInputStream = context.getAssets().open(DB_NAME);
        String outPath = DB_PATH + DB_NAME;
        OutputStream mOutputStream = new FileOutputStream(outPath);

        byte[] buff = new byte[1024];
        int lengh;
        while ((lengh = mInputStream.read(buff)) > 0) {
            mOutputStream.write(buff, 0, lengh);
        }
        mOutputStream.flush();
        mOutputStream.close();
        mInputStream.close();
    }

    public void openDatabase() {
        String myPath = DB_PATH + DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (mDatabase != null)
            mDatabase.close();
        super.close();
    }

    public List<Student> getAllStudents() {
        List<Student> list = new LinkedList<>();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Student s = new Student();
            s.setId(cursor.getInt(0));
            s.setName(cursor.getString(1));
            s.setAge(cursor.getInt(2));
            s.setScore(cursor.getDouble(3));
            list.add(s);

            cursor.moveToNext();
        }
        return list;
    }

    public Student getStudent(int id) {
        Student s = new Student();
        Cursor cursor = mDatabase.query(TABLE_NAME, ALL_KEY, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();

        s.setId(cursor.getInt(0));
        s.setName(cursor.getString(1));
        s.setAge(cursor.getInt(2));
        s.setScore(cursor.getDouble(3));
        return s;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
