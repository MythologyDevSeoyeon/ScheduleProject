package Project.ScheduleProject.service;

import Project.ScheduleProject.dto.ScheduleRequestDto;
import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.entity.Schedule;
import Project.ScheduleProject.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

    // create
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {

        Schedule schedule = new Schedule(requestDto.getAuthor(), requestDto.getPassword(),requestDto.getTask(),requestDto.getCreatedAt(), requestDto.getUpdatedAt());
        Schedule savedSchedule = scheduleRepository.saveSchedule(schedule);

        return new ScheduleResponseDto(savedSchedule);
    }

    // Read -> 다건 조회 (전체 조회)
    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllSchedules();
    }

    // Read -> 단건 조회 (아이디)
    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        //NPE방지
        if(schedule == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        return new ScheduleResponseDto(schedule);
    }

    //Update -> 수정 (전체 수정)
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String author, String task, String password, String updateAt) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        //NPE방지
        if(schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + "는 존재하지 않는 아이디 입니다.");
        }

        //필수 값 검증
        if(author == null || task == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"작성자와, 할일을 입력해주세요.");
        }

        // 비밀번호 검증
        if(password == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"비밀번호를 입력해주세요.");
        }else if(!password.equals(schedule.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다.");
        }

        schedule.update(author, task, updateAt);

        return new ScheduleResponseDto(schedule);
    }

    // Update -> 수정 (일정만 수정)
    @Override
    public ScheduleResponseDto updateTask(Long id, String author, String task, String password, String updatedAt) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        //NPE방지
        if(schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + "는 존재하지 않는 아이디 입니다.");
        }

        //필수 값 검증
        if(author != null || task == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"작성자와, 할일을 입력해주세요.");
        }

        // 비밀번호 검증
        if(password == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"비밀번호를 입력해주세요.");
        }else if(!password.equals(schedule.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다.");
        }

        schedule.updateTask(task, updatedAt);

        return new ScheduleResponseDto(schedule);
    }

    // Update -> 수정 (작성자만 수정)
    @Override
    public ScheduleResponseDto updateAuthor(Long id, String author, String task, String password, String updatedAt) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        //NPE방지
        if(schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + "는 존재하지 않는 아이디 입니다.");
        }

        //필수 값 검증
        if(author == null || task != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"작성자와, 할일을 입력해주세요.");
        }

        // 비밀번호 검증
        if(password == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"비밀번호를 입력해주세요.");
        }else if(!password.equals(schedule.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다.");
        }

        schedule.updateAuthor(author, updatedAt);

        return new ScheduleResponseDto(schedule);
    }

    //Delete -> 삭제
    @Override
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        //NPE 검증
        if(schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + "는 존재하지 않는 아이디 입니다.");
        }

        scheduleRepository.deleteSchedule(id);
    }

}
