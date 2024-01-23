package com.bobmadereren.myfirstruneliteplugin.offer;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.ItemComposition;

@Getter
public abstract class Offer {

    private final ItemComposition item;

    private final int price;

    private final int progressTotal;

    @Setter
    private int progressSold;

    public abstract int getProgressCollected();

    private int netItems;

    private int netCoins;

    public Offer(ItemComposition item, int price, int quantity, int netItems, int netCoins) {
        this.item = item;
        this.price = price;
        this.progressTotal = quantity;
        this.netItems = netItems;
        this.netCoins = netCoins;
    }

    public void collectItems(int quantity) {
        netItems += quantity;
    }

    public void collectCoins(int quantity){
        netCoins += quantity;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Price: %,d, Progress: %,d / %,d, Net items: %,d, Net coins %,d",
                item.getName(),
                price,
                progressSold, progressTotal,
                netItems,
                netCoins
        );
    }

}
