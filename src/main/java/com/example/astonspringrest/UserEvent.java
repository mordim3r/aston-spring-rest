package com.example.astonspringrest;

public class UserEvent {
    private String type;
    private UserDto user;
    private Integer userid;
    private String email;

    public UserEvent() {
    }

 public static UserEvent created(UserDto user){
        UserEvent event = new UserEvent();
        event.type="Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
        event.user=user;
        return event;
 }
 public static UserEvent updated (UserDto user){
        UserEvent event = new UserEvent();
        event.type="Здравствуйте! Ваш аккаунт на сайте ваш сайт был изменен.";
        event.user=user;
        return event;
 }
 public static UserEvent deleted (int userId, String email){
        UserEvent event = new UserEvent();
        event.type="Здравствуйте! Ваш аккаунт был удалён.";
        event.userid =userId;
        event.email = email;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
