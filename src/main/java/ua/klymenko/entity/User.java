package ua.klymenko.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.klymenko.entity.enums.Role;
import ua.klymenko.entity.enums.Sex;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class User {
    private String userId;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String password;
    private Sex sex;
    private Role role;
    private Map<Lesson, Integer> lessonsWithGrade;

    public User() {
        this.lessonsWithGrade = new HashMap<>();
    }

    public void setLessonsWithGrade(Lesson lesson, Integer grade) {
        this.lessonsWithGrade.put(lesson, grade);
    }

    private User(Builder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.surname = builder.surname;
        this.phone = builder.phone;
        this.email = builder.email;
        this.password = builder.password;
        this.sex = builder.sex;
        this.role = builder.role;
        this.lessonsWithGrade = new HashMap<>();
        if (builder.lessonsWithGrade != null) {
            this.lessonsWithGrade.putAll(builder.lessonsWithGrade);
        }
    }

    public static class Builder {
        private String userId;
        private String name;
        private String surname;
        private String phone;
        private String email;
        private String password;
        private Sex sex;
        private Role role;
        private Map<Lesson, Integer> lessonsWithGrade;

        public Builder() {
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setSex(Sex sex) {
            this.sex = sex;
            return this;
        }

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setLessonsWithGrade(Map<Lesson, Integer> lessonsWithGrade) {
            this.lessonsWithGrade = lessonsWithGrade;
            return this;
        }

        public User build() {
            if (name == null || surname == null || phone == null || email == null || password == null || sex == null || role == null) {
                throw new IllegalStateException("Не всі обов'язкові поля були встановлені");
            }
            return new User(this);
        }
    }
}
