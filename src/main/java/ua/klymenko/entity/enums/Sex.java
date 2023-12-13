package ua.klymenko.entity.enums;

public enum Sex {
    MALE("male"),
    FEMALE("female");
    private String sex;

    Sex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }
}
