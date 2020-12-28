package net.harmal.karnet2.ui.fragments.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.adapters.OrderStacksAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class OrderDetailsFragment extends KarnetFragment
{

    private int oid;

    private LinearLayout deliveryPriceLayout;
    private TextView     deliveryPriceText  ;
    private TextView     dateText           ;
    private TextView     totalPriceText     ;
    private RecyclerView               stackList;
    private RecyclerView.LayoutManager stackListLayoutManager;
    private OrderStacksAdapter         stackListAdapter;

    public OrderDetailsFragment()
    {
        super(R.layout.fragment_details_order);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        OrderDetailsFragmentArgs args = OrderDetailsFragmentArgs.fromBundle(requireArguments());
        oid = args.getOid();

        deliveryPriceLayout = view.findViewById(R.id.layout_delivery_price            );
        deliveryPriceText   = view.findViewById(R.id.text_order_details_delivery_price);
        dateText            = view.findViewById(R.id.text_order_details_date          );
        totalPriceText      = view.findViewById(R.id.text_order_details_total_price   );

        Order o = OrderRegister.getOrder(oid);
        assert o != null;
        if(o.deliveryPrice() == 0)
            deliveryPriceLayout.setVisibility(View.GONE);
        else
            deliveryPriceText.setText(String.format(getString(R.string.currency), o.deliveryPrice()));
        dateText.setText(o.dueDate().toString());
        totalPriceText.setText(String.format(getString(R.string.total_price), o.totalPrice()));
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_details_order;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.options_edit_order)
        {
            Order o = OrderRegister.getOrder(oid);
            assert o != null;
            Customer c = CustomerRegister.getCustomer(o.cid());
            assert c != null;
            NavDirections action = OrderDetailsFragmentDirections
                    .actionOrderDetailsFragmentToOrderAddModifyFragment(oid,
                            String.format(getString(R.string.modify_order_label), c.name()));
            NavHostFragment.findNavController(this).navigate(action);
        }
        return true;
    }
}
