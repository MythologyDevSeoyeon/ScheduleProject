package Project.ScheduleProject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Schedule {
    @Setter
    private Long id;
    private String author;
    private String password;
    private String task;
    private String createdAt;
    private String updatedAt;

    public Schedule(String author, String password, String task, String createdAt, String updatedAt){
        this.author = author;
        this.password = password;
        this.task = task;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(String author, String task, String updatedAt) {
        this.author = author;
        this.task = task;
        this.updatedAt = updatedAt;
    }

    public void updateTask(String task, String updatedAt) {
        this.task = task;
        this.updatedAt = updatedAt;
    }

    public void updateAuthor(String author, String updatedAt) {
        this.author = author;
        this.updatedAt = updatedAt;
    }

}
