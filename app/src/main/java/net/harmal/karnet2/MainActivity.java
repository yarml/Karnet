package net.harmal.karnet2;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import net.harmal.karnet2.savefile.SaveFileRW;
import net.harmal.karnet2.utils.ExternalActivityInterface;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{

    private NavController       navController      ;
    private DrawerLayout        drawerLayout       ;
    private Toolbar             toolbar            ;
    private AppBarConfiguration appBarConfiguration;
    private NavigationView      navigationView     ;
    private NavHostFragment     navHostFragment    ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logs.debug("Creating main activity");

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

    @Override
    protected void onStop()
    {
        super.onStop();
        Logs.debug("Saving data");
        try
        {
            SaveFileRW.save(getFilesDir().getAbsolutePath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            ExternalActivityInterface.activityResulted(requestCode, data);
        else
            ExternalActivityInterface.cancel(requestCode);
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
     *
     * Closes keyboard if open
     */
    private void onNavigationReturnClicked(View view)
    {
        Logs.debug("navig return clicked");
        assert navController.getCurrentDestination() != null;
        if (appBarConfiguration.getTopLevelDestinations()
                .contains(navController.getCurrentDestination().getId())) // Top level destination
            drawerLayout.openDrawer(GravityCompat.START);
        else // Somewhere
            navController.navigateUp();

    }


    /**
     * Animates the drawer icon and makes sure it
     * is replaced with the arrow back when needed
     */
    private void onDestinationChanged(NavController controller, @NotNull NavDestination destination, Bundle arguments) {
        if (appBarConfiguration.getTopLevelDestinations().contains(destination.getId())) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                    R.string.nav_drawer_open, R.string.nav_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }
}
