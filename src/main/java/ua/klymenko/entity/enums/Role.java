package ua.klymenko.entity.enums;

public enum Role {
    STUDENT("student"),
    TEACHER("teacher");
    private String role;

    Role(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}