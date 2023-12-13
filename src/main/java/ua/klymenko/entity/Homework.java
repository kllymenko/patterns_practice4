package ua.klymenko.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Homework {
    private String homeworkId;
    private String description;
    private LocalDateTime dueDateTime;

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
}
