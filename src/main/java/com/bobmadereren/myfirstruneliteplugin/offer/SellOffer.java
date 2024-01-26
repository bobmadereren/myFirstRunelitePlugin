package com.bobmadereren.myfirstruneliteplugin.offer;

import lombok.Getter;
import net.runelite.api.ItemComposition;

public class SellOffer extends Offer {

    @Getter
    private int progressCollected;

    public SellOffer(ItemComposition item, int price, int quantity) {
        super(item, price, quantity, -quantity, 0);
    }

    @Override
    public void collectCoins(int quantity){
        super.collectCoins(quantity);
        progressCollected = getProgressValue();
    }

}
