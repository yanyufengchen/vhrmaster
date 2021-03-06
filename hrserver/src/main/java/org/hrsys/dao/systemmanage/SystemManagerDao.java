package org.hrsys.dao.systemmanage;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.hrsys.databasevarible.systemmanage.SystemManagerDto;
import org.springframework.stereotype.Component;

/**
 * @Author jjma
 * @Date Created in 2019/08/01.
 */

@Component
public interface SystemManagerDao {

    /**
     * 新增公共服务
     * @param systemManagerDto 系统操作员
     */
    @Insert("insert into hr(name,phone,telephone,address,enabled,username,password)" +
            "values(#{name},#{phone},#{telephone},#{address},#{enabled},#{username},#{password})")
    void addSystemManager(SystemManagerDto systemManagerDto);

}
