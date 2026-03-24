package com.niuniu.mapper;

import com.niuniu.dto.record.SignRecordResponse;
import com.niuniu.entity.SignRecord;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SignRecordMapper {

    @Insert("""
            insert into sign_records(task_id, student_id, sign_time, status, location, latitude, longitude)
            values(#{taskId}, #{studentId}, #{signTime}, #{status}, #{location}, #{latitude}, #{longitude})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SignRecord record);

    @Select("select * from sign_records where task_id = #{taskId} and student_id = #{studentId}")
    SignRecord findByTaskAndStudent(@Param("taskId") Integer taskId, @Param("studentId") Integer studentId);

    @Select("select count(1) from sign_records where task_id = #{taskId}")
    int countByTaskId(@Param("taskId") Integer taskId);

    @Select("select count(1) from sign_records where task_id = #{taskId} and status = 2")
    int countLateByTaskId(@Param("taskId") Integer taskId);

    @Select("""
            <script>
            select sr.id as record_id, sr.task_id, s.id as student_id, s.user_number as student_number, s.name as student_name,
                   sr.sign_time, sr.status, sr.location, sr.latitude, sr.longitude
            from sign_records sr
            join students s on sr.student_id = s.id
            where sr.task_id = #{taskId}
            <if test="status != null">
              and sr.status = #{status}
            </if>
            <if test="keyword != null and keyword != ''">
              and (s.name like concat('%', #{keyword}, '%') or s.user_number like concat('%', #{keyword}, '%'))
            </if>
            order by sr.id desc
            limit #{offset}, #{pageSize}
            </script>
            """)
    List<SignRecordResponse> listByTask(@Param("taskId") Integer taskId,
                                        @Param("status") Integer status,
                                        @Param("keyword") String keyword,
                                        @Param("offset") Integer offset,
                                        @Param("pageSize") Integer pageSize);

    @Select("""
            <script>
            select count(1)
            from sign_records sr
            join students s on sr.student_id = s.id
            where sr.task_id = #{taskId}
            <if test="status != null">
              and sr.status = #{status}
            </if>
            <if test="keyword != null and keyword != ''">
              and (s.name like concat('%', #{keyword}, '%') or s.user_number like concat('%', #{keyword}, '%'))
            </if>
            </script>
            """)
    long countByTask(@Param("taskId") Integer taskId, @Param("status") Integer status, @Param("keyword") String keyword);

    @Select("""
            select null as record_id, null as task_id, s.id as student_id, s.user_number as student_number, s.name as student_name
            from course_members cm
            join students s on cm.student_id = s.id
            join sign_tasks st on st.course_id = cm.course_id
            left join sign_records sr on sr.task_id = st.id and sr.student_id = s.id
            where st.id = #{taskId} and sr.id is null
            order by s.id desc
            limit #{offset}, #{pageSize}
            """)
    List<SignRecordResponse> listUnsigned(@Param("taskId") Integer taskId,
                                          @Param("offset") Integer offset,
                                          @Param("pageSize") Integer pageSize);

    @Select("""
            select count(1)
            from course_members cm
            join sign_tasks st on st.course_id = cm.course_id
            left join sign_records sr on sr.task_id = st.id and sr.student_id = cm.student_id
            where st.id = #{taskId} and sr.id is null
            """)
    long countUnsigned(@Param("taskId") Integer taskId);

    @Select("""
            select sr.id as record_id, sr.task_id, st.course_id, c.course_name, st.title,
                   sr.sign_time, sr.status, sr.location, sr.latitude, sr.longitude
            from sign_records sr
            join sign_tasks st on sr.task_id = st.id
            join courses c on st.course_id = c.id
            where sr.student_id = #{studentId}
              and (#{courseId} is null or st.course_id = #{courseId})
            order by sr.id desc
            limit #{offset}, #{pageSize}
            """)
    List<SignRecordResponse> listMine(@Param("studentId") Integer studentId,
                                      @Param("courseId") Integer courseId,
                                      @Param("offset") Integer offset,
                                      @Param("pageSize") Integer pageSize);

    @Select("""
            select count(1)
            from sign_records sr
            join sign_tasks st on sr.task_id = st.id
            where sr.student_id = #{studentId}
              and (#{courseId} is null or st.course_id = #{courseId})
            """)
    long countMine(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);

    @Select("""
            select count(1)
            from sign_records sr
            join sign_tasks st on sr.task_id = st.id
            where st.course_id = #{courseId}
            """)
    int countByCourseId(@Param("courseId") Integer courseId);
}
