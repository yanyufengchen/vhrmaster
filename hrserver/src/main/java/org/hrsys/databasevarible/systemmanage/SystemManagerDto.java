package org.hrsys.databasevarible.systemmanage;

import lombok.Data;

/**
 * @Author jjma
 * @Date Created in 2019/8/1.
 */

@Data
public class SystemManagerDto {

    /*
     * 主键
     */
    private int id;

    /*
     * 姓名
     */
    private String name;

    /*
     * 手机号码
     */
    private String phone;

    /*
     * 电话号码
     */
    private String telephone;

    /*
     * 联系地址
     */
    private String address;

    private int enabled;

    /*
     * 用户名
     */
    private String username;

    /*
     * 密码
     */
    private String password;

    private String userface;

    private String remark;
}

