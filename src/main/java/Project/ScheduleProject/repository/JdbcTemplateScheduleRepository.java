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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        return new ScheduleResponseDto(key.longValue(), schedule.getAuthor(), schedule.getTask(), schedule.getCreatedAt(), schedule.getUpdatedAt());
    }

    // Read -> 다건 조회 (전체 조회)    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return jdbcTemplate.query("SELECT * FROM schedule", scheduleRowMapper());
    }
    private RowMapper<ScheduleResponseDto> scheduleRowMapper(){
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("author"),
                        rs.getString("task"),
                        rs.getTimestamp("createdDate").toLocalDateTime(),
                        rs.getTimestamp("updatedDate").toLocalDateTime()
                );
            }
        };
    }

    // Read -> 단건 조회 (아이디)
    @Override
    public Optional<Schedule> findScheduleById(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule where id = ?", scheduleRowMapperV2(), id);
        return result.stream().findAny();
    }

    //Update -> 수정 (전체 수정)
    @Override
    public int updateSchedule(Long id, String author, String task){
        String sql = "UPDATE schedule SET author = ?, task = ?, updatedDate = ? WHERE id = ?";
        return jdbcTemplate.update(sql, author, task, LocalDateTime.now(), id);
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

    // Update -> 수정 (일정만 수정)
    @Override
    public int updateTask(Long id, String task) {
        return jdbcTemplate.update("UPDATE schedule set task = ? WHERE id = ?", task, id);
    }

    // Update -> 수정 (작성자만 수정)
    @Override
    public int updateAuthor(Long id, String author) {
        return jdbcTemplate.update("UPDATE schedule set author = ? WHERE id = ?", author, id);
    }

    //Delete -> 삭제
    @Override
    public int deleteSchedule(Long id) {
        return jdbcTemplate.update("DELETE from schedule WHERE id = ?", id);
    }

    //optional 검증
    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?",scheduleRowMapperV2(),id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }
}
