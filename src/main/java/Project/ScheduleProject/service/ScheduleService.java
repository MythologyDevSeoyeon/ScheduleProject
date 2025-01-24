package Project.ScheduleProject.service;

import Project.ScheduleProject.dto.ScheduleRequestDto;
import Project.ScheduleProject.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    //create
    ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto);

    // Read -> 다건 조회 (전체 조회)
    List<ScheduleResponseDto> findAllSchedules();

    // Read -> 단건 조회 (아이디)
    ScheduleResponseDto findScheduleById(Long id);

    //Update -> 수정 (전체 수정)
    ScheduleResponseDto updateSchedule(Long id, String author, String task, String password, String updatedAt);

    // Update -> 수정 (일정만 수정)
    ScheduleResponseDto updateTask(Long id, String author, String task, String password, String updatedAt);

    // Update -> 수정 (작성자만 수정)
    ScheduleResponseDto updateAuthor(Long id, String author, String task, String password, String updatedAt);

    //Delete -> 삭제
    void deleteSchedule(Long id);
}
