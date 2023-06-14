package com.example.planeatapp;

import java.util.List;

public class EventConfirmations {
    private List<Confirmation> confirmations;

    public List<Confirmation> getConfirmations() {
        return confirmations;
    }
}

class Confirmation {
    private String _id;
    private String eventID;
    private String name;
    private String option;

    public String get_id()  { return _id; }

    public String getEventID() { return eventID; }

    public String getName() { return name; }

    public String getOption() { return option; }

}
