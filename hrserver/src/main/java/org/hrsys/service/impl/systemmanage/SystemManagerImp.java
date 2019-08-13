package org.hrsys.service.impl.systemmanage;

import org.hrsys.service.api.SystemManage.ManagerManageService;
import org.hrsys.bean.Hr;
import org.hrsys.dao.systemmanage.SystemManagerDao;
import org.hrsys.dto.systemmanage.SystemManagerDto;
import org.hrsys.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author jjma
 * @Date Created in 2019/8/1.
 */

@Service
public class SystemManagerImp implements ManagerManageService {

    @Autowired
    private DozerConverter dozerConverter;

    @Autowired
    private SystemManagerDao systemManagerDao;

    @Override
    public void addSystemManager(final Hr hr){
        final SystemManagerDto systemManagerDto = dozerConverter.convert(hr,SystemManagerDto.class);
        systemManagerDao.addSystemManager(systemManagerDto);
    }
}
