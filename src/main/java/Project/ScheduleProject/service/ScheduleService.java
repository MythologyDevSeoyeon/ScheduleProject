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

    // Read -> 조건 조회 (수정 날짜, 작성자)
    List<ScheduleResponseDto> findSchedulesByConditions(String updatedDate, String author);

    //Update -> 수정 (전체 수정)
    ScheduleResponseDto updateSchedule(Long id, String author, String task, String password);

    // Update -> 수정 (일정만 수정)
    ScheduleResponseDto updateTask(Long id, String author, String task, String password);

    // Update -> 수정 (작성자만 수정)
    ScheduleResponseDto updateAuthor(Long id, String author, String task, String password);

    //Delete -> 삭제
    void deleteSchedule(Long id, String password);
}
