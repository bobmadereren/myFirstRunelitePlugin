package com.bobmadereren.myfirstruneliteplugin;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Plugin2",
        description = "A 2nd plugin for testing their interconnection"
)
public class Plugin2 extends net.runelite.client.plugins.Plugin
{
    @Inject
    private Client client;

    private int tick = 0;

    @Override
    protected void startUp() throws Exception
    {
        log.info("Plugin2 started!");
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("Plugin2 stopped!");
    }

    @Subscribe
    public void onGameTick(GameTick gameTick)
    {
        String message = "Tick: " + tick++;
        //client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
    }
}
