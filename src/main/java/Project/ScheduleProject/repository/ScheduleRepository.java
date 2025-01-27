package Project.ScheduleProject.repository;

import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    //create
    ScheduleResponseDto saveSchedule (Schedule schedule);

    // Read -> 다건 조회 (전체 조회)
    List<ScheduleResponseDto> findAllSchedules();

    // Read -> 단건 조회 (아이디)
    Optional<Schedule> findScheduleById(Long id);

    // Read -> 조건 조회 (수정 날짜, 작성자)
    List<ScheduleResponseDto> findScheduleByConditions(String updatedAt, String author);

    // Update -> 수정 (전체 수정)
    int updateSchedule(Long id, String author, String task);

    // Update -> 수정 (일정만 수정)
    int updateTask(Long id, String task);

    // Update -> 수정 (작성자만 수정)
    int updateAuthor(Long id, String author);

    //Delete -> 삭제
    int deleteSchedule(Long id);

    //optional 검증
    Schedule findScheduleByIdOrElseThrow(Long id);
}
