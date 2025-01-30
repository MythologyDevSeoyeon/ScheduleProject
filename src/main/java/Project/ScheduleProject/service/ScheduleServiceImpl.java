package Project.ScheduleProject.service;

import Project.ScheduleProject.dto.ScheduleRequestDto;
import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.entity.Schedule;
import Project.ScheduleProject.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // create
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {
        validateRequiredFields(requestDto.getAuthor(), requestDto.getTask(), requestDto.getPassword());
        Schedule schedule = new Schedule(requestDto.getAuthor(), requestDto.getPassword(), requestDto.getTask());
        return scheduleRepository.saveSchedule(schedule);
    }

    // Read -> 조회 (전체, 아이디, 수정 날짜, 작성자)
    @Override
    public List<ScheduleResponseDto> findSchedules(Long id, String updatedDate, String author) {
        if (id != null) {
            validateScheduleExistence(id);
            Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
            return List.of(new ScheduleResponseDto(schedule));
        }
        List<Schedule> schedules = scheduleRepository.findSchedules(null, updatedDate, author);
        if(schedules.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조건에 맞는 데이터가 없습니다.");
        }
        return schedules.stream().map(ScheduleResponseDto::new).toList();
    }


    // Update -> 수정 (전체 , 일정, 작성자 수정)
    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String author, String task, String password) {
        validateRequiredFields(password);
        if ((author == null || author.trim().isEmpty()) && (task == null || task.trim().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정할 데이터가 없습니다.");
        }
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        validatePassword(password, schedule.getPassword());
        int updatedRow = scheduleRepository.updateSchedule(id, author, task);
        validateUpdateResult(updatedRow,"수정");
        return new ScheduleResponseDto(scheduleRepository.findSchedules(id, null, null).get(0));
    }

    //Delete -> 삭제
    @Override
    public void deleteSchedule(Long id, String password) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        validatePassword(password, schedule.getPassword());
        int deletedRow = scheduleRepository.deleteSchedule(id);
        validateUpdateResult(deletedRow,"삭제");
    }

    // 필수 값 검증
    private void validateRequiredFields(Object... fields) {
        for (Object field : fields) {
            if (field == null || (field instanceof String && ((String) field).trim().isEmpty())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "문자열 값이 비어 있습니다.");
            }
        }
    }

    // 비밀번호 검증
    private void validatePassword(String inputPassword, String actualPassword) {
        if (inputPassword == null || inputPassword.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요.");
        }
        if (!inputPassword.equals(actualPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
    }

    // 일정의 존재 여부
    private void validateScheduleExistence(Long id) {
        Optional<Schedule> schedule = scheduleRepository.findSchedules(id, null, null).stream().findFirst();
        if (schedule.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + ": 이 아이디는 존재하지 않는 일정 입니다.");
        }
    }

    // 업데이트 결과 검증
    private void validateUpdateResult(int updatedRow, String action){
        if(updatedRow == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, action + "된 데이터가 없습니다.");
        }
    }
}
