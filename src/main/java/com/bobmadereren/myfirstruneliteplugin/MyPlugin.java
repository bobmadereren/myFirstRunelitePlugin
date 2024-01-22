package com.bobmadereren.myfirstruneliteplugin;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
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

	@Inject
	private ItemManager itemManager;

	private MasterPanel masterPanel;

	@Override
	protected void startUp() throws Exception
	{
		log.info("My first plugin started!");

		masterPanel = injector.getInstance(MasterPanel.class);

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
	public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged offerChangedEvent)
	{
		masterPanel.getProgressBarPanel()
				.getProgressBar(offerChangedEvent.getSlot())
				.update(offerChangedEvent.getOffer(), itemManager);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged itemContainerChanged)
	{
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

	@Provides
	PluginConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PluginConfig.class);
	}
}
