package Project.ScheduleProject.controller;

import Project.ScheduleProject.dto.ScheduleRequestDto;
import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    public ScheduleController (ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    //create
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        return new ResponseEntity<>(scheduleService.saveSchedule(requestDto), HttpStatus.CREATED);
    }

    // Read -> 조회 (전체, 아이디, 수정 날짜, 작성자)
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findSchedules(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String updatedAt,
            @RequestParam(required = false) String author
    ){
        List<ScheduleResponseDto> schedules = scheduleService.findSchedules(id, updatedAt, author);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    // Update -> 수정 (전체 , 일정, 작성자 수정)
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateScheduleById(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, requestDto.getAuthor(), requestDto.getTask(), requestDto.getPassword()), HttpStatus.OK);
    }

    //Delete -> 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto) {
        scheduleService.deleteSchedule(id, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
