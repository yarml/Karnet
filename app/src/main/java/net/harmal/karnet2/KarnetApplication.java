package net.harmal.karnet2;

import android.app.Application;
import android.os.Looper;

import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.savefile.SaveFileRW;
import net.harmal.karnet2.utils.ErrorStream;
import net.harmal.karnet2.utils.ExternalActivityInterface;
import net.harmal.karnet2.utils.LogStream;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;

public class KarnetApplication extends Application
{
    public KarnetApplication()
    {
        super();
        Thread.setDefaultUncaughtExceptionHandler(this::uncaughtException);
        System.setOut(new PrintStream(new LogStream()));
        System.setErr(new PrintStream(new ErrorStream()));
        Logs.debug("Starting application");
    }

    private void uncaughtException(@NotNull Thread t, @NotNull Throwable e)
    {
        Logs.error("Uncaught exception at thread :" + t.toString());
        Logs.error("Starting exception trace: ");
        Logs.error("\n" + e.toString());
        Logs.error("\n\nThis is the end of the exception");
        Logs.error("Trying to save data");
        Logs.debug("Saving data");

        try
        {
            SaveFileRW.save(getApplicationContext().getFilesDir().getAbsolutePath());
        }
        catch (IOException e2)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Logs.debug("Reading save file");
        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        try
        {
            SaveFileRW.read(path);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        for(Customer c : CustomerRegister.get())
        {
            Logs.debug(c.cid() + ": " + c.name() + ", " + c.city() + ", " + c.phoneNum() + ", " + c.creationDate().toString());
        }
    }
}
