package com.myfirstruneliteplugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PluginTest
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("----------\nTest\n----------");
		ExternalPluginManager.loadBuiltin(Plugin.class);
		RuneLite.main(args);
	}
}