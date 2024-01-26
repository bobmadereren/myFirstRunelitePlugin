package com.bobmadereren.myfirstruneliteplugin.offer;

public enum OfferChangeState {
    /**
     * Starting to track an offer.
     */
    CREATED,

    /**
     * A tracked offer sold (or bought).
     */
    SOLD_PARTIALLY,

    /**
     * A tracked offer sold (or bought) out.
     */
    SOLD_OUT,

    /**
     * A tracked offer was cancelled.
     */
    CANCELLED,

    /**
     * A tracked offer's collection box is observed with a higher quantity
     * of items than it was previously observed with.
     */
    OBSERVED_ITEMS,

    /**
     * A tracked offer's collection box is observed with a higher quantity
     * of coins than it was previously observed with.
     */
    OBSERVED_COINS,

    /**
     * Items has been collected from the collection box of a tracked offer.
     */
    COLLECTED_ITEMS,

    /**
     * Coins has been collected from the collection box of a tracked offer.
     */
    COLLECTED_COINS,

    /**
     * A tracked offer has been archived.
     */
    ARCHIVED,
}
