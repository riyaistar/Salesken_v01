package ai.salesken.v1.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import ai.salesken.v1.async.ContactAsync;

public class ContactObserver  extends ContentObserver {
    private static final String TAG = "ContactObserver";
    // left blank below constructor for this Contact observer example to work
// or if you want to make this work using Handler then change below registering  //line
    private Context context;
    public ContactObserver(Context context) {
        super(null);
        this.context = context;
    }
    @Override
    public boolean deliverSelfNotifications() {
        return true;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.e(TAG, "onChange  fdd " + selfChange);

        Log.e(TAG, "~~~~~~" + selfChange);
        // Override this method to listen to any changes
    }
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        //On Contact add/delete this method is fired
        Log.e(TAG, "Contact add/delete" + selfChange);
        new ContactAsync(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;

    }

}