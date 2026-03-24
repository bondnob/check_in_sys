package com.niuniu.mapper;

import com.niuniu.entity.Teacher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TeacherMapper {

    @Select("select * from teachers where id = #{id}")
    Teacher findById(@Param("id") Integer id);

    @Select("select * from teachers where openid = #{openid}")
    Teacher findByOpenid(@Param("openid") String openid);

    @Select("select * from teachers where user_number = #{userNumber} limit 1")
    Teacher findByUserNumber(@Param("userNumber") String userNumber);

    @Select("select * from teachers where phone = #{phone} limit 1")
    Teacher findByPhone(@Param("phone") String phone);

    @Insert("""
            insert into teachers(openid, name, user_number, avatar, phone)
            values(#{openid}, #{name}, #{userNumber}, #{avatar}, #{phone})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Teacher teacher);

    @Update("""
            update teachers
            set openid = #{openid}, name = #{name}, avatar = #{avatar}, phone = #{phone}
            where id = #{id}
            """)
    int update(Teacher teacher);
}
