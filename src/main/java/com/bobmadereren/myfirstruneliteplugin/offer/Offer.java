package com.bobmadereren.myfirstruneliteplugin.offer;

import lombok.Getter;
import net.runelite.api.ItemComposition;

@Getter
public abstract class Offer {

    private final ItemComposition item;

    private final int price;

    private int progressTotal;

    private int progressValue;

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

    protected void collectItems(int quantity) {
        netItems += quantity;
    }

    protected void collectCoins(int quantity){
        netCoins += quantity;
    }

    protected void progress(int value){
        progressValue += value;
    }

    protected void cancel(){
        progressTotal = progressValue;
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

    protected boolean isSoldOut(){
        return progressValue == progressTotal;
    }
}
