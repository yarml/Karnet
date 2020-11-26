package net.harmal.karnet2.core;

import net.harmal.karnet2.savefile.Savable;
import net.harmal.karnet2.savefile.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Product implements Savable
{
    private int          pid                    ;
    private short        unitPrice              ;
    private String       name                   ;

    private ProductCategory       baseIngredient;
    private ProductCategory       fat           ;
    private ProductCategory       shape         ;
    private ProductCategory       type          ;
    private List<ProductCategory> extra         ;

    public Product(int pid, int unitPrice, @NotNull String name,
                   @NotNull ProductCategory base, @NotNull ProductCategory fat, @NotNull ProductCategory shape,
                   @NotNull ProductCategory type, @NotNull List<ProductCategory> extra                        )
    {
        this.pid                = pid              ;
        this.unitPrice          = (short) unitPrice;
        this.name               = name             ;
        this.baseIngredient = base ;
        this.fat            = fat  ;
        this.shape          = shape;
        this.type           = type ;
        this.extra          = extra;
    }

    public int pid()
    {
        return pid;
    }

    public void pid(int pid)
    {
        this.pid = pid;
    }

    public short unitPrice()
    {
        return unitPrice;
    }

    public void unitPrice(short unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public String name()
    {
        return name;
    }

    public void name(String name)
    {
        this.name = name;
    }

    public ProductCategory baseIngredient() {

        return baseIngredient;
    }

    public void baseIngredient(ProductCategory baseIngredient)
    {
        this.baseIngredient = baseIngredient;
    }

    public ProductCategory fat()
    {
        return fat;
    }

    public void fat(ProductCategory fat)
    {
        this.fat = fat;
    }

    public ProductCategory shape()
    {
        return shape;
    }

    public void shape(ProductCategory shape)
    {
        this.shape = shape;
    }

    public ProductCategory type()
    {
        return type;
    }

    public void type(ProductCategory type)
    {
        this.type = type;
    }

    public List<ProductCategory> extra()
    {
        return extra;
    }

    public void extra(List<ProductCategory> extra)
    {
        this.extra = extra;
    }

    @Override
    public void writeData(@NotNull DataOutputStream stream) throws IOException
    {
        stream.writeInt(pid);
        stream.writeShort(unitPrice);
        stream.writeInt(extra.size());
        Utils.writeString(name, stream);
        baseIngredient.writeData(stream);
        fat.writeData(stream);
        shape.writeData(stream);
        type.writeData(stream);
        for(ProductCategory c : extra)
            c.writeData(stream);
    }

    public static class ProductBuilder implements BUILDER<Product>
    {
        @Override
        public Product readData(int version, @NotNull ByteBuffer buffer)
        {
            int pid = buffer.getInt();
            int unitPrice = buffer.getShort();
            int extraCount = buffer.getInt();
            String name = Utils.readString(buffer);
            ProductCategory.ProductCategoryBuilder builder = new ProductCategory
                    .ProductCategoryBuilder();
            ProductCategory base  = builder.readData(version, buffer);
            ProductCategory fat   = builder.readData(version, buffer);
            ProductCategory shape = builder.readData(version, buffer);
            ProductCategory type  = builder.readData(version, buffer);
            List<ProductCategory> extra = new ArrayList<>();
            for(int i = 0; i < extraCount; i++)
                extra.add(builder.readData(version, buffer));
            return new Product(pid, unitPrice, name, base, fat, shape, type, extra);
        }
    }
}
