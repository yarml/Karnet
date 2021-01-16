package net.harmal.karnet2.core;

public class ContactData
{
    public String name;
    public String num ;

    public ContactData(String name, String num)
    {
        this.name = name;
        this.num  = num ;
    }
    public ContactData()
    {
        this.name = "";
        this.num  = "";
    }

    public boolean isCustomer()
    {
        return name.toLowerCase().startsWith("client");
    }

    @Override
    public String toString() {
        return "ContactData{" +
                "name='" + name + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
