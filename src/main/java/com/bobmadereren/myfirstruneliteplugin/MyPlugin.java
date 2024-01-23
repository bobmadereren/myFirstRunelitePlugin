package com.bobmadereren.myfirstruneliteplugin;

import com.bobmadereren.myfirstruneliteplugin.offer.Offer;
import com.bobmadereren.myfirstruneliteplugin.offer.OfferManager;
import com.bobmadereren.myfirstruneliteplugin.ui.MasterPanel;
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

	private OfferManager offerManager;

	@Override
	protected void startUp() throws Exception
	{
		offerManager = injector.getInstance(OfferManager.class);

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
		Offer offer = offerManager.onGrandExchangeOfferChanged(offerChangedEvent.getSlot(), offerChangedEvent.getOffer());
		masterPanel.getProgressBarPanel().getProgressBar(offerChangedEvent.getSlot()).update(offer, itemManager);
	}

	private ItemContainer firstItemContainer;

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged itemContainerChanged)
	{
		offerManager.onItemContainerChanged(itemContainerChanged.getItemContainer());
	}

	private String containerString(ItemContainer itemContainer)
	{
		String cell = "%-30s";
		String tab = "    ";
		StringBuilder result = new StringBuilder();

		result.append(String.format("Container ID: %d, Space: %,d / %,d%n" +
						tab + cell + cell + "%n" +
                        tab + cell + cell + "%n",
				itemContainer.getId(), itemContainer.count(), itemContainer.size(),
                "Name", "Quantity",
                "----", "--------"
        ));

		for(Item item : itemContainer.getItems())
			result.append(String.format(tab + cell + cell + "%n", itemManager.getItemComposition(item.getId()).getName(), item.getQuantity()));

		return result.toString();
	}

	private String grandExchangeString()
	{
		GrandExchangeOffer[] offers = client.getGrandExchangeOffers();

		String cell = "%-30s";
		String intCell = "%,-30d";
		String fractionCell = "%,d / %,-30d";
		String tab = "    ";
		StringBuilder result = new StringBuilder();

		result.append(String.format("Grand Exchange offers%n" +
						tab + cell.repeat(5) + "%n" +
						tab + cell.repeat(5) + "%n",
				"Slot", "Name", "State", "Price", "Quantity",
				"----", "----", "-----", "-----", "--------"
		));

		for(int i = 0; i < 8; i++) {
			GrandExchangeOffer offer = offers[i];
			result.append(String.format(tab + intCell + cell + cell + fractionCell + fractionCell + "%n",
					i,
					itemManager.getItemComposition(offer.getItemId()).getName(),
					offer.getState(),
					offer.getSpent(),
					offer.getPrice() * offer.getTotalQuantity(),
					offer.getQuantitySold(),
					offer.getTotalQuantity()
			));
		}
		return result.toString();
	}

	@Provides
	PluginConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PluginConfig.class);
	}
}
