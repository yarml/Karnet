package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.Animations;

import org.jetbrains.annotations.NotNull;

public class StockPasswordDialog extends KarnetDialogFragment
{
    private EditText passwordEdit;
    public StockPasswordDialog()
    {
        super(R.string.enter_password, R.layout.dialog_stock_password);
        neutral(R.string.modify_stock_password);
        positive(R.string.ok);
        positiveListener((dialog, which) -> {});
        neutralListener((dialog, which) -> {});
    }

    private void onPositiveListener(View v)
    {
        if(passwordEdit.getText().toString().equals(Stock.password))
            dismiss();
        else
        {
            Animations.shake(passwordEdit);
            Toast.makeText(requireContext(), R.string.invalid_password, Toast.LENGTH_SHORT).show();
        }
    }

    private void onNeutralClick(DialogInterface dialog, int which)
    {
        ModifyStockPasswordDialog modifyStockPasswordDialog = new ModifyStockPasswordDialog();
        modifyStockPasswordDialog.show(getChildFragmentManager(), "");
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, @NotNull AlertDialog.Builder builder)
    {
        passwordEdit = v.findViewById(R.id.edit_stock_password);
        builder.setCancelable(false);
    }

    @Override
    protected void onDialogCreated(AlertDialog dialog)
    {
        super.onDialogCreated(dialog);
        dialog.setOnShowListener(dialog1 -> {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(this::onNeutralClick);
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(this::onPositiveListener);
        });
    }

    private void onNeutralClick(View view)
    {
        ModifyStockPasswordDialog modifyStockPasswordDialog = new ModifyStockPasswordDialog();
        modifyStockPasswordDialog.show(getChildFragmentManager(), "");
    }
}
