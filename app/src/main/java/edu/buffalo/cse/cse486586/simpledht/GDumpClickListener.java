package edu.buffalo.cse.cse486586.simpledht;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 3/23/16.
 */
public class GDumpClickListener implements View.OnClickListener {

    private static final String TAG = OnTestClickListener.class.getName();
    private static final int TEST_CNT = 50;
    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";

    private final TextView mTextView;
    private final ContentResolver mContentResolver;
    private final Uri mUri;

    public GDumpClickListener(TextView _tv, ContentResolver _cr) {
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


    @Override
    public void onClick(View v) {new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    private class Task extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String key="*";
            Cursor resultCursor=mContentResolver.query(mUri, null,
                    key, null, null);

            int keyIndex = 0;//resultCursor.getColumnIndex(KEY_FIELD);
            int valueIndex =1; //resultCursor.getColumnIndex(VALUE_FIELD);
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
