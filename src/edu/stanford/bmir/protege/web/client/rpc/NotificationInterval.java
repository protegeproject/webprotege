package edu.stanford.bmir.protege.web.client.rpc;

/**
 */
public enum NotificationInterval {
    HOURLY("Hourly", 1 * 1000 * 60 * 60),DAILY("Daily", 1 * 1000 * 60 * 60 * 24),IMMEDIATELY("Immediately", 0), NEVER("Never", -1);
    private final String value;
    private final long intervalAsMilliseconds;

    NotificationInterval(String value, long intervalAsMilliseconds) {
        this.value = value;
        this.intervalAsMilliseconds = intervalAsMilliseconds;
    }

    public String getValue() {
        return value;
    }

    public static NotificationInterval fromString(String str){
        if (HOURLY.getValue().equals(str)){
            return HOURLY;
        }
        if (DAILY.getValue().equals(str)){
            return DAILY;
        }
        if (IMMEDIATELY.getValue().equals(str)){
            return IMMEDIATELY;
        }
        if (NEVER.getValue().equals(str)){
            return NEVER;
        }
        return null;
    }

    public long getIntervalInMilliseconds() {
        return intervalAsMilliseconds;
    }


}
