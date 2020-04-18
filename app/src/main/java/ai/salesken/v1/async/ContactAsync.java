package ai.salesken.v1.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ai.salesken.v1.activity.ContactActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.constant.SaleskenIntent;
import ai.salesken.v1.utils.ContactUtil;

public class ContactAsync extends AsyncTask<String, Void, String> {
    private Context context;
    private static final String TAG = "ContactAsync";

    public ContactAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected String doInBackground(String... strings) {
        return new ContactUtil().fetchContacts(context);
    }
    @Override
    protected void onPostExecute(String result) {
        Intent intent = new Intent();
        intent.setAction(SaleskenIntent.CONTACT_BROADCAST);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
