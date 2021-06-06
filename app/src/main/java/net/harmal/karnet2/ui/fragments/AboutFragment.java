package net.harmal.karnet2.ui.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import net.harmal.karnet2.BuildConfig;
import net.harmal.karnet2.R;
import net.harmal.karnet2.savefile.SaveFileRW;
import net.harmal.karnet2.utils.ExternalActivityInterface;
import net.harmal.karnet2.utils.Logs;
import net.harmal.karnet2.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AboutFragment extends KarnetFragment
{
    private Button updateBtn    ;
    private Button backupBtn    ;
    private Button loadBackupBtn;

    public AboutFragment()
    {
        super(R.layout.fragment_about);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        this.updateBtn     = view.findViewById(R.id.btn_update     );
        this.backupBtn     = view.findViewById(R.id.btn_backup     );
        this.loadBackupBtn = view.findViewById(R.id.btn_load_backup);

        this.updateBtn.setOnClickListener(this::onUpdateButton     );
        this.backupBtn.setOnClickListener(this::onBackupBtn        );
        this.loadBackupBtn.setOnClickListener(this::onLoadBackupBtn);

        requireContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void onLoadBackupBtn(View view)
    {
        ExternalActivityInterface.grantPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, this::onLoadBackupPermission);
    }


    private void onBackupBtn(View view)
    {
        ExternalActivityInterface.grantPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, this::onBackupPermission);
    }

    private void onLoadBackupPermission(String[] requested, String[] granted, @NotNull String[] denied)
    {
        if(denied.length != 0)
        {
            Toast.makeText(requireContext(), R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
            return;
        }
        File src = Utils.appDataBackupFile();
        File dest = new File(requireContext().getFilesDir(), SaveFileRW.SAVE_FILE_NAME);
        if(Utils.copy(src, dest))
            Toast.makeText(requireContext(), R.string.load_backup_success, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(requireContext(), R.string.file_copy_error, Toast.LENGTH_SHORT).show();
        try {
            SaveFileRW.read(requireContext().getFilesDir().getAbsolutePath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(requireContext(), R.string.unable_to_load, Toast.LENGTH_SHORT).show();
        }
    }

    private void onBackupPermission(String[] requested, String[] granted, @NotNull String[] denied)
    {
        if(denied.length != 0)
        {
            Toast.makeText(requireContext(), R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
            return;
        }
        File src = new File(requireContext().getFilesDir(), SaveFileRW.SAVE_FILE_NAME);
        File dest = Utils.appDataBackupFile();
        File logSrc = new File(requireContext().getFilesDir(), SaveFileRW.LOGS_FILE_NAME);
        File logDest = Utils.appLogBackupFile();
        if(Utils.copy(logSrc, logDest))
            Toast.makeText(requireContext(), R.string.backup_log_success, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(requireContext(), R.string.file_copy_error, Toast.LENGTH_SHORT).show();
        if(Utils.copy(src, dest))
            Toast.makeText(requireContext(), R.string.backup_success, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(requireContext(), R.string.file_copy_error, Toast.LENGTH_SHORT).show();
    }

    private void onUpdateButton(View view)
    {
        ExternalActivityInterface.grantPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, this::onUpdatePermission);
    }

    private void onUpdatePermission(String[] requested, String[] granted, @NotNull String[] denied)
    {
        if(denied.length != 0)
        {
            Toast.makeText(requireContext(), R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
            return;
        }
        File oldApkFile = Utils.updateFile();
        if(oldApkFile.exists())
        {
            if(oldApkFile.delete())
                Logs.debug("Old File deleted");
            else
                Logs.debug("Couldn't delete old file");
        }
        else
            Logs.debug("Old file does not exist");
        Cache cache = new DiskBasedCache(requireContext().getCacheDir(), 1024*1024);
        Network net = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, net);
        queue.start();

        JsonObjectRequest test = new JsonObjectRequest(Request.Method.GET, Utils.GITHUB_API_LATEST, null, response -> {
            try {
                String tagName = response.getString("tag_name");
                Logs.debug("Response: " + tagName);
                if(BuildConfig.VERSION_NAME.equalsIgnoreCase(tagName))
                {
                    Toast.makeText(requireContext(), R.string.no_update_available, Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri uri = Uri.parse(Utils.updateLink(tagName));
                DownloadManager mgr = (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
                mgr.enqueue(new DownloadManager.Request(uri)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(getString(R.string.download_update))
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Utils.UPDATE_FILE_NAME));
                updateBtn.setEnabled(false);
            } catch (JSONException e) {
                Logs.debug("Error");
                e.printStackTrace();
            }
        }, error -> Logs.debug("Error"));
        queue.add(test);
        Logs.debug("Request now in queue");
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Logs.debug("Download complete");
            updateBtn.setEnabled(true);
            try
            {
                File apk = Utils.updateFile();
                Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", apk);

                Logs.debug("URI: " + apkUri.toString());
                Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE, apkUri);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                requireContext().grantUriPermission("com.google.android.packageinstaller", apkUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                startActivity(install);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        requireContext().unregisterReceiver(onComplete);
    }

}
