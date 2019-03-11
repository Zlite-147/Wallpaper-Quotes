package com.example.zlite_147.wallpaperquotes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zlite_147.wallpaperquotes.Fragments.ImagesFragment;
import com.example.zlite_147.wallpaperquotes.Fragments.QuotesFragment;
import com.example.zlite_147.wallpaperquotes.Fragments.RecentsFragment;
import com.example.zlite_147.wallpaperquotes.Fragments.Trending;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
  //      toolbar.setTitle("Images");
        loadFragment(new ImagesFragment());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
    //                toolbar.setTitle("Images");
                    fragment = new ImagesFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_gifts:
      //              toolbar.setTitle("Quotes");
                    fragment = new QuotesFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_cart:
        //            toolbar.setTitle("Recents");
                    fragment = new RecentsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
          //          toolbar.setTitle("Trending");
                    fragment = new Trending();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this, "Tap To Exit", Toast.LENGTH_SHORT).show();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
