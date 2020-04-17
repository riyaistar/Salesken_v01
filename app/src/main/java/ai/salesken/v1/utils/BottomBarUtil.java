package ai.salesken.v1.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.ContactActivity;
import ai.salesken.v1.activity.DialerActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.activity.TaskActivity;


public class BottomBarUtil {

    public void setupBottomBar(final BottomNavigationView bottomNavigationView, final Context context, int i) {
        bottomNavigationView.setSelectedItemId(i);
     //   setBottomNavigationView(bottomNavigationView, context);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.dialer:
                                if (((Activity) context) instanceof DialerActivity) {
                                    System.out.println("Dont call Task history in task history... ... .... ");
                                } else {
                                          //removeShiftMode(bottomNavigationView);
                                    Intent i = new Intent(context, DialerActivity.class);
                                    context.startActivity(i);
                                    ((Activity) context).overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                                    ((Activity) context).finish();
                                }
                                break;
                            case R.id.contact:

                                   if (((Activity) context) instanceof ContactActivity) {
                                       System.out.println("Dont call ContactActivity in ContactActivity... ... .... ");
                                   } else {
                                       //removeShiftMode(bottomNavigationView);
                                       Intent i = new Intent(context, ContactActivity.class);
                                       context.startActivity(i);
                                       ((Activity) context).overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                                       ((Activity) context).finish();
                                   }

                                break;

                            case R.id.tasks:
                                if (((Activity) context) instanceof TaskActivity) {
                                    System.out.println("Dont call task in tasks... ... .... ");
                                } else {
                                         //removeShiftMode(bottomNavigationView);
                                    Intent i = new Intent(context, TaskActivity.class);
                                    context.startActivity(i);
                                    ((Activity) context).overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                                    ((Activity) context).finish();
                                }
                                break;


                        }
                        return true;
                    }
                });

    }



 /*   private void setBottomNavigationView(BottomNavigationView bottomNavigationView, final Context context) {
        LayoutInflater inflater = Activity.getLayoutInflater();
        BottomNavigationMenuView navigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            BottomNavigationItemView view = (BottomNavigationItemView) navigationMenuView.getChildAt(i);
            View itemBottomNavigation = inflater.inflate(R.layout.item_bottom_navigation_bar, null, false);
            ((ImageView) itemBottomNavigation.findViewById(R.id.icon)).setImageDrawable(menu.getItem(i).getIcon());
            ((TextView) itemBottomNavigation.findViewById(R.id.title)).setText(menu.getItem(i).getTitle());
            view.removeAllViews();
            view.addView(itemBottomNavigation);
        }
    }
*/




//    public void removeShiftMode(BottomNavigationView view) {
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
//        try {
//            view.setSelected(false);
//            view.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
//        } catch (Exception e) {
//            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
//        }
//    }
}
