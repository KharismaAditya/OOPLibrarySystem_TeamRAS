package model;

public class students {
    private String userId;
    private String userName;
    private String userEmail;
    private String userDepart;

    public students(String userId, String userName, String userEmail, String userDepart){
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userDepart = userDepart;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserDepart() {
        return userDepart;
    }
}
