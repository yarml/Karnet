package net.harmal.karnet2;

import android.app.Application;

import net.harmal.karnet2.savefile.SaveFileRW;
import net.harmal.karnet2.utils.Logs;

public class KarnetApplication extends Application
{
    public KarnetApplication()
    {
        super();

        Logs.debug("Starting application");
        try
        {
            SaveFileRW.readSaveFile(getFilesDir().getAbsolutePath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
