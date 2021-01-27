package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.harmal.karnet2.R;

import org.jetbrains.annotations.NotNull;

public class WaitDialog extends KarnetDialogFragment
{
    public interface Task
    {
        void doTask();
    }

    private TextView msgText     ;
    private final String   msg         ;
    private final String   taskEndToast;
    private final Task     task        ;

    public WaitDialog(Task task, String msg, String taskEndToast, IBinder windowToken)
    {
        super(R.string.wait, R.layout.dialog_wait, windowToken);
        this.msg          = msg         ;
        this.taskEndToast = taskEndToast;
        this.task         = task        ;
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, @NotNull AlertDialog.Builder builder)
    {
        msgText = v.findViewById(R.id.text_wait_msg);
        msgText.setText(msg);
        addOnDismissEvent(dialog ->
                Toast.makeText(requireContext(), taskEndToast, Toast.LENGTH_SHORT));
        builder.setCancelable(false);
        Thread taskThread = new Thread(() -> {
            task.doTask();
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), taskEndToast, Toast.LENGTH_SHORT).show();
                dismiss();
            });
        });
        taskThread.start();
    }
}
