package ai.salesken.v1.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ai.salesken.v1.activity.AccountActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.constant.SaleskenIntent;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;

public class UpdateUserAsync extends AsyncTask<String, Void, String> {
    private Context context;
    private AccountActivity accountActivity;
    private SaleskenActivity saleskenActivity;
    public UpdateUserAsync(Context context) {
        this.context = context;
        this.accountActivity=((AccountActivity)context);
        this.saleskenActivity=((SaleskenActivity)context);

    }

    @Override
    protected String doInBackground(String... strings) {
        accountActivity.user.setProfileImage(strings[0]);
        saleskenActivity.editor.putString(SaleskenSharedPrefKey.USER,saleskenActivity.gson.toJson(accountActivity.user));
        saleskenActivity.editor.commit();
        saleskenActivity.editor.apply();
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        accountActivity.updateDrawer();
    }
}
