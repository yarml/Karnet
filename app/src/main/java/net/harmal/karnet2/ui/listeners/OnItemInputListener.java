package net.harmal.karnet2.ui.listeners;

import android.view.View;

public interface OnItemInputListener
{
    void onItemClick    (View v, int position);
    void onItemLongClick(View v, int position);

    class Builder
    {
        public interface OnItemClickListener
        {
            void onItemClick(View v, int position);
        }
        public interface OnItemLongClickListener
        {
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
                public void onItemClick(View v, int position)
                {
                    if(cl != null)
                        cl.onItemClick(v, position);
                }

                @Override
                public void onItemLongClick(View v, int position)
                {
                    if(lcl != null)
                        lcl.onItemLongClick(v, position);
                }

            };
        }
    }

}
