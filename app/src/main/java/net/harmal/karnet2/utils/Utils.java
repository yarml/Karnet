package net.harmal.karnet2.utils;

import net.harmal.karnet2.core.ContactData;

import org.jetbrains.annotations.NotNull;

public class Utils
{

    @NotNull
    public static String extractCustomerName(@NotNull ContactData contact)
    {
        String name = contact.name;
        name = name.replaceAll("Cliente", "");
        name = name.replaceAll("cliente", "");
        name = name.replaceAll("Client", "");
        name = name.replaceAll("client", "");
        name = name.trim();
        return name;
    }

    @NotNull
    public static String extractCustomerNum(@NotNull ContactData contact)
    {
        String num = contact.num;
        num = num.replaceAll("\\(", "");
        num = num.replaceAll("\\)", "");
        num = num.replaceAll("-", "");
        num = num.replaceAll(" ", "");
        num = num.replace("+212", "0");
        return num;
    }

}
