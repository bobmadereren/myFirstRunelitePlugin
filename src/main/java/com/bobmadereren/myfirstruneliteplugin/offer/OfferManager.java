package com.bobmadereren.myfirstruneliteplugin.offer;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.function.Supplier;

public class OfferManager {

    private final Offer[] activeOffers = new Offer[8];

    private final int[] collectionBoxItems = new int[8];

    private final int[] collectionBoxCoins = new int[8];

    private final ItemManager itemManager;

    private static final int[] COLLECT_BOX_CONTAINER_ID = new int[]{518, 519, 520, 521, 522, 523, 539, 540};

    @Inject
    public OfferManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * Only handles {@link net.runelite.api.GrandExchangeOfferState#SELLING selling}
     * and {@link net.runelite.api.GrandExchangeOfferState#BUYING buying} offers.
     * Updates the offer if it is being tracked.
     * Starts tracking an offer if it is not being tracked, and it's
     * {@link GrandExchangeOffer#getQuantitySold() progress} is 0.
     * @param slot Slot of "changed" offer.
     * @param offer "Changed" offer.
     * @return The tracked offer change or null.
     */
    public OfferChange onGrandExchangeOfferChanged(int slot, GrandExchangeOffer offer) {

        Supplier<Offer> newOffer;

        switch (offer.getState()){
            case BUYING: newOffer = () -> new BuyOffer(itemManager.getItemComposition(offer.getItemId()), offer.getPrice(), offer.getTotalQuantity()); break;
            case SELLING: newOffer = () -> new SellOffer(itemManager.getItemComposition(offer.getItemId()), offer.getPrice(), offer.getTotalQuantity()); break;
            default: return null;
        }

        if (activeOffers[slot] != null) {
            activeOffers[slot].setProgressSold(offer.getQuantitySold());
            return new OfferChange(activeOffers[slot], slot, OfferChangeState.SOLD);
        }
        else if (offer.getQuantitySold() == 0) {
            activeOffers[slot] = newOffer.get();
            return new OfferChange(activeOffers[slot], slot, OfferChangeState.CREATED);
        }

        return null;
    }

    public OfferChange onItemContainerChanged(ItemContainerChanged event){
        int slot = Arrays.binarySearch(COLLECT_BOX_CONTAINER_ID, event.getContainerId());

        if(slot < 0) return null;

        ItemContainer itemContainer = event.getItemContainer();

        Offer offer = activeOffers[slot];
        int itemId = offer.getItem().getId();
        int coinsId = 995;

        if(!itemContainer.contains(itemId))
            offer.collectItems(collectionBoxItems[slot]);

        if(!itemContainer.contains(coinsId))
            offer.collectCoins(collectionBoxCoins[slot]);

        if(itemContainer.count() == 0 && offer.getProgressCollected() == offer.getProgressTotal()) {
            activeOffers[slot] = null;
            return new OfferChange(activeOffers[slot], slot, OfferChangeState.COMPLETE);
        }

        collectionBoxItems[slot] = itemContainer.count(itemId);
        collectionBoxCoins[slot] = itemContainer.count(coinsId);

        return new OfferChange(activeOffers[slot], slot, OfferChangeState.SOLD);
    }

}
