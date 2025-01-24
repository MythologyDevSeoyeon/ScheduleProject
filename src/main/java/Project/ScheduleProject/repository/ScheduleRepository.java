package Project.ScheduleProject.repository;

import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {

   //create
   Schedule saveSchedule(Schedule schedule);

    // Read -> 다건 조회 (전체 조회)
    List<ScheduleResponseDto> findAllSchedules();

    // Read -> 단건 조회 (아이디)
    Schedule findScheduleById(Long id);

    //Delete -> 삭제
    void deleteSchedule(Long id);
}
