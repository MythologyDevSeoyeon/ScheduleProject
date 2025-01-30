package Project.ScheduleProject.repository;

import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {

    //create
    ScheduleResponseDto saveSchedule (Schedule schedule);

    // Read -> 조회 (전체, 아이디, 수정 날짜, 작성자)
    List<Schedule> findSchedules(Long id, String updatedAt, String author);

    // Update -> 수정 (전체 , 일정, 작성자 수정)
    int updateSchedule(Long id, String author, String task);

    //Delete -> 삭제
    int deleteSchedule(Long id);

    //optional 검증
    Schedule findScheduleByIdOrElseThrow(Long id);
}
