package org.hrsys.service;

import org.hrsys.bean.Hr;
import org.hrsys.common.HrUtils;
import org.hrsys.mapper.HrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
@Service
@Transactional
public class HrService implements UserDetailsService {

    @Autowired
    HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(s);
        if (hr == null) {
            throw new UsernameNotFoundException("用户名不对");
        }
        return hr;
    }

    //系统管理人员注册
    public int hrReg(Hr hr) {
        //如果用户名存在，返回错误
        if (hrMapper.loadUserByUsername(hr.getUsername()) != null) {
            return -1;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(hr.getPassword());
        return hrMapper.hrReg(hr.getName(),hr.getUsername(),hr.getTelephone(),hr.getPhone(),hr.getAddress(),encode);
    }

    public List<Hr> getHrsByKeywords(String keywords) {
        return hrMapper.getHrsByKeywords(keywords);
    }

    public int updateHr(Hr hr) {
        return hrMapper.updateHr(hr);
    }

    public int updateHrRoles(Long hrId, Long[] rids) {
        int i = hrMapper.deleteRoleByHrId(hrId);
        return hrMapper.addRolesForHr(hrId, rids);
    }

    public Hr getHrById(Long hrId) {
        return hrMapper.getHrById(hrId);
    }

    public int deleteHr(Long hrId) {
        return hrMapper.deleteHr(hrId);
    }

    public List<Hr> getAllHrExceptAdmin() {
        return hrMapper.getAllHr(HrUtils.getCurrentHr().getId());
    }
    public List<Hr> getAllHr() {
        return hrMapper.getAllHr(null);
    }
}
