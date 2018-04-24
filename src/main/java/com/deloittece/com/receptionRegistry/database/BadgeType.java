package com.deloittece.com.receptionRegistry.database;

public enum BadgeType {

	VISITOR_ERDC("Visitor badge ERDC"),
	VISITOR_GES("Visitor badge GES"),
	VISITOR_ADC("Visitor badge ADC"),
	TEMPORARILY_ERDC("Temporarily badge ERDC"), 
	TEMPORARILY_GES("Temporarily badge GES"),
	TEMPORARILY_ADC("Temporarily badge ADC");
	
	private final String displayName;

	BadgeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


