package com.niuniu.mapper;

import com.niuniu.dto.course.CourseDetailResponse;
import com.niuniu.dto.course.CourseResponse;
import com.niuniu.entity.Course;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CourseMapper {

    @Insert("""
            insert into courses(teacher_id, course_name, invite_code,location, class_name, stu_number, term)
            values(#{teacherId}, #{courseName}, #{inviteCode}, #{location}, #{className}, #{stuNumber}, #{term})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Course course);

    @Select("select * from courses where id = #{id}")
    Course findById(@Param("id") Integer id);

    @Select("select * from courses where invite_code = #{inviteCode}")
    Course findByInviteCode(@Param("inviteCode") String inviteCode);

    @Select("""
            <script>
            select c.id, c.course_name, c.class_name, c.term, c.location, c.invite_code, c.stu_number,
                   c.created_at, count(cm.id) as actual_member_count
            from courses c
            left join course_members cm on c.id = cm.course_id
            where c.teacher_id = #{teacherId}
            <if test="keyword != null and keyword != ''">
              and (c.course_name like concat('%', #{keyword}, '%') or c.class_name like concat('%', #{keyword}, '%'))
            </if>
            group by c.id
            order by c.id desc
            limit #{offset}, #{pageSize}
            </script>
            """)
    List<CourseResponse> listTeaching(@Param("teacherId") String teacherId,
                                      @Param("keyword") String keyword,
                                      @Param("offset") Integer offset,
                                      @Param("pageSize") Integer pageSize);

    @Select("""
            <script>
            select count(1)
            from courses c
            where c.teacher_id = #{teacherId}
            <if test="keyword != null and keyword != ''">
              and (c.course_name like concat('%', #{keyword}, '%') or c.class_name like concat('%', #{keyword}, '%'))
            </if>
            </script>
            """)
    long countTeaching(@Param("teacherId") String teacherId, @Param("keyword") String keyword);

    @Select("""
            select c.id, c.course_name, c.class_name, c.term, c.location, c.created_at,
                   t.name as teacher_name, cm.joined_at
            from course_members cm
            join courses c on cm.course_id = c.id
            join teachers t on c.teacher_id = t.user_number
            where cm.student_id = #{studentId}
            order by cm.id desc
            limit #{offset}, #{pageSize}
            """)
    List<CourseResponse> listJoined(@Param("studentId") Integer studentId,
                                    @Param("offset") Integer offset,
                                    @Param("pageSize") Integer pageSize);

    @Select("select count(1) from course_members where student_id = #{studentId}")
    long countJoined(@Param("studentId") Integer studentId);

    @Select("""
            select c.id, c.course_name, c.class_name, c.term, c.location, c.invite_code, c.stu_number,
                   c.teacher_id, t.name as teacher_name, c.created_at,
                   (select count(1) from course_members cm where cm.course_id = c.id) as member_count
            from courses c
            join teachers t on c.teacher_id = t.user_number
            where c.id = #{courseId}
            """)
    CourseDetailResponse detail(@Param("courseId") Integer courseId);

    @Update("""
            update courses
            set course_name = #{courseName},
                class_name = #{className},
                term = #{term},
                location = #{location},
                stu_number = #{stuNumber}
            where id = #{id}
            """)
    int update(Course course);

    @Delete("delete from courses where id = #{id}")
    int delete(@Param("id") Integer id);
}
