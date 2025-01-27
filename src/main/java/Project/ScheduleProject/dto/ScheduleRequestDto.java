package Project.ScheduleProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScheduleRequestDto {

    private String author;
    private String password;
    private String task;
}
