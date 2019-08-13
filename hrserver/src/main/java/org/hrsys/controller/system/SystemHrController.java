package org.hrsys.controller.system;

import org.hrsys.service.api.SystemManage.ManagerManageService;
import org.hrsys.bean.Hr;
import org.hrsys.bean.RespBean;
import org.hrsys.service.HrService;
import org.hrsys.vo.SysManQueryVo;
import org.hrsys.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by sang on 2018/1/2.
 * Edited by majiajia on 2019/7/31
 * 系统管理
 */
@RestController
@RequestMapping("/system/hr")
public class SystemHrController {

    @Autowired
    private HrService hrService;

    @Autowired
    private ManagerManageService managerManage;

    @RequestMapping("/id/{hrId}")
    public Hr getHrById(@PathVariable Long hrId) {
        return hrService.getHrById(hrId);
    }

    @RequestMapping(value = "/{hrId}", method = RequestMethod.DELETE)
    public RespBean deleteHr(@PathVariable Long hrId) {
        if (hrService.deleteHr(hrId) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public RespBean updateHr(Hr hr) {
        if (hrService.updateHr(hr) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @RequestMapping(value = "/roles", method = RequestMethod.PUT)
    public RespBean updateHrRoles(Long hrId, Long[] rids) {
        if (hrService.updateHrRoles(hrId, rids) == rids.length) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    /**系统管理员hr查询
    @RequestMapping("/{keywords}")
    public List<Hr> getHrsByKeywords(@PathVariable(required = false) SysManQueryVo SysManQueryVo) {
        List<Hr> hrs = hrService.getHrsByKeywords(SysManQueryVo.getHrName());
        return hrs;
    }*/

    @RequestMapping("/{keywords}")
    public ResponseEntity<Result> getHrByKeywords(@Valid SysManQueryVo SysManQueryVo){
        return ResponseEntity.ok(Result.success(hrService.getHrsByKeywords(SysManQueryVo)));
    }

    /**
     **系统管理员注册
     **/
    @RequestMapping(value = "/register/SysMan", method = RequestMethod.POST)
    public RespBean addSystemManager(Hr hr) {
        if(hr.getActualpassword() != hr.getPassword()){
            return RespBean.error("两次密码输入不同，请确认重新输入！");
        }
        managerManage.addSystemManager(hr);
        return RespBean.ok("注册成功!");
    }

}
