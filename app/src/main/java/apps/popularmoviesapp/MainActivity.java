package apps.popularmoviesapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;

import apps.popularmoviesapp.fragments.DetailFragment;
import apps.popularmoviesapp.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    private MainFragment mainFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Check if this call is the first call or not
        if(savedInstanceState == null) {
            fragmentManager = this.getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            mainFragment = new MainFragment();
            fragmentTransaction.replace(R.id.main_fragment, mainFragment);
            fragmentTransaction.commit();
            Log.d(LOG_TAG, "OnCreate() A new main fragment has been created");
        }
        //Don't replace current detail fragment
        else {
            fragmentManager = this.getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.attach(new DetailFragment());
            fragmentTransaction.commit();
            Log.d(LOG_TAG, "OnCreate() The old detail fragment wasn't replaced");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
