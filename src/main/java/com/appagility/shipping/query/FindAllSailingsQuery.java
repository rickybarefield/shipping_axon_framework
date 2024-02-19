package com.appagility.shipping.query;

import java.util.Objects;

public class FindAllSailingsQuery {


    private final boolean limitToUnintendedDestinations;

    public FindAllSailingsQuery(boolean limitToUnintendedDestinations) {
        this.limitToUnintendedDestinations = limitToUnintendedDestinations;
    }

    public boolean isLimitToUnintendedDestinations() {
        return limitToUnintendedDestinations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FindAllSailingsQuery that = (FindAllSailingsQuery) o;
        return limitToUnintendedDestinations == that.limitToUnintendedDestinations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(limitToUnintendedDestinations);
    }
}
