package Project.ScheduleProject.repository;

import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.entity.Schedule;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository{

    //임시 저장소 = 자료 구조가 DB 역할 수행
    private final Map<Long, Schedule> scheduleList = new HashMap<>();

    //저장 create
    @Override
    public Schedule saveSchedule(Schedule schedule) {
        //고유식별자(ID)생성
        Long scheduleId = scheduleList.isEmpty() ? 1 : Collections.max(scheduleList.keySet()) + 1;
        schedule.setId(scheduleId);
        scheduleList.put(scheduleId,schedule);
        return schedule;
    }

    // Read -> 다건 조회 (전체 조회)
    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        //초기 리스트
        List<ScheduleResponseDto> allSchedules = new ArrayList<>();
        for(Schedule schedule : scheduleList.values()){
            ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
            allSchedules.add(responseDto);
        }
        return allSchedules;
    }

    // Read -> 단건 조회 (아이디)
    @Override
    public Schedule findScheduleById(Long id) {
        return scheduleList.get(id);
    }

    //Delete -> 삭제
    @Override
    public void deleteSchedule(Long id) {
        scheduleList.remove(id);
    }
}


