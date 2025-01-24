package Project.ScheduleProject.entity;

import Project.ScheduleProject.dto.ScheduleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long id;
    private String author;
    private String password;
    private String task;
    private String createdAt;
    private String updatedAt;

    public void update(ScheduleRequestDto requestDto) {
        this.author = requestDto.getAuthor();
        this.password = requestDto.getPassword();
        this.task = requestDto.getTask();
        this.updatedAt = requestDto.getUpdatedAt();
    }

    public void updateTask(ScheduleRequestDto requestDto) {
        this.task = requestDto.getTask();
    }

    public void updateAuthor(ScheduleRequestDto requestDto) {
        this.author = requestDto.getAuthor();
    }
}
