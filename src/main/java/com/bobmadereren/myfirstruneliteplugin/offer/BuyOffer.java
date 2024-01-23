package com.bobmadereren.myfirstruneliteplugin.offer;

import lombok.Getter;
import net.runelite.api.ItemComposition;

public class BuyOffer extends Offer{

    @Getter
    private int progressCollected;

    public BuyOffer(ItemComposition item, int price, int quantity) {
        super(item, price, quantity, 0, -price * quantity);
    }

    @Override
    public void collectItems(int quantity){
        super.collectItems(quantity);
        progressCollected = getProgressTotal();
    }

}
