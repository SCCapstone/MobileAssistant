package com.example.mobileassistant;

/**
 * enum of State
 * Link from: https://stackoverflow.com/questions/11005751/is-there-a-util-to-convert-us-state-name-to-state-code-eg-arizona-to-az
 */

import java.util.HashMap;
import java.util.Map;

public enum State {

    ALABAMA("ALABAMA", "AL"),
    ALASKA("ALASKA", "AK"),
    AMERICAN_SAMOA("AMERICAN SAMOA", "AS"),
    ARIZONA("ARIZONA", "AZ"),
    ARKANSAS("ARKANSAS", "AR"),
    CALIFORNIA("CALIFORNIA", "CA"),
    COLORADO("COLORADO", "CO"),
    CONNECTICUT("CONNECTICUT", "CT"),
    DELAWARE("DELAWARE", "DE"),
    DISTRICT_OF_COLUMBIA("DISTRICT OF COLUMBIA", "DC"),
    FEDERATED_STATES_OF_MICRONESIA("FEDERATED STATES OF MICRONESIA", "FM"),
    FLORIDA("FLORIDA", "FL"),
    GEORGIA("GEORGIA", "GA"),
    GUAM("GUAM", "GU"),
    HAWAII("HAWAII", "HI"),
    IDAHO("IDAHO", "ID"),
    ILLINOIS("ILLINOIS", "IL"),
    INDIANA("INDIANA", "IN"),
    IOWA("IOWA", "IA"),
    KANSAS("KANSAS", "KS"),
    KENTUCKY("KENTUCKY", "KY"),
    LOUISIANA("LOUISIANA", "LA"),
    MAINE("MAINE", "ME"),
    MARYLAND("MARYLAND", "MD"),
    MARSHALL_ISLANDS("MARSHALL ISLANDS", "MH"),
    MASSACHUSETTS("MASSACHUSETTS", "MA"),
    MICHIGAN("MICHIGAN", "MI"),
    MINNESOTA("MINNESOTA", "MN"),
    MISSISSIPPI("MISSISSIPPI", "MS"),
    MISSOURI("MISSOURI", "MO"),
    MONTANA("MONTANA", "MT"),
    NEBRASKA("NEBRASKA", "NE"),
    NEVADA("NEVADA", "NV"),
    NEW_HAMPSHIRE("NEW HAMPSHIRE", "NH"),
    NEW_JERSEY("NEW JERSEY", "NJ"),
    NEW_MEXICO("NEW MEXICO", "NM"),
    NEW_YORK("NEW YORK", "NY"),
    NORTH_CAROLINA("NORTH CAROLINA", "NC"),
    NORTH_DAKOTA("NORTH DAKOTA", "ND"),
    NORTHERN_MARIANA_ISLANDS("NORTHERN MARIANA ISLANDS", "MP"),
    OHIO("OHIO", "OH"),
    OKLAHOMA("OKLAHOMA", "OK"),
    OREGON("OREGON", "OR"),
    PALAU("PALAU", "PW"),
    PENNSYLVANIA("PENNSYLVANIA", "PA"),
    PUERTO_RICO("PUERTO RICO", "PR"),
    RHODE_ISLAND("RHODE ISLAND", "RI"),
    SOUTH_CAROLINA("SOUTH CAROLINA", "SC"),
    SOUTH_DAKOTA("SOUTH DAKOTA", "SD"),
    TENNESSEE("TENNESSEE", "TN"),
    TEXAS("TEXAS", "TX"),
    UTAH("UTAH", "UT"),
    VERMONT("VERMONT", "VT"),
    VIRGIN_ISLANDS("VIRGIN ISLANDS", "VI"),
    VIRGINIA("VIRGINIA", "VA"),
    WASHINGTON("WASHINGTON", "WA"),
    WEST_VIRGINIA("WEST VIRGINIA", "WV"),
    WISCONSIN("WISCONSIN", "WI"),
    WYOMING("WYOMING", "WY"),
    UNKNOWN("UNKNOWN", "");

    /**
     * The state's name.
     */
    private String name;

    /**
     * The state's abbreviation.
     */
    private String abbreviation;

    /**
     * The set of states addressed by abbreviations.
     */
    private static final Map<String, State> STATES_BY_ABBR = new HashMap<String, State>();

    /* static initializer */
    static {
        for (State state : values()) {
            STATES_BY_ABBR.put(state.getAbbreviation(), state);
        }
    }

    /**
     * Constructs a new state.
     *
     * @param name the state's name.
     * @param abbreviation the state's abbreviation.
     */
    State(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    /**
     * Returns the state's abbreviation.
     *
     * @return the state's abbreviation.
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Gets the enum constant with the specified abbreviation.
     *
     * @param abbr the state's abbreviation.
     * @return the enum constant with the specified abbreviation.
     */
    // eg: "SC" -> "South Carolina"
    public static State abbrToState(final String abbr) {
        final State state = STATES_BY_ABBR.get(abbr);
        if (state != null) {
            return state;
        } else {
            return UNKNOWN;
        }
    }

    // State name to Abbreviation
    // eg: "South Carolina" -> "SC"
    public static String stateToAbbr(final String name) {
        final String enumName = name.toUpperCase().replaceAll(" ", "_");
        try {
            return valueOf(enumName).getAbbreviation();
        } catch (final IllegalArgumentException e) {
            return State.UNKNOWN.toString();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}

