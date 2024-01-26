package com.bobmadereren.myfirstruneliteplugin.offer;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Stack;

public class OfferManager {

    private final Offer[] activeOffers = new Offer[8];

    private Stack<Offer> archivedOffers = new Stack<Offer>();

    private final int[] collectionBoxItems = new int[8];

    private final int[] collectionBoxCoins = new int[8];

    private final ItemManager itemManager;

    private static final int[] COLLECT_BOX_CONTAINER_ID = new int[]{518, 519, 520, 521, 522, 523, 539, 540};

    @Inject
    public OfferManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    /**
     * Updates the offer if it is being tracked.
     * Starts tracking an offer if it is not being tracked, and it's
     * {@link GrandExchangeOffer#getQuantitySold() progress} is 0.
     * @param slot Slot of "changed" offer.
     * @param runeliteOffer "Changed" offer.
     * @return The tracked offer change or null.
     */
    public OfferChanges onGrandExchangeOfferChanged(int slot, GrandExchangeOffer runeliteOffer) {

        if(activeOffers[slot] != null)
            return updateOffer(slot, activeOffers[slot], runeliteOffer);
        else if(runeliteOffer.getQuantitySold() == 0)
            return createNewOffer(slot, runeliteOffer);
        else
            return new OfferChanges(slot, activeOffers[slot]);

    }

    private OfferChanges updateOffer(int slot, Offer offer, GrandExchangeOffer runeliteOffer){
        OfferChanges offerChanges = new OfferChanges(slot, offer);

        switch (runeliteOffer.getState()) {
            case BUYING:
            case SELLING:
                int progressValue = offer.getProgressValue();
                int newProgressValue = runeliteOffer.getQuantitySold();
                if(newProgressValue > progressValue) {
                    offer.progress(newProgressValue - progressValue);
                    offerChanges.push(offer.isSoldOut() ? OfferChangeState.SOLD_OUT : OfferChangeState.SOLD_PARTIALLY);
                }
                break;
            case CANCELLED_BUY:
            case CANCELLED_SELL:
                if(!offer.isSoldOut()){
                    offer.cancel();
                    offerChanges.push(OfferChangeState.CANCELLED);
                }
                break;
        }

        return offerChanges;
    }

    private OfferChanges createNewOffer(int slot, GrandExchangeOffer runeliteOffer){
        switch (runeliteOffer.getState()) {
            case BUYING:
                activeOffers[slot] = new BuyOffer(itemManager.getItemComposition(runeliteOffer.getItemId()), runeliteOffer.getPrice(), runeliteOffer.getTotalQuantity());
                break;
            case SELLING:
                activeOffers[slot] = new SellOffer(itemManager.getItemComposition(runeliteOffer.getItemId()), runeliteOffer.getPrice(), runeliteOffer.getTotalQuantity());
                break;
        }

        return new OfferChanges(slot, activeOffers[slot], OfferChangeState.CREATED);
    }

    public OfferChanges onItemContainerChanged(ItemContainerChanged event){
        int slot = Arrays.binarySearch(COLLECT_BOX_CONTAINER_ID, event.getContainerId());

        if(slot < 0 || activeOffers[slot] == null)
            return new OfferChanges(slot, null);

        ItemContainer itemContainer = event.getItemContainer();

        Offer offer = activeOffers[slot];
        int boxItems = collectionBoxItems[slot];
        int boxCoins = collectionBoxCoins[slot];
        int newBoxItems = itemContainer.count(offer.getItem().getId());
        int newBoxCoins = itemContainer.count(995);

        Stack<OfferChangeState> changes = new Stack<>();

        if(boxItems > newBoxItems){
            offer.collectItems(boxItems - newBoxItems);
            changes.push(OfferChangeState.COLLECTED_ITEMS);
        }
        else if (newBoxItems > boxItems)
            changes.push(OfferChangeState.OBSERVED_ITEMS);

        if(boxCoins > newBoxCoins){
            offer.collectCoins(boxCoins - newBoxCoins);
            changes.push(OfferChangeState.COLLECTED_COINS);
        }
        else if (newBoxCoins > boxCoins)
            changes.push(OfferChangeState.OBSERVED_COINS);

        if(newBoxItems == 0 && newBoxCoins == 0 && offer.getProgressCollected() == offer.getProgressTotal()){
            activeOffers[slot] = null;
            archivedOffers.push(offer);
            changes.push(OfferChangeState.ARCHIVED);
        }

        collectionBoxItems[slot] = newBoxItems;
        collectionBoxCoins[slot] = newBoxCoins;

        return new OfferChanges(slot, offer, changes);
    }

}
