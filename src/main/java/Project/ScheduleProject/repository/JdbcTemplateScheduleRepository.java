package Project.ScheduleProject.repository;

import Project.ScheduleProject.dto.ScheduleResponseDto;
import Project.ScheduleProject.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //create
    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("author", schedule.getAuthor());
        parameters.put("password", schedule.getPassword());
        parameters.put("task", schedule.getTask());
        parameters.put("createdDate", schedule.getCreatedAt());
        parameters.put("updatedDate", schedule.getUpdatedAt());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(
                key.longValue(),
                schedule.getAuthor(),
                schedule.getTask(),
                ScheduleResponseDto.formatDate(schedule.getCreatedAt()),
                ScheduleResponseDto.formatDate(schedule.getUpdatedAt())
        );
    }

    // Read -> 조회 (전체, 아이디, 수정 날짜, 작성자)
    @Override
    public List<Schedule> findSchedules(Long id, String updatedAt, String author) {
        StringBuilder sql = new StringBuilder("SELECT * FROM schedule WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // ID 조건
        if (id != null) {
            sql.append(" AND id = ?");
            params.add(id);
        }

        // 날짜 조건
        if (updatedAt != null && !updatedAt.trim().isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(updatedAt); // yyyy-MM-dd 형식
                sql.append(" AND DATE(updatedDate) = ?");
                params.add(date);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("잘못된 날짜 형식입니다. 'yyyy-MM-dd' 형식으로 입력해주세요.");
            }
        }

        // 작성자 조건
        if (author != null && !author.trim().isEmpty()) {
            sql.append(" AND author = ?");
            params.add(author);
        }

        // 정렬 조건
        sql.append(" ORDER BY updatedDate DESC");

        // SQL 실행
        return jdbcTemplate.query(sql.toString(), scheduleRowMapper(), params.toArray());
    }


    private RowMapper<Schedule> scheduleRowMapper() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("author"),
                        rs.getString("password"),
                        rs.getString("task"),
                        rs.getTimestamp("createdDate").toLocalDateTime(),
                        rs.getTimestamp("updatedDate").toLocalDateTime()
                );
            }
        };
    }

    // Update -> 수정 (전체 , 일정, 작성자 수정)
    @Override
    public int updateSchedule(Long id, String author, String task) {
        StringBuilder sql = new StringBuilder("UPDATE schedule SET updatedDate = ?");
        List<Object> param = new ArrayList<>();

        // 수정 시간 추가
        String updatedDate = ScheduleResponseDto.formatDate(LocalDateTime.now());
        param.add(updatedDate);

        // 일정 수정
        if(task != null && !task.trim().isEmpty()){
            sql.append(", task = ?");
            param.add(task);
        }

        //작성자 수정
        if(author != null && !author.trim().isEmpty()){
            sql.append(", author = ?");
            param.add(author);
        }

        sql.append(" WHERE id = ?");
        param.add(id);

        return jdbcTemplate.update(sql.toString(), param.toArray());
    }

    //Delete -> 삭제
    @Override
    public int deleteSchedule(Long id) {
        return jdbcTemplate.update("DELETE from schedule WHERE id = ?", id);
    }

    //optional 검증
    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", scheduleRowMapper(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, id +  ", 이 아이디는 존재하지 않습니다."));
    }
}
