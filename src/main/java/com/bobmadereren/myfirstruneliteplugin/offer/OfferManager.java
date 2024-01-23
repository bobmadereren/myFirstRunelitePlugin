package com.bobmadereren.myfirstruneliteplugin.offer;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemContainer;
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

    public Offer onGrandExchangeOfferChanged(int slot, GrandExchangeOffer offer){
        if(offer.getState() == GrandExchangeOfferState.EMPTY)
            return null;

        if(activeOffers[slot] == null)
            activeOffers[slot] = newOffer(offer);
        else
            activeOffers[slot].setProgressSold(offer.getQuantitySold());

        return activeOffers[slot];
    }

    public void onItemContainerChanged(ItemContainer itemContainer)
    {
        int slot = Arrays.binarySearch(COLLECT_BOX_CONTAINER_ID, itemContainer.getId());

        if(slot < 0) return;

        Offer offer = activeOffers[slot];
        int itemId = offer.getItem().getId();
        int coinsId = 995;

        if(!itemContainer.contains(itemId))
            offer.collectItems(collectionBoxItems[slot]);

        if(!itemContainer.contains(coinsId))
            offer.collectCoins(collectionBoxCoins[slot]);

        if(itemContainer.count() == 0)
            activeOffers[slot] = null;

        collectionBoxItems[slot] = itemContainer.count(itemId);
        collectionBoxCoins[slot] = itemContainer.count(coinsId);
    }

    private Offer newOffer(GrandExchangeOffer offer) {
        switch (offer.getState()) {
            case BUYING: return new BuyOffer(itemManager.getItemComposition(offer.getItemId()), offer.getPrice(), offer.getTotalQuantity());
            case SELLING: return new SellOffer(itemManager.getItemComposition(offer.getItemId()), offer.getPrice(), offer.getTotalQuantity());
            default: return null;
        }
    }

}
