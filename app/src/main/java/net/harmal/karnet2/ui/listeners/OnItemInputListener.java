package net.harmal.karnet2.ui.listeners;

import android.view.View;

import net.harmal.karnet2.utils.EventHandler;

public interface OnItemInputListener
{
    @EventHandler
    void onItemClick    (View v, int position);
    @EventHandler
    void onItemLongClick(View v, int position);

    class Builder
    {
        public interface OnItemClickListener
        {
            @EventHandler
            void onItemClick(View v, int position);
        }
        public interface OnItemLongClickListener
        {
            @EventHandler
            void onItemLongClick(View v, int position);
        }

        private final OnItemClickListener     cl ;
        private final OnItemLongClickListener lcl;

        public Builder(OnItemClickListener cl, OnItemLongClickListener lcl)
        {
            this.cl  = cl ;
            this.lcl = lcl;
        }
        public OnItemInputListener build()
        {
            return new OnItemInputListener()
            {
                @Override
                @EventHandler
                public void onItemClick(View v, int position)
                {
                    if(cl != null)
                        cl.onItemClick(v, position);
                }

                @Override
                @EventHandler
                public void onItemLongClick(View v, int position)
                {
                    if(lcl != null)
                        lcl.onItemLongClick(v, position);
                }

            };
        }
    }

}
