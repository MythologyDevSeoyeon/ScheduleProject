package Project.ScheduleProject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long id;
    private String author;
    private String password;
    private String task;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schedule(String author, String password, String task){
        this.author = author;
        this.password = password;
        this.task = task;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String author, String task) {
        this.author = author;
        this.task = task;
        this.updatedAt = LocalDateTime.now();
    }
}
