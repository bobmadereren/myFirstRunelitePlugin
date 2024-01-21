package com.bobmadereren.myfirstruneliteplugin;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.event.ActionListener;

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

	@Inject
	private ItemManager itemManager;

	private boolean buttonClicked = false;

	protected ActionListener setButtonClicked = e -> buttonClicked = true;

	@Override
	protected void startUp() throws Exception
	{
		log.info("My first plugin started!");

		MasterPanel masterPanel = injector.getInstance(MasterPanel.class);

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
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "My first plugin says " + config.greeting(), null);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick){
		if(buttonClicked){
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "",  "Button was clicked", null);
			buttonClicked = false;
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged itemContainerChanged){
		ItemContainer itemContainer = itemContainerChanged.getItemContainer();
		System.out.printf("%nContainer modified - Container ID: %d, Space: %,d / %,d%n" +
				"    %-20s%-20s%n" +
				"    %-20s%-20s%n",
				itemContainer.getId(), itemContainer.count(), itemContainer.size(),
				"Name", "Quantity",
				"----", "--------"
		);
		for(Item item : itemContainer.getItems())
			System.out.printf("    %-20s%,-20d%n", itemManager.getItemComposition(item.getId()).getName(), item.getQuantity());

		GrandExchangeOffer[] offers = client.getGrandExchangeOffers();
		System.out.printf("Grand Exchange offers%n" +
				"    %-20s%-20s%-20s%-20s%-20s%n" +
				"    %-20s%-20s%-20s%-20s%-20s%n",
				"Slot", "Name", "State", "Price", "Quantity",
				"----", "----", "-----", "-----", "--------"
		);
		for(int i = 0; i < 8; i++) {
			GrandExchangeOffer offer = offers[i];
			System.out.printf("    %-20d%-20s%-20s%,d / %,-20d%,d / %,-20d%n",
					i,
					itemManager.getItemComposition(offer.getItemId()).getName(),
					offer.getState(),
					offer.getSpent(),
					offer.getPrice() * offer.getTotalQuantity(),
					offer.getQuantitySold(),
					offer.getTotalQuantity()
			);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked){
		//System.out.printf("%nAction%n    %s%n", menuOptionClicked.getMenuAction());
	}

	@Subscribe
	public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged offerChangedEvent) {
		/*GrandExchangeOffer offer = offerChangedEvent.getOffer();
		if(offer.getState() == GrandExchangeOfferState.EMPTY) return;
		System.out.printf("%nGrand Exchange offer change:%n" +
						"    Slot: %d, Name: %s, State: %s, Price: %,d / %,d, Quantity: %,d / %,d%n",
				offerChangedEvent.getSlot(),
				itemManager.getItemComposition(offer.getItemId()).getName(),
				offer.getState(),
				offer.getSpent(),
				offer.getPrice() * offer.getTotalQuantity(),
				offer.getQuantitySold(),
				offer.getTotalQuantity()
		);*/
	}

	@Provides
	PluginConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PluginConfig.class);
	}
}
