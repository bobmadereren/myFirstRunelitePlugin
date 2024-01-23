package com.bobmadereren.myfirstruneliteplugin.offer;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.util.Arrays;

public class OfferManager {

    private final Offer[] activeOffers = new Offer[8];

    private final int[] collectionBoxItems = new int[8];

    private final int[] collectionBoxCoins = new int[8];

    private static final int[] COLLECT_BOX_CONTAINER_ID = new int[]{518, 519, 520, 521, 522, 523, 539, 540};

    private final ItemManager itemManager;

    @Inject
    public OfferManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged offerChangedEvent){
        if(offerChangedEvent.getOffer().getState() == GrandExchangeOfferState.EMPTY) return;

        int slot = offerChangedEvent.getSlot();
        GrandExchangeOffer offer = offerChangedEvent.getOffer();

        if(activeOffers[slot] == null) {
            activeOffers[slot] = newOffer(offer);
            System.out.println("Creating new offer - " + activeOffers[slot]);
        }
        else {
            activeOffers[slot].setProgressValue(offer.getQuantitySold());
            System.out.println("Offer progress - " + activeOffers[slot]);
        }
    }

    public void onItemContainerChanged(ItemContainerChanged itemContainerChanged)
    {
        ItemContainer newItemContainer = itemContainerChanged.getItemContainer();
        int slot = Arrays.binarySearch(COLLECT_BOX_CONTAINER_ID, newItemContainer.getId());
        if(slot < 0) return;

        Offer offer = activeOffers[slot];
        int itemId = offer.getItemId();
        int coinsId = 995;

        if(!newItemContainer.contains(itemId))
            offer.collectItems(collectionBoxItems[slot]);

        if(!newItemContainer.contains(coinsId))
            offer.collectCoins(collectionBoxCoins[slot]);

        System.out.println("Container change - " + activeOffers[slot]);

        if(newItemContainer.count() == 0)
            activeOffers[slot] = null;

        collectionBoxItems[slot] = newItemContainer.count(itemId);
        collectionBoxCoins[slot] = newItemContainer.count(coinsId);
    }

    private Offer newOffer(GrandExchangeOffer offer) {
        switch (offer.getState()) {
            case BUYING: return new BuyOffer(itemManager.getItemComposition(offer.getItemId()), offer.getPrice(), offer.getTotalQuantity());
            case SELLING: return new SellOffer(itemManager.getItemComposition(offer.getItemId()), offer.getPrice(), offer.getTotalQuantity());
            default: return null;
        }
    }

}
