package Project.ScheduleProject.controller;

import Project.ScheduleProject.dto.ScheduleRequestDto;
import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    //임시 저장소 = 자료 구조가 DB 역할 수행
    private final Map<Long, Schedule> scheduleList = new HashMap<>();

    //create
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        // 고유 식별자 (1씩 증가)
        Long scheduleId = scheduleList.isEmpty() ? 1 : Collections.max(scheduleList.keySet()) + 1;

        // Schedule 객체 생성
        Schedule schedule = new Schedule(scheduleId, requestDto.getAuthor(), requestDto.getPassword(), requestDto.getTask(), requestDto.getCreatedAt(), requestDto.getUpdatedAt());

        //임시 DB에 값 저장
        scheduleList.put(scheduleId, schedule);

        return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.CREATED);
    }

    // Read -> 다건 조회 (전체 조회)
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules() {

        // 초기 리스트
        List<ScheduleResponseDto> responseList = new ArrayList<>();

        for (Schedule schedule : scheduleList.values()) {
            ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
            responseList.add(responseDto);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    // Read -> 단건 조회 (아이디)
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {

        Schedule schedule = scheduleList.get(id);

        //NPE방지
        if(schedule == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.OK);
    }

    //Update -> 수정 (전체 수정)
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateScheduleById(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {
        Schedule schedule = scheduleList.get(id);

        // NPE 방지
        if(schedule == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //필수 값 검증
        if(requestDto.getAuthor() == null ||requestDto.getPassword() == null || requestDto.getTask() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 검증
        if(!requestDto.getPassword().equals(schedule.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        schedule.update(requestDto);

        return new ResponseEntity<>(new ScheduleResponseDto(schedule),HttpStatus.OK);
    }

    // Update -> 수정 (일정만 수정)
    @PatchMapping("/{id}/task")
    public ResponseEntity<ScheduleResponseDto> updateTask(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ){
        Schedule schedule = scheduleList.get(id);

        //NPE 방지
        if(schedule == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //필수 값 검증
        if(requestDto.getTask() == null || requestDto.getPassword() == null || requestDto.getAuthor() != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 검증
        if(!requestDto.getPassword().equals(schedule.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        schedule.updateTask(requestDto);

        return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.OK);
    }


    // Update -> 수정 (작성자만 수정)
    @PatchMapping("/{id}/author")
    public ResponseEntity<ScheduleResponseDto> updateAuthor (
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ){
        Schedule schedule = scheduleList.get(id);

        //NPE 방지
        if(schedule == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //필수 값 검증
        if(requestDto.getAuthor() == null || requestDto.getPassword() == null || requestDto.getTask() != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //비밀번호 검증
        if(!requestDto.getPassword().equals(schedule.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        schedule.updateAuthor(requestDto);

        return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.OK);
    }


    //Delete -> 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {

        //NPE 검증
        if(!scheduleList.containsKey(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        scheduleList.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
