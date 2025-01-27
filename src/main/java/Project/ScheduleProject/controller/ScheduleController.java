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

//    // Read -> 다건 조회 (전체 조회)
//    @GetMapping
//    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules() {
//        return new ResponseEntity<>(scheduleService.findAllSchedules(), HttpStatus.OK);
//    }

    // Read -> 단건 조회 (아이디)
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    // Read -> 조건 조회 (수정 날짜, 작성자)
    @GetMapping("/filter")
    public ResponseEntity<List<ScheduleResponseDto>> findSchedulesByFilter(
            @RequestParam(required = false) String updatedAt,
            @RequestParam(required = false) String author
    ) {
        return new ResponseEntity<>(scheduleService.findSchedulesByConditions(updatedAt, author), HttpStatus.OK);
    }


    //Update -> 수정 (전체 수정)
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateScheduleById(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, requestDto.getAuthor(), requestDto.getTask(), requestDto.getPassword()), HttpStatus.OK);
    }

    // Update -> 수정 (일정만 수정)
    @PatchMapping("/{id}/task")
    public ResponseEntity<ScheduleResponseDto> updateTask(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ){
        return new ResponseEntity<>(scheduleService.updateTask(id, requestDto.getAuthor(), requestDto.getTask(), requestDto.getPassword()), HttpStatus.OK);
    }


    // Update -> 수정 (작성자만 수정)
    @PatchMapping("/{id}/author")
    public ResponseEntity<ScheduleResponseDto> updateAuthor (
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ){
        return new ResponseEntity<>(scheduleService.updateAuthor(id, requestDto.getAuthor(), requestDto.getTask(), requestDto.getPassword()), HttpStatus.OK);
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
