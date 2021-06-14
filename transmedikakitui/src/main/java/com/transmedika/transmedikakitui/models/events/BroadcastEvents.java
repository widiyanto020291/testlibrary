package com.transmedika.transmedikakitui.models.events;

public class BroadcastEvents {
    private Event event;

    public BroadcastEvents() {
        super();
    }

    public BroadcastEvents(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public static class Event {
        private String initString;
        private Object object;


        public Event() {
            super();
        }

        public String getInitString() {
            return initString;
        }

        public void setInitString(String initString) {
            this.initString = initString;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }
}
