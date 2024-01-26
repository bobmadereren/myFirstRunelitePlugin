package com.bobmadereren.myfirstruneliteplugin.offer;

public class OfferChange {

    public final Offer offer;

    public final int slot;

    public final OfferChangeState state;

    public OfferChange(Offer offer, int slot, OfferChangeState state) {
        this.offer = offer;
        this.slot = slot;
        this.state = state;
    }
}
