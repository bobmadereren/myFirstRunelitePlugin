package com.bobmadereren.myfirstruneliteplugin;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
		name = "My first plugin!",
		description = "This is my first plugin :)"
)
public class MyPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private PluginConfig config;

	private boolean buttonClicked = false;

	protected void clickButton() { buttonClicked = true; }

	@Override
	protected void startUp() throws Exception
	{
		log.info("My first plugin started!");

		MasterPanel masterPanel = injector.getInstance(MasterPanel.class);
		//MasterPanel masterPanel = new MasterPanel(client, config);

		NavigationButton navButton = NavigationButton.builder()
				.tooltip("My first plugin!")
				.icon(ImageUtil.loadImageResource(getClass(), "/icon.png"))
				.priority(0)
				.panel(masterPanel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("My first plugin stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "My first plugin says " + config.greeting(), null);
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick){
		if(buttonClicked){
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "My first plugin says " + config.greeting(), null);
			buttonClicked = false;
		}
	}

	@Subscribe
	public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged offerChangedEvent) {
		//printGrandExchangeOffer(offerChangedEvent);
	}

	private void printGrandExchangeOffer(GrandExchangeOfferChanged offerChangedEvent){
		System.out.println("Slot: " + offerChangedEvent.getSlot());
		System.out.println("State: " + offerChangedEvent.getOffer().getState());
		System.out.println("Item ID: " + offerChangedEvent.getOffer().getItemId());
		System.out.println("Price: " + offerChangedEvent.getOffer().getPrice() + " each, " + offerChangedEvent.getOffer().getSpent() + " total");
		System.out.println("Quantity: " + offerChangedEvent.getOffer().getQuantitySold() + " / " + offerChangedEvent.getOffer().getTotalQuantity());
		System.out.println("--------------------");
	}

	@Provides
	PluginConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PluginConfig.class);
	}
}
