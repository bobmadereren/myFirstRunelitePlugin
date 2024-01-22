package com.bobmadereren.myfirstruneliteplugin.offer;

import net.runelite.api.ItemComposition;

public class BuyOffer extends Offer{

    public BuyOffer(ItemComposition item, int price, int quantity) {
        super(item, price, quantity, 0, -price * quantity);
    }

}
