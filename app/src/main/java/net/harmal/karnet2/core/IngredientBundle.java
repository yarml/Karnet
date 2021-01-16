package net.harmal.karnet2.core;

import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.savefile.Savable;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IngredientBundle implements Savable
{

    private int           basePiid  ;
    private int           fatPiid   ;
    private int           shapePiid ;
    private int           tastePiid ;
    private List<Integer> extrasPiid;

    public IngredientBundle(int basePiid, int fatPiid,
                            int shapePiid, int tastePiid,
                            @NotNull List<Integer> extras)
    {
        this.basePiid   = basePiid ;
        this.fatPiid    = fatPiid  ;
        this.shapePiid  = shapePiid;
        this.tastePiid  = tastePiid;
        this.extrasPiid = extras   ;
    }
    
    public int basePiid()
    {
        return basePiid;
    }

    public void basePiid(int basePiid)
    {
        this.basePiid = basePiid;
    }

    public int fatPiid()
    {
        return fatPiid;
    }

    public void fatPiid(int fatPiid)
    {
        this.fatPiid = fatPiid;
    }

    public int shapePiid()
    {
        return shapePiid;
    }

    public void shapePiid(int shapePiid)
    {
        this.shapePiid = shapePiid;
    }

    public int tastePiid()
    {
        return tastePiid;
    }

    public void tastePiid(int tastePiid)
    {
        this.tastePiid = tastePiid;
    }

    public List<Integer> extrasPiid()
    {
        return extrasPiid;
    }

    public void extrasPiid(List<Integer> extrasPiid)
    {
        this.extrasPiid = extrasPiid;
    }

    public boolean contains(@NotNull ProductIngredient ingredient)
    {
        return contains(ingredient.piid());
    }

    public boolean contains(int piid)
    {
        return basePiid  == piid
        || fatPiid   == piid
        || shapePiid == piid
        || tastePiid == piid
        || extrasPiid.contains(piid);
    }

    public int price()
    {
        int price = 0;

        ProductIngredient baseI  = IngredientRegister.getIngredient(basePiid );
        ProductIngredient fatI   = IngredientRegister.getIngredient(fatPiid  );
        ProductIngredient shapeI = IngredientRegister.getIngredient(shapePiid);
        ProductIngredient tasteI = IngredientRegister.getIngredient(tastePiid);

        assert baseI != null && fatI != null && shapeI != null && tasteI != null;

        price += baseI.price( );
        price += fatI.price(  );
        price += shapeI.price();
        price += tasteI.price();

        for(int i : extrasPiid)
        {
            ProductIngredient extra = IngredientRegister.getIngredient(i);
            assert extra != null;
            price += extra.price();
        }
        return price;
    }

    public String name()
    {
        ProductIngredient baseI  = IngredientRegister.getIngredient(basePiid );
        ProductIngredient fatI   = IngredientRegister.getIngredient(fatPiid  );
        ProductIngredient shapeI = IngredientRegister.getIngredient(shapePiid);
        ProductIngredient tasteI = IngredientRegister.getIngredient(tastePiid);

        assert baseI != null && fatI != null && shapeI != null && tasteI != null;

        String base  = baseI.displayName( );
        String fat   = fatI.displayName(  );
        String shape = shapeI.displayName();
        String taste = tasteI.displayName();
        
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(String.format("%s %s %s %s", shape, taste, base, fat));

        if(extrasPiid.size() > 0)
        {
            nameBuilder.append("(");
            for(int extra : extrasPiid)
            {
                ProductIngredient extraI = IngredientRegister.getIngredient(extra);
                assert extraI != null;
                String extraName = extraI.displayName();
                nameBuilder.append("+").append(extraName);
            }
            nameBuilder.append(")");
        }
        return nameBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientBundle that = (IngredientBundle) o;
        return basePiid == that.basePiid &&
                fatPiid == that.fatPiid &&
                shapePiid == that.shapePiid &&
                tastePiid == that.tastePiid &&
                extrasPiid.equals(that.extrasPiid);
    }

    @Override
    public void writeData(@NotNull DataOutputStream stream) throws IOException
    {
        stream.writeInt(basePiid);
        stream.writeInt(fatPiid);
        stream.writeInt(shapePiid);
        stream.writeInt(tastePiid);
        stream.writeInt(extrasPiid.size());
        for(int extra : extrasPiid)
            stream.writeInt(extra);
    }

    public static class IngredientBundleBuilder implements Savable.BUILDER<IngredientBundle>
    {

        @Override
        public IngredientBundle readData(int version, ByteBuffer buffer)
        {
            int base = buffer.getInt();
            int fat = buffer.getInt();
            int shape = buffer.getInt();
            int taste = buffer.getInt();
            int extraCount = buffer.getInt();
            List<Integer> extras = new ArrayList<>();
            for(int i = 0 ; i < extraCount; i++)
                extras.add(buffer.getInt());
            return new IngredientBundle(base, fat, shape, taste, extras);
        }
    }
}
