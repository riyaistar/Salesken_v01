package ai.salesken.v1.utils;


import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class SaveMediaAsync extends AsyncTask<String, Void, String> {
    private MediaSaver mediaSaver;

    public SaveMediaAsync(MediaSaver mediaSaver){
        this.mediaSaver = mediaSaver;
    }
    @Override
    protected String doInBackground(String... params) {
        Bitmap bmp = null;
        try{
            System.out.println("Course image --> "+params[0].replaceAll(" ", "%20"));
            URL url = new URL(params[0].replaceAll(" ", "%20"));
            InputStream fileInputStream = url.openStream();
            if(fileInputStream != null) {
                mediaSaver.save(fileInputStream);
            }else{
                System.out.println("Not Saving Course image --> ");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String string) {
    }
}
