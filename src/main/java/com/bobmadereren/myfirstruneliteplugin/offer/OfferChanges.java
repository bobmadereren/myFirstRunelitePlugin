package com.bobmadereren.myfirstruneliteplugin.offer;

import java.util.Stack;

public class OfferChanges extends Stack<OfferChangeState> {

    public final int slot;

    public final Offer offer;

    public OfferChanges(int slot, Offer offer, OfferChangeState ...states) {
        this.slot = slot;
        this.offer = offer;
        for(OfferChangeState state : states)
            push(state);
    }

    @Override
    public String toString() {
        return String.format("Slot: %d, Changes: %s, Offer: %s", slot, super.toString(), offer);
    }

}
