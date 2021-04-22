package net.harmal.karnet2.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends KarnetRecyclerAdapter<OrderListAdapter.OrderViewHolder>
{

    public enum ViewMode
    {
        ALL, DELIVERY, NO_DELIVERY
    }
    public static class OrderViewHolder extends KarnetRecyclerViewHolder
    {
        CardView    mainCard    ;
        TextView    priceView   ;
        TextView    nameView    ;
        TextView    dateView    ;
        ImageButton deleteBtn   ;
        ImageButton doneBtn     ;
        public OrderViewHolder(@NotNull View itemView, KarnetRecyclerAdapter<? extends KarnetRecyclerViewHolder> adapter)
        {
            super(itemView, adapter);
            mainCard  = itemView.findViewById(R.id.card_order_item     );
            priceView = itemView.findViewById(R.id.text_order_total    );
            nameView  = itemView.findViewById(R.id.text_order_item_name);
            dateView  = itemView.findViewById(R.id.text_order_item_date);
            deleteBtn = itemView.findViewById(R.id.btn_order_delete    );
            doneBtn   = itemView.findViewById(R.id.btn_order_done      );
        }
    }
    private final List<Order> orderList       ;
    private List<Order>       visibleOrderList;

    private ViewMode     viewMode    ;
    private List<String> filterCities;
    private String       filterText  ;

    public OrderListAdapter(@NotNull List<Order> orderList)
    {
        this.orderList   = orderList        ;
        visibleOrderList = new ArrayList<>();
        visibleOrderList.addAll(  orderList);
        viewMode = ViewMode.ALL;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order, parent, false);
        return new OrderViewHolder(v, this);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position)
    {
        Order current = visibleOrderList.get(position);
        Customer customer = CustomerRegister.getCustomer(current.cid());
        assert customer != null;
        holder.nameView.setText(String.format(holder.nameView.getResources()
                        .getString(R.string.order_customer_info),
                        customer.cid(), customer.name()));
        holder.priceView.setText(String.format(holder.priceView.getResources()
                        .getString(R.string.currency),
                        current.totalPrice() + current.deliveryPrice()));
        holder.dateView.setText(String.format(holder.dateView.getResources()
                .getString(R.string.text_for_date), current.dueDate().toString()));
        holder.deleteBtn.setVisibility(View.GONE);
        holder.doneBtn.setVisibility(View.GONE);

        int canValidate = Stock.canValidate(current);
        if(canValidate == Stock.VALIDATE_IMPOSSIBLE)
            holder.mainCard.setCardBackgroundColor(holder.mainCard.getResources()
                    .getColor(android.R.color.holo_red_light, null));
        else if(canValidate == Stock.VALIDATE_POSSIBLE)
            holder.mainCard.setCardBackgroundColor(holder.mainCard.getResources()
                    .getColor(android.R.color.white, null));
        else if(canValidate == Stock.VALIDATE_WARN)
            holder.mainCard.setCardBackgroundColor(holder.mainCard.getResources()
                    .getColor(android.R.color.holo_orange_light, null));

    }

    @Override
    public int getItemCount() {
        return visibleOrderList.size();
    }

    public List<Order> orderList()
    {
        return orderList;
    }
    public List<Order> visibleOrderList()
    {
        return visibleOrderList;
    }

    public void filterCities(List<String> cities)
    {
        filterCities = cities;
        update();
    }

    public void filterText(String text)
    {
        filterText = text;
        update();
    }

    @SuppressLint("DefaultLocale")
    public void update()
    {
        visibleOrderList = new ArrayList<>();
        for(Order o : orderList)
        {
            boolean viewModeFilter = false;
            boolean cityFilter     = false;
            boolean textFilter     = false;
            if ((viewMode == ViewMode.DELIVERY    && o.deliveryPrice() != 0)
             || (viewMode == ViewMode.NO_DELIVERY && o.deliveryPrice() == 0)
             || (viewMode == ViewMode.ALL))
                viewModeFilter = true;

            if(filterCities != null)
            {
                Customer c = CustomerRegister.getCustomer(o.cid());
                assert c != null;
                for (String city : filterCities)
                    if (c.city().equalsIgnoreCase(city))
                    {
                        cityFilter = true;
                        break;
                    }
            }
            else
                cityFilter = true;
            if(filterText != null)
            {
                Customer c = CustomerRegister.getCustomer(o.cid());
                assert c != null;
                if(c.name().toLowerCase().contains(filterText.toLowerCase()) ||
                   c.city().toLowerCase().contains(filterText.toLowerCase()) ||
                   o.dueDate().toString().toLowerCase().contains(filterText.toLowerCase()) ||
                   String.format("%d", c.cid()).contains(filterText))
                    textFilter = true;
            }
            else
                textFilter = true;
            if(viewModeFilter && cityFilter && textFilter)
                visibleOrderList.add(o);
        }
        notifyDataSetChanged();
    }
    public ViewMode viewMode()
    {
        return viewMode;
    }

    public void viewMode(ViewMode viewMode)
    {
        this.viewMode = viewMode;
        update();
    }
}
