package com.example.astonspringrest;

public class UserEvent {
    private String type;
    private UserDto user;
    private Integer userid;

    public UserEvent() {
    }

 public static UserEvent created(UserDto user){
        UserEvent event = new UserEvent();
        event.type="Created";
        event.user=user;
        return event;
 }
 public static UserEvent updated (UserDto user){
        UserEvent event = new UserEvent();
        event.type="Updated";
        event.user=user;
        return event;
 }
 public static UserEvent deleted (int userId){
        UserEvent event = new UserEvent();
        event.type="Deleted";
        event.userid =userId;
        return event;
 }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userid;
    }

    public void setUserId(Integer userid) {
        this.userid = userid;
    }
}
