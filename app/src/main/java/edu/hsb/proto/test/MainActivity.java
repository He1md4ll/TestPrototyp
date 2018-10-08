package edu.hsb.proto.test;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends PermissionBase implements HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private MapFragment mapFragment = new MapFragment();
    private LoginFragment loginFragment = new LoginFragment();

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);

        initViews();

        checkLocation(success -> {
            if (success) {
                performFragmentTransaction(R.id.nav_map);
            } else {
                Toast.makeText(this, "Required permission denied", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (!item.isChecked()) {
            performFragmentTransaction(item.getItemId());
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void performFragmentTransaction(@IdRes int menuId) {
        switch (menuId) {
            case R.id.nav_map:
                replaceFragment(mapFragment);
                break;
            case R.id.nav_login:
                replaceFragment(loginFragment);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(Boolean.TRUE)
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_placeholder, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
}