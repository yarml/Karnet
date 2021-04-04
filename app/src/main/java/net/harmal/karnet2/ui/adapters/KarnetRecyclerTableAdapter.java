package net.harmal.karnet2.ui.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/* Parameters
 * CH: Column header View Holder Class
 * RH: Row header view holder class
 * CL: Cell view holder class
 * C : Corner view holder class
 */
public abstract class KarnetRecyclerTableAdapter
        <CH extends KarnetRecyclerViewHolder,
         RH extends KarnetRecyclerViewHolder,
         CL extends KarnetRecyclerViewHolder,
         C  extends KarnetRecyclerViewHolder>
extends KarnetRecyclerAdapter<KarnetRecyclerViewHolder>
{
    private static final int TYPE_CORNER        = 0;
    private static final int TYPE_COLUMN_HEADER = 1;
    private static final int TYPE_ROW_HEADER    = 2;
    private static final int TYPE_CELL          = 3;

    private final int columns;
    private final int rows   ;

    public KarnetRecyclerTableAdapter(int columns, int rows)
    {
        this.columns = columns;
        this.rows    = rows   ;
    }

    @NotNull
    @Override
    public KarnetRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
        case TYPE_CORNER:
            return onCreateCornerViewHolder(parent);
        case TYPE_COLUMN_HEADER:
            return onCreateColumnHeaderViewHolder(parent);
        case TYPE_ROW_HEADER:
            return onCreateRowHeaderViewHolder(parent);
        case TYPE_CELL:
            return onCreateCellViewHolder(parent);
        default:
            throw new IllegalStateException("viewType can't be: " + viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull KarnetRecyclerViewHolder holder, int position)
    {
        switch(getItemViewType(position))
        {
        case TYPE_CORNER:
            onBindCornerViewHolder((C) holder);
            break;
        case TYPE_COLUMN_HEADER:
            onBindColumnHeaderViewHolder((CH) holder, positionColumn(position));
            break;
        case TYPE_ROW_HEADER:
            onBindRowHeaderViewHolder((RH) holder, positionRow(position));
            break;
        case TYPE_CELL:
            onBindCellViewHolder((CL) holder, positionColumn(position), positionRow(position));
            break;
        }
    }

    @Override
    public int getItemCount()
    {
        return rows * columns;
    }

    @Override
    public int getItemViewType(int position)
    {
        if(position == 0)
            return TYPE_CORNER;
        if(positionRow(position) == 0)
            return TYPE_COLUMN_HEADER;
        if(positionColumn(position) == 0)
            return TYPE_ROW_HEADER;
        return TYPE_CELL;
    }

    public int positionColumn(int position)
    {
        return (int)(position % columns);
    }
    public int positionRow(int position)
    {
        return (int)(position / columns);
    }


    public abstract CH onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent);
    public abstract RH onCreateRowHeaderViewHolder(@NonNull ViewGroup parent   );
    public abstract CL onCreateCellViewHolder(@NonNull ViewGroup parent        );
    public abstract C  onCreateCornerViewHolder(@NonNull ViewGroup parent      );

    public abstract void onBindColumnHeaderViewHolder(@NonNull CH holder, int column         );
    public abstract void onBindRowHeaderViewHolder(@NonNull    RH holder, int row            );
    public abstract void onBindCellViewHolder(@NonNull         CL holder, int column, int row);
    public abstract void onBindCornerViewHolder(@NonNull       C  holder                     );
}
