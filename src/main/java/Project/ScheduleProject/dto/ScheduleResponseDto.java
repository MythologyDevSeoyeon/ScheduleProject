package Project.ScheduleProject.dto;

import Project.ScheduleProject.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class ScheduleResponseDto {
    private Long id;
    private String author;
    private String task;
    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.author = schedule.getAuthor();
        this.task = schedule.getTask();
        this.createdAt = formatDate(schedule.getCreatedAt());
        this.updatedAt = formatDate(schedule.getUpdatedAt());
    }

    // 날짜를 문자열로 포맷팅
    public static String formatDate(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(FORMATTER::format)
                .orElse(null);
    }
}
