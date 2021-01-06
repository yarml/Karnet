package net.harmal.karnet2.core;

import net.harmal.karnet2.savefile.Savable;
import net.harmal.karnet2.savefile.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ProductIngredient implements Savable
{

    public enum Type
    {
        BASE("Ingrédient de base"), FAT("Matière grasse"),
        SHAPE("Forme"), TASTE("Goût"), EXTRA("Additif");

        final String name;

        Type(String name)
        {
            this.name = name;
        }

        public String displayName()
        {
            return name;
        }

        @Override
        public String toString() {
            return displayName();
        }
    }

    private int    piid       ;
    private int    price      ;
    @NotNull
    private String displayName;
    @NotNull
    private Type   type       ;

    public ProductIngredient(int piid, int price, @NotNull String displayName, @NotNull Type type)
    {
        this.piid        = piid       ;
        this.price       = price      ;
        this.displayName = displayName;
        this.type        = type       ;
    }

    public int piid()
    {
        return piid;
    }

    public void piid(int piid)
    {
        this.piid = piid;
    }

    public int price() {
        return price;
    }

    public void price(int price)
    {
        this.price = price;
    }

    @NotNull
    public String displayName()
    {
        return displayName;
    }

    public void displayName(@NotNull String displayName)
    {
        this.displayName = displayName;
    }

    @NotNull
    public Type type()
    {
        return type;
    }

    public void type(@NotNull Type type)
    {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        if(!(o instanceof ProductIngredient))
            return false;
        return piid == ((ProductIngredient) o).piid;
    }

    @Override
    public void writeData(@NotNull DataOutputStream stream) throws IOException
    {
        stream.writeInt(piid);
        stream.writeShort(price);
        stream.writeByte(type.ordinal());
        Utils.writeString(displayName, stream);
    }

    public static class ProductIngredientBuilder implements Savable.BUILDER<ProductIngredient>
    {
        @Override
        public ProductIngredient readData(int version, ByteBuffer buffer)
        {
            int piid = buffer.getInt();
            int price = buffer.getShort();
            Type type = Type.values()[buffer.get()];
            String displayName = Utils.readString(buffer);
            return new ProductIngredient(piid, price, displayName, type);
        }
    }
}
