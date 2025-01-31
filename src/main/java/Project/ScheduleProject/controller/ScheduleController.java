package Project.ScheduleProject.controller;

import Project.ScheduleProject.dto.ScheduleRequestDto;
import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@Tag(name = "일정 관리 API", description = "일정을 생성하고 조회하는 API입니다.")
public class ScheduleController {

    private final ScheduleService scheduleService;
    public ScheduleController (ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    //create
    @PostMapping
    @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다.")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        return new ResponseEntity<>(scheduleService.saveSchedule(requestDto), HttpStatus.CREATED);
    }

    // Read -> 조회 (전체, 아이디, 수정 날짜, 작성자)
    @GetMapping
    @Operation(summary = "일정 목록 조회", description = "등록된 모든 일정을 조회합니다.")
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
    @Operation(summary = "일정 수정", description = "일정 ID를 입력하여 해당 일정을 수정합니다.")
    public ResponseEntity<ScheduleResponseDto> updateScheduleById(
            @Parameter(description = "수정할 일정의 ID") @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, requestDto.getAuthor(), requestDto.getTask(), requestDto.getPassword()), HttpStatus.OK);
    }

    //Delete -> 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "일정 삭제", description = "일정 ID를 입력하여 특정 일정을 삭제합니다.")
    public ResponseEntity<Void> deleteSchedule(
            @Parameter(description = "삭제할 일정의 ID") @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto) {
        scheduleService.deleteSchedule(id, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
