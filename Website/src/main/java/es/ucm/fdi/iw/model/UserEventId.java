package es.ucm.fdi.iw.model;

import java.io.Serializable;

public class UserEventId implements Serializable {
    
    private long user;

    private long event;

    // default constructor

    // public UserEventId(User user, Event event) {
    //     this.user = user;
    //     this.event = event;
    // }

    // equals() and hashCode()

    public long getUser(){
        return user;
    }

    public long getEvent(){
        return event;
    }

    public void setUser(long user){
        this.user = user;
    }

    public void setEvent(long event){
        this.event = event;
    }
}