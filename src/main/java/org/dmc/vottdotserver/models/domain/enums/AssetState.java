package org.dmc.vottdotserver.models.domain.enums;

public enum AssetState {
    DISABLED("Disabled"),
    NOTVISITED("NotVisited"),
    VISITIED("Visited"),
    TAGGEDDOT("TaggedDot"),
    TAGGEDRECTANGLE("TaggedRectangle"),
    COMMENTED("Commented"),
    REJECTED("Rejected"),
    APPROVED("Approved"),
    COMPLETED("Completed");

    // Enum Variables
    private final String value;

    /**
     * The LedgerRequestStatus constructor.
     *
     * @param value     The string value of the enum
     */
    AssetState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
