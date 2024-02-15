package com.appagility.shipping.query;

public class FindAllSailingsQuery {


    private final boolean limitToUnintendedDestinations;

    public FindAllSailingsQuery(boolean limitToUnintendedDestinations) {
        this.limitToUnintendedDestinations = limitToUnintendedDestinations;
    }

    public boolean isLimitToUnintendedDestinations() {
        return limitToUnintendedDestinations;
    }
}
