package edu.buffalo.cse.cse486586.simpledht;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 3/17/16.
 */
public class LDumpClickListener implements View.OnClickListener {


    private static final String TAG = OnTestClickListener.class.getName();
    private static final int TEST_CNT = 50;
    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";

    private final TextView mTextView;
    private final ContentResolver mContentResolver;
    private final Uri mUri;
    //private final ContentValues[] mContentValues;

    public LDumpClickListener(TextView _tv, ContentResolver _cr) {
        mTextView = _tv;
        mContentResolver = _cr;
        mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
        //mContentValues = initTestValues();
    }


    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

//    private ContentValues[] initTestValues() {
//        ContentValues[] cv = new ContentValues[TEST_CNT];
//        for (int i = 0; i < TEST_CNT; i++) {
//            cv[i] = new ContentValues();
//            cv[i].put(KEY_FIELD, "key" + Integer.toString(i));
//            cv[i].put(VALUE_FIELD, "val" + Integer.toString(i));
//        }
//
//        return cv;
//    }


    @Override
    public void onClick(View v) {
        new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private class Task extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String key="@";
            Cursor resultCursor=mContentResolver.query(mUri, null,
                    key, null, null);

            int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
            int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
            //System.out.println(keyIndex);
            //System.out.println(valueIndex);

            resultCursor.moveToFirst();

            int rownum=resultCursor.getCount();
            System.out.println(rownum);

            for (Integer i=1;i<=rownum;i++) {
                String returnKey = resultCursor.getString(keyIndex);
                String returnValue = resultCursor.getString(valueIndex);

                String output = returnKey + "  " + returnValue + "\n";

                publishProgress(output);
                //publishProgress(i.toString()+"\n");
                resultCursor.moveToNext();
            }

            resultCursor.close();
            return null;


        }

        protected void onProgressUpdate(String...strings) {
            mTextView.append(strings[0]);

            return;
        }
    }
}
