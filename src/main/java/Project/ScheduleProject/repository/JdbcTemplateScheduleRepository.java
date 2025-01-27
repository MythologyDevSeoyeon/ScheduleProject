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
    public List<ScheduleResponseDto> findSchedules(Long id, String updatedAt, String author) {
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
                sql.append(" AND updatedDate BETWEEN ? AND ?");
                params.add(date.atStartOfDay()); // 하루의 시작
                params.add(date.atTime(23, 59, 59)); // 하루의 끝
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

    private RowMapper<ScheduleResponseDto> scheduleRowMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("author"),
                        rs.getString("task"),
                        ScheduleResponseDto.formatDate(rs.getTimestamp("createdDate").toLocalDateTime()),
                        ScheduleResponseDto.formatDate(rs.getTimestamp("updatedDate").toLocalDateTime())
                );
            }
        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2() {
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


//    private RowMapper<ScheduleResponseDto> scheduleRowMapperV3(){
//        return new RowMapper<ScheduleResponseDto>() {
//            @Override
//            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
//                return new ScheduleResponseDto(
//                        rs.getLong("id"),
//                        rs.getString("author"),
//                        rs.getString("task"),
//                        ScheduleResponseDto.formatDate(rs.getTimestamp("createdDate").toLocalDateTime()),
//                        ScheduleResponseDto.formatDate(rs.getTimestamp("updatedDate").toLocalDateTime())
//                );
//            }
//        };
//    }


    // Update -> 수정 (전체 수정)
    @Override
    public int updateSchedule(Long id, String author, String task) {
        String sql = "UPDATE schedule SET author = ?, task = ?, updatedDate = ? WHERE id = ?";
        String updatedDate = ScheduleResponseDto.formatDate(LocalDateTime.now());
        return jdbcTemplate.update(sql, author, task, updatedDate, id);
    }

    // Update -> 수정 (일정만 수정)
    @Override
    public int updateTask(Long id, String task) {
        String sql = "UPDATE schedule SET task = ?, updatedDate = ? WHERE id = ?";
        String updatedDate = ScheduleResponseDto.formatDate(LocalDateTime.now());
        return jdbcTemplate.update(sql, task, updatedDate, id);
    }

    // Update -> 수정 (작성자만 수정)
    @Override
    public int updateAuthor(Long id, String author) {
        String sql = "UPDATE schedule SET author = ?, updatedDate = ? WHERE id = ?";
        String updatedDate = ScheduleResponseDto.formatDate(LocalDateTime.now());
        return jdbcTemplate.update(sql, author, updatedDate, id);
    }

    //Delete -> 삭제
    @Override
    public int deleteSchedule(Long id) {
        return jdbcTemplate.update("DELETE from schedule WHERE id = ?", id);
    }

    //optional 검증
    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", scheduleRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }
}
