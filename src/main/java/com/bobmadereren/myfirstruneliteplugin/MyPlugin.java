package com.bobmadereren.myfirstruneliteplugin;

import com.bobmadereren.myfirstruneliteplugin.offer.Offer;
import com.bobmadereren.myfirstruneliteplugin.offer.OfferManager;
import com.bobmadereren.myfirstruneliteplugin.ui.MasterPanel;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
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
	public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged event)
	{
		Offer offer = offerManager.onGrandExchangeOfferChanged(event.getSlot(), event.getOffer());
		masterPanel.getProgressBarPanel().getProgressBar(event.getSlot()).update(offer, itemManager);
	}

	private ItemContainer firstItemContainer;

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		offerManager.onItemContainerChanged(event.getItemContainer());
		//masterPanel.getProgressBarPanel().getProgressBar(slot).update(offer, itemManager);
	}

	@Provides
	PluginConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PluginConfig.class);
	}
}
