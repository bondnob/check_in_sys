package com.niuniu.mapper;

import com.niuniu.dto.course.CourseMemberResponse;
import com.niuniu.entity.CourseMember;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseMemberMapper {

    @Insert("insert into course_members(course_id, student_id) values(#{courseId}, #{studentId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CourseMember member);

    @Select("select * from course_members where course_id = #{courseId} and student_id = #{studentId}")
    CourseMember findByCourseAndStudent(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    @Select("select count(1) from course_members where course_id = #{courseId}")
    int countByCourseId(@Param("courseId") Integer courseId);

    @Select("""
            select cm.id as member_id, s.id as student_id, s.user_number as student_number, s.name as student_name,
                   s.avatar, s.phone, cm.joined_at
            from course_members cm
            join students s on cm.student_id = s.id
            where cm.course_id = #{courseId}
            order by cm.id desc
            limit #{offset}, #{pageSize}
            """)
    List<CourseMemberResponse> listMembers(@Param("courseId") Integer courseId,
                                           @Param("offset") Integer offset,
                                           @Param("pageSize") Integer pageSize);

    @Select("select count(1) from course_members where course_id = #{courseId}")
    long countMembers(@Param("courseId") Integer courseId);

    @Delete("delete from course_members where course_id = #{courseId} and student_id = #{studentId}")
    int delete(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
}
