package com.bobmadereren.myfirstruneliteplugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MyPluginTest
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("----------\nTest\n----------");
		ExternalPluginManager.loadBuiltin(MyPlugin.class);
		RuneLite.main(args);
	}
}