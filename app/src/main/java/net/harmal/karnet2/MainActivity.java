package net.harmal.karnet2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import net.harmal.karnet2.savefile.SaveFileRW;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        try
        {
            SaveFileRW.readSaveFile(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setSupportActionBar(findViewById(R.id.toolbar_activity_main));
        NavigationUI.setupActionBarWithNavController(this,
                Navigation.findNavController(this, R.id.nav_host_activity_main_fragment));
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        return Navigation.findNavController(this, R.id.nav_host_activity_main_fragment).navigateUp()
            || super.onSupportNavigateUp();
    }
}
