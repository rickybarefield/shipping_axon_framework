package com.appagility.shipping.query;

public class FindAllShipsQuery {

    private final boolean limitToShipsReadyForSailing;

    public FindAllShipsQuery(boolean limitToShipsReadyForSailing) {

        this.limitToShipsReadyForSailing = limitToShipsReadyForSailing;
    }

    public boolean isLimitToShipsReadyForSailing() {

        return limitToShipsReadyForSailing;
    }
}
