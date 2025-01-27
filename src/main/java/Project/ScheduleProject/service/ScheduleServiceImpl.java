package Project.ScheduleProject.service;

import Project.ScheduleProject.dto.ScheduleRequestDto;
import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.entity.Schedule;
import Project.ScheduleProject.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

    // create
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {
        validateRequiredFields(requestDto.getAuthor(), requestDto.getTask(), requestDto.getPassword());
        Schedule schedule = new Schedule(
                requestDto.getAuthor(),
                requestDto.getPassword(),
                requestDto.getTask(),
                requestDto.getCreatedAt(),
                requestDto.getUpdatedAt()
        );
        return scheduleRepository.saveSchedule(schedule);
    }

    // Read -> 다건 조회 (전체 조회)
    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllSchedules();
    }

    // Read -> 단건 조회 (아이디)
    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    //Update -> 수정 (전체 수정)
    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String author, String task, String password, LocalDateTime updateAt) {
        // 1. 필수 값 검증
        validateRequiredFields(author,task,password);

        // 2. 일정 조회 및 존재 여부 검증
        Optional<Schedule> scheduleOptional = scheduleRepository.findScheduleById(id);
        validateScheduleExistence(scheduleOptional,id);

        // 3. Schedule 객체 추출 및 비밀번호 검증
        Schedule schedule = scheduleOptional.get();
        validatePassword(password, schedule.getPassword());

        // 4. 데이터 수정
        int updatedRow = scheduleRepository.updateSchedule(id, author, task);
        if(updatedRow == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }

        // 5. Schedule 객체 업데이트
        schedule.update(author, task, updateAt);
        return new ScheduleResponseDto(schedule);
    }

    // Update -> 수정 (일정만 수정)
    @Override
    public ScheduleResponseDto updateTask(Long id, String author, String task, String password, LocalDateTime updatedAt) {
        // 1. 필수 값 검증
        validateRequiredFields(task,password);

        // 2. 일정 조회 및 존재 여부 검증
        Optional<Schedule> scheduleOptional = scheduleRepository.findScheduleById(id);
        validateScheduleExistence(scheduleOptional,id);

        // 3. Schedule 객체 추출 및 비밀번호 검증
        Schedule schedule = scheduleOptional.get();
        validatePassword(password, schedule.getPassword());

        // 4. 데이터 수정
        int updatedRow = scheduleRepository.updateTask(id, task);
        if(updatedRow == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }

        return new ScheduleResponseDto(scheduleRepository.findScheduleById(id).get());
    }

    // Update -> 수정 (작성자만 수정)
    @Override
    public ScheduleResponseDto updateAuthor(Long id, String author, String task, String password, LocalDateTime updatedAt) {
        // 1. 필수 값 검증
        validateRequiredFields(author,password);

        // 2. 일정 조회 및 존재 여부 검증
        Optional<Schedule> scheduleOptional = scheduleRepository.findScheduleById(id);
        validateScheduleExistence(scheduleOptional,id);

        // 3. Schedule 객체 추출 및 비밀번호 검증
        Schedule schedule = scheduleOptional.get();
        validatePassword(password, schedule.getPassword());

         // 4. 데이터 수정
        int updatedRow = scheduleRepository.updateAuthor(id,author);
        if(updatedRow == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }
        return new ScheduleResponseDto(scheduleRepository.findScheduleById(id).get());
    }

    //Delete -> 삭제
    @Override
    public void deleteSchedule(Long id, String password) {
        // 1. 일정 조회 및 존재 여부 검증
        Optional<Schedule> scheduleOptional = scheduleRepository.findScheduleById(id);
        validateScheduleExistence(scheduleOptional,id);

        // 2. Schedule 객체 추출 및 비밀번호 검증
        Schedule schedule = scheduleOptional.get();
        validatePassword(password, schedule.getPassword());

        //3. 데이터 삭제
        int updatedRow = scheduleRepository.deleteSchedule(id);
        if(updatedRow ==  0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }
    }

    // 필수 값 검증
    private void validateRequiredFields(Object... fields){
        for(Object field : fields){
            if(field == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 값을 모두 입력해주세요.");
            }
            if(field instanceof String && ((String)field).trim().isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "문자열 값이 비어 있습니다.");
            }
        }
    }

    // 비밀번호 검증
    private void validatePassword(String inputPassword, String actualPassword){
        if(inputPassword == null || inputPassword.trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요.");
        }
        if(!inputPassword.equals(actualPassword)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다.");
        }
    }

    // 일정의 존재 여부
    private void validateScheduleExistence(Optional<Schedule> schedule, Long id){
        if(schedule.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + ": 이 아이디는 존재하지 않는 일정 입니다.");
        }
    }
}
