package com.bobmadereren.myfirstruneliteplugin.offer;

import net.runelite.api.ItemComposition;

public class SellOffer extends Offer {

    public SellOffer(ItemComposition item, int price, int quantity) {
        super(item, price, quantity, -quantity, 0);
    }

}
