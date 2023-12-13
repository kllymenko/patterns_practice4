package ua.klymenko.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    private String lessonId;
    private String name;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private int cabNum;
    private String topic;
    private Homework homework;

    private Lesson(Builder builder) {
        this.lessonId = builder.lessonId;
        this.name = builder.name;
        this.date = builder.date;
        this.timeStart = builder.timeStart;
        this.timeEnd = builder.timeEnd;
        this.cabNum = builder.cabNum;
        this.topic = builder.topic;
        this.homework = builder.homework;
    }

    public static class Builder {
        private String lessonId;
        private String name;
        private LocalDate date;
        private LocalTime timeStart;
        private LocalTime timeEnd;
        private int cabNum;
        private String topic;
        private Homework homework;

        public Builder() {
        }

        public Builder setLessonId(String lessonId) {
            this.lessonId = lessonId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setTimeStart(LocalTime timeStart) {
            this.timeStart = timeStart;
            return this;
        }

        public Builder setTimeEnd(LocalTime timeEnd) {
            this.timeEnd = timeEnd;
            return this;
        }

        public Builder setCabNum(int cabNum) {
            this.cabNum = cabNum;
            return this;
        }

        public Builder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder setHomework(Homework homework) {
            this.homework = homework;
            return this;
        }

        public Lesson build() {
            if (name == null || topic == null) {
                throw new IllegalStateException("Не всі обов'язкові поля були встановлені");
            }
            return new Lesson(this);
        }
    }
}
