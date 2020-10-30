package net.harmal.karnet2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.savefile.SaveFileRW;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.fragments.customer.CustomerFragmentDirections;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private NavController       navController      ;
    private DrawerLayout        drawerLayout       ;
    private Toolbar             toolbar            ;
    private AppBarConfiguration appBarConfiguration;
    private NavigationView      navigationView     ;
    private NavHostFragment     navHostFragment    ;

    private List<KarnetFragment> childFragments     ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logs.debug("Starting main activity");

        try
        {
            SaveFileRW.readSaveFile(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // init
        drawerLayout   = findViewById(R.id.drawer_layout        );
        toolbar        = findViewById(R.id.toolbar_activity_main);
        navigationView = findViewById(R.id.nav_view             );



        // setup nav controller, toolbar and nav drawer
        setSupportActionBar(toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.orderFragment   , R.id.stockFragment  ,
                R.id.customerFragment, R.id.productFragment, R.id.aboutFragment)
                .setOpenableLayout(drawerLayout)
                .build();

        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_activity_main_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        toolbar.setNavigationOnClickListener(this::onNavigationReturnClicked);

        navController.addOnDestinationChangedListener(this::onDestinationChanged);
    }

    /**
     * Bind each fragment with its options menu
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        assert navController.getCurrentDestination() != null;
        switch(navController.getCurrentDestination().getId())
        {
            case R.id.customerFragment:
                getMenuInflater().inflate(R.menu.customer_options_menu, menu);
                break;
            case R.id.orderFragment:
                getMenuInflater().inflate(R.menu.order_options_menu, menu);
                break;
            case R.id.customerAddModifyFragment:
                getMenuInflater().inflate(R.menu.options_menu_add_customer, menu);
                break;
            default:
                getMenuInflater().inflate(R.menu.default_options_menu, menu);
                break;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        for(KarnetFragment f : childFragments)
            if(f.isVisible())
            {
                f.onMenuOptionsSelected(item, navController);
                break;
            }
        return true;
    }

    /**
     * Closes the drawer when back is pressed
     */
    @Override
    public void onBackPressed()
    {
        Logs.debug("Called onBackPressed");
        if(drawerLayout == null)
        {
            super.onBackPressed();
            return;
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
        {
            super.onBackPressed();
            onSupportNavigateUp();
        }
    }

    /**
     * Makes navigation drawer icon and back
     * on toolbar work as they should
     */
    private void onNavigationReturnClicked(View view)
    {
        assert navController.getCurrentDestination() != null;
        if (appBarConfiguration.getTopLevelDestinations()
                .contains(navController.getCurrentDestination().getId())) // Top level destination
            drawerLayout.openDrawer(GravityCompat.START);
        else // Somewhere else
            navController.navigateUp();

    }

    public void registerFragment(KarnetFragment child)
    {
        if(childFragments == null)
            childFragments = new ArrayList<>();
        childFragments.add(child);
    }

    /**
     * Animates the drawer icon and makes sure it
     * is replaced with the arrow back when needed
     */
    private void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
        if (appBarConfiguration.getTopLevelDestinations().contains(destination.getId())) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                    R.string.nav_drawer_open, R.string.nav_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }
}
