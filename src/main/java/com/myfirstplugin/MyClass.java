package com.myfirstplugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MyClass
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("----------\nMyClass\n----------");
        ExternalPluginManager.loadBuiltin(MyFirstPlugin.class);
        RuneLite.main(args);
    }
}