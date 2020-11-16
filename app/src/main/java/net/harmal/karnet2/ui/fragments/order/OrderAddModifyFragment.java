package net.harmal.karnet2.ui.fragments.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.harmal.karnet2.R;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class OrderAddModifyFragment extends KarnetFragment
{

    private Button chooseCustomerBtn;
    private ImageButton editDeliveryPriceBtn;
    private ImageButton editDateBtn;
    private FloatingActionButton addStackBtn;
    private TextView noStackText;


    public OrderAddModifyFragment()
    {
        super(R.layout.fragment_add_modify_order);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        chooseCustomerBtn    = v.findViewById(R.id.btn_order_choose_customer               );
        editDeliveryPriceBtn = v.findViewById(R.id.btn_edit_delivery_price_add_modify_order);
        editDateBtn          = v.findViewById(R.id.btn_edit_date_add_modify_order          );
        addStackBtn          = v.findViewById(R.id.floating_btn_add_order_stack            );
        noStackText          = v.findViewById(R.id.text_order_no_stack                     );
        
    }
}
