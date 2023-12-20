package ua.klymenko.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Homework {
    private String homeworkId;
    private String description;
    private LocalDateTime dueDateTime;

    private List<HomeworkMemento> mementos = new ArrayList<>();
    private Homework(Builder builder) {
        this.homeworkId = builder.homeworkId;
        this.description = builder.description;
        this.dueDateTime = builder.dueDateTime;
    }

    public static class Builder {
        private String homeworkId;
        private String description;
        private LocalDateTime dueDateTime;

        public Builder() {
        }

        public Builder setHomeworkId(String homeworkId) {
            this.homeworkId = homeworkId;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setDueDateTime(LocalDateTime dueDateTime) {
            this.dueDateTime = dueDateTime;
            return this;
        }

        public Homework build() {
            if (description == null || dueDateTime == null) {
                throw new IllegalStateException("Не всі обов'язкові поля були встановлені");
            }
            return new Homework(this);
        }
    }

    public class HomeworkMemento {
        private String homeworkId;
        private String description;
        private LocalDateTime dueDateTime;

        public HomeworkMemento(Homework homework) {
            this.homeworkId = homework.homeworkId;
            this.description = homework.description;
            this.dueDateTime = homework.dueDateTime;
        }

        public String getHomeworkId() {
            return homeworkId;
        }

        public void setHomeworkId(String homeworkId) {
            this.homeworkId = homeworkId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDateTime getDueDateTime() {
            return dueDateTime;
        }

        public void setDueDateTime(LocalDateTime dueDateTime) {
            this.dueDateTime = dueDateTime;
        }

        public HomeworkMemento saveToMemento() {
            HomeworkMemento memento = new HomeworkMemento(Homework.this);
            mementos.add(memento);
            return memento;
        }

        public int getMementoSize() {
            return mementos.size();
        }

        public HomeworkMemento undo() {
            if (!mementos.isEmpty() || mementos.size() - 1 > 1) {
                HomeworkMemento memento = mementos.remove(mementos.size() - 1);
                this.homeworkId = memento.homeworkId;
                this.description = memento.description;
                this.dueDateTime = memento.getDueDateTime();
            } else {
                System.out.println("Це початковий стан. Попереднього стану об'єкта не існує!");
            }
            if (!mementos.isEmpty()) {
                return mementos.get(mementos.size() - 1);
            }
            return null;
        }

        public static Homework fromMemento(HomeworkMemento memento) {
            return new Homework.Builder()
                    .setHomeworkId(memento.homeworkId)
                    .setDescription(memento.getDescription())
                    .setDueDateTime(memento.getDueDateTime())
                    .build();
        }
    }
}
