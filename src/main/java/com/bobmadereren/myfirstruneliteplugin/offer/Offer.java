package com.bobmadereren.myfirstruneliteplugin.offer;

import lombok.Setter;
import net.runelite.api.ItemComposition;

public abstract class Offer {

    private final ItemComposition item;

    private final int price;

    private final int progressTotal;

    @Setter
    private int progressValue;

    private int netItems;

    private int netCoins;

    public Offer(ItemComposition item, int price, int quantity, int netItems, int netCoins){
        this.item = item;
        this.price = price;
        this.progressTotal = quantity;
        this.netItems = netItems;
        this.netCoins = netCoins;
    }

    public void collectItems(int quantity){
        netItems += quantity;
        System.out.println("Net items: " + netItems);
    }

    public void collectCoins(int quantity){
        netCoins += quantity;
        System.out.println("Net coins: " + netCoins);
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Price: %,d, Progress: %,d / %,d, Net items: %,d, Net coins %,d",
                item.getName(),
                price,
                progressValue, progressTotal,
                netItems,
                netCoins
        );
    }

    public int getItemId(){
        return item.getId();
    }

}
