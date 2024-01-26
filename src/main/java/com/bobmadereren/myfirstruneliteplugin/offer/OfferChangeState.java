package com.bobmadereren.myfirstruneliteplugin.offer;

public enum OfferChangeState {
    /**
     * Starting to track an offer.
     */
    CREATED,

    /**
     * A tracked offer is sold (or bought) .
     */
    SOLD,

    /**
     * A tracked offer's collection box is observed with a higher quantity
     * of coins or items than it was previously observed with.
     */
    COLLECTION_BOX_OBSERVED,

    /**
     * One or both of the items in the collection box of a tracked offer is being collected.
     */
    COLLECTED,

    /**
     * A tracked offer has been archived.
     */
    ARCHIVED,
}
