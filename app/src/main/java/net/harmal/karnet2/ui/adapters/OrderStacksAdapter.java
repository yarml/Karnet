package net.harmal.karnet2.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.Stack;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import java.util.List;

public class OrderStacksAdapter extends KarnetRecyclerAdapter<OrderStacksAdapter.OrderStacksViewHolder>
{
    public static class OrderStacksViewHolder extends KarnetRecyclerViewHolder
    {
        private final TextView    stackItemName ;
        private final TextView    stackItemCount;
        private final ImageButton stackDeleteBtn;
        public OrderStacksViewHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);

            stackItemName  = itemView.findViewById(R.id.text_order_stack_item_name );
            stackItemCount = itemView.findViewById(R.id.text_order_stack_item_count);
            stackDeleteBtn = itemView.findViewById(R.id.btn_order_stack_delete     );
        }
    }

    @NonNull
    private final List<Stack> stackList   ;
    private final boolean     showDelete  ;
    private final String      itemSubtitle;

    public OrderStacksAdapter(@NonNull List<Stack> stackList)
    {
        this(stackList, true, "%d");
    }

    public OrderStacksAdapter(@NonNull List<Stack> stackList, boolean showDelete, String itemSubtitle)
    {
        this.stackList    = stackList   ;
        this.showDelete   = showDelete  ;
        this.itemSubtitle = itemSubtitle;
    }

    @NonNull
    public List<Stack> stackList()
    {
        return stackList;
    }

    @NonNull
    @Override
    public OrderStacksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_stack, parent, false);
        OrderStacksViewHolder vh = new OrderStacksViewHolder(v, onItemInputListener);
        if(showDelete)
            vh.stackDeleteBtn.setVisibility(View.VISIBLE);
        else
            vh.stackDeleteBtn.setVisibility(View.GONE);
        return vh;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull OrderStacksViewHolder holder, int position)
    {
        Stack current = stackList.get(position);
        Product p = ProductRegister.getProduct(current.pid());
        assert p != null;
        holder.stackItemName.setText(p.name());
        holder.stackItemCount.setText(String.format(itemSubtitle, current.count(), p.unitPrice()));
    }

    @Override
    public int getItemCount()
    {
        return stackList.size();
    }
}
