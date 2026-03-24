package com.niuniu.mapper;

import com.niuniu.dto.statistics.StudentAttendanceResponse;
import com.niuniu.entity.Student;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentMapper {

    @Select("select * from students where id = #{id}")
    Student findById(@Param("id") Integer id);

    @Select("select * from students where openid = #{openid}")
    Student findByOpenid(@Param("openid") String openid);

    @Select("select * from students where user_number = #{userNumber} limit 1")
    Student findByUserNumber(@Param("userNumber") String userNumber);

    @Select("select * from students where phone = #{phone} limit 1")
    Student findByPhone(@Param("phone") String phone);

    @Insert("""
            insert into students(openid, name, user_number, avatar, phone)
            values(#{openid}, #{name}, #{userNumber}, #{avatar}, #{phone})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Student student);

    @Update("""
            update students
            set openid = #{openid}, name = #{name}, avatar = #{avatar}, phone = #{phone}
            where id = #{id}
            """)
    int update(Student student);

    @Select("""
            select s.id as student_id, s.user_number as student_number, s.name as student_name,
                   count(sr.id) as signed_count,
                   (select count(1) from sign_tasks st where st.course_id = #{courseId}) - count(sr.id) as unsigned_count,
                   round(ifnull(count(sr.id) * 100.0 / nullif((select count(1) from sign_tasks st where st.course_id = #{courseId}), 0), 0), 2) as attendance_rate
            from course_members cm
            join students s on cm.student_id = s.id
            left join sign_tasks st on st.course_id = cm.course_id
            left join sign_records sr on sr.task_id = st.id and sr.student_id = s.id
            where cm.course_id = #{courseId}
            group by s.id
            order by s.id desc
            limit #{offset}, #{pageSize}
            """)
    List<StudentAttendanceResponse> studentAttendance(@Param("courseId") Integer courseId,
                                                      @Param("offset") Integer offset,
                                                      @Param("pageSize") Integer pageSize);
}
