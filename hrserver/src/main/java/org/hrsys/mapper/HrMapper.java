package org.hrsys.mapper;

import org.apache.ibatis.annotations.Param;
import org.hrsys.bean.Hr;
import org.hrsys.bean.Role;

import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
public interface HrMapper {
    Hr loadUserByUsername(String username);

    List<Role> getRolesByHrId(Long id);

    int hrReg(@Param("name") String name,@Param("username") String username, @Param("telephone") String telephone,
              @Param("phone") String phone,@Param("address") String address, @Param("password") String password);

    List<Hr> getHrsByKeywords(@Param("keywords") String keywords);

    int updateHr(Hr hr);

    int deleteRoleByHrId(Long hrId);

    int addRolesForHr(@Param("hrId") Long hrId, @Param("rids") Long[] rids);

    Hr getHrById(Long hrId);

    int deleteHr(Long hrId);

    List<Hr> getAllHr(@Param("currentId") Long currentId);
}
