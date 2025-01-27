package Project.ScheduleProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ScheduleRequestDto {

    private String author;
    private String password;
    private String task;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
