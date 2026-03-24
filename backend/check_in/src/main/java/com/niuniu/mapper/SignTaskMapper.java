package com.niuniu.mapper;

import com.niuniu.entity.SignTask;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SignTaskMapper {

    @Insert("""
            insert into sign_tasks(course_id, title, start_time, end_time, sign_type, qr_code, latitude, longitude, radius, late_time)
            values(#{courseId}, #{title}, #{startTime}, #{endTime}, #{signType}, #{qrCode}, #{latitude}, #{longitude}, #{radius}, #{lateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SignTask task);

    @Select("select * from sign_tasks where id = #{id}")
    SignTask findById(@Param("id") Integer id);

    @Select("""
            select * from sign_tasks
            where course_id = #{courseId}
            order by id desc
            limit #{offset}, #{pageSize}
            """)
    List<SignTask> listByCourse(@Param("courseId") Integer courseId,
                                @Param("offset") Integer offset,
                                @Param("pageSize") Integer pageSize);

    @Select("select count(1) from sign_tasks where course_id = #{courseId}")
    long countByCourse(@Param("courseId") Integer courseId);

    @Update("""
            update sign_tasks
            set title = #{title},
                start_time = #{startTime},
                end_time = #{endTime},
                sign_type = #{signType},
                qr_code = #{qrCode},
                latitude = #{latitude},
                longitude = #{longitude},
                radius = #{radius},
                late_time = #{lateTime}
            where id = #{id}
            """)
    int update(SignTask task);

    @Delete("delete from sign_tasks where id = #{id}")
    int delete(@Param("id") Integer id);

    @Select("""
            select * from sign_tasks
            where course_id = #{courseId}
              and start_time <= now()
              and end_time >= now()
            order by id desc
            limit 1
            """)
    SignTask activeByCourse(@Param("courseId") Integer courseId);

    @Select("select count(1) from sign_tasks where course_id = #{courseId}")
    int countTasksByCourse(@Param("courseId") Integer courseId);
}
