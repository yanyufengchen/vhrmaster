<template>
  <el-form :model="emp" :rules="rules" ref="addSysMamForm" style="margin: 0px;padding: 0px;">
    <div style="text-align: left">
      <el-dialog
        :title="dialogTitle"
        :close-on-click-modal="false"
        :visible.sync="dialogVisible"
        width="30%">
          <el-form-item label="用户名:" prop="name">
                <el-input prefix-icon="el-icon-edit" v-model="emp.name" size="mini" style="width: 200px"
                          placeholder="请输入用户名"></el-input>
           </el-form-item>
           <el-form-item label="登陆名:" prop="username">
              <el-input prefix-icon="el-icon-edit" v-model="emp.username" size="mini" style="width: 200px"
                    placeholder="请输入登陆名"></el-input>
            </el-form-item>
            <el-form-item label="联系地址:" prop="address">
                <el-input prefix-icon="el-icon-edit" v-model="emp.address" size="mini" style="width: 200px"
                          placeholder="请输入联系地址..."></el-input>
            </el-form-item>
            <el-form-item label="手机号码:" prop="phone">
                <el-input prefix-icon="el-icon-phone" v-model="emp.phone" size="mini" style="width: 200px"
                          placeholder="请输入手机号码..."></el-input>
            </el-form-item>
            <el-form-item label="电话号码:" prop="telephone">
                 <el-input prefix-icon="el-icon-phone" v-model="emp.telephone" size="mini" style="width: 200px"
                    placeholder="请输入电话号码..."></el-input>
             </el-form-item>
           <el-form-item label="登陆密码:" prop="password">
             <el-input prefix-icon="el-icon-edit" v-model="emp.password" size="mini" style="width: 200px"
                        placeholder="请输入登陆密码..."></el-input>
             </el-form-item>
              <el-form-item label="确认密码:" prop="actualpassword">
                <el-input prefix-icon="el-icon-edit" v-model="emp.actualpassword" size="mini" style="width: 200px"
                    placeholder="请再次输入密码..."></el-input>
              </el-form-item>
              <el-form-item label="备注:" >
                  <el-input v-model="emp.annotation" type="textarea" size="mini" style="width: 200px"/>
              </el-form-item>
        <span slot="footer" class="dialog-footer">
          <el-button size="mini" @click="cancelEidt">取 消</el-button>
          <el-button size="mini" type="primary" @click="addEmp('addSysMamForm')">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </el-form>
</template>
<script>
  export default {
    data() {
      return {
        dialogVisible: false,
        dialogTitle: '系统管理员注册',
        close: false,
        emp: {
          name: '',
          username:'',
          phone: '',
          address: '',
          telephone: '',
          password:'',
          annotation:'',
          actualpassword:''
        },
        checkList:[],
        rules: {
          name: [{required: true, message: '必填:姓名', trigger: 'blur'}],
          phone: [{pattern:  /^[0-9]+$/, message: '手机号码格式不正确，必须全为数字', trigger: 'blur'},
                  {maxlength:11,message:'号码长度最多只能为11位',trigger:'blur'}],
          address: [{required: true, message: '必填:联系地址', trigger: 'blur'}],
          username: [{required: true, message: '必填:登陆名', trigger: 'blur'}],
          telephone: [{pattern:  /^[0-9]+$/, message: '电话号码格式不正确，必须全为数字', trigger: 'blur'},
                  {maxlength:11,message:'号码长度最多只能为11位',trigger:'blur'}],
          password: [{required: true, message: '必填:联系地址', trigger: 'blur'}],
          actualpassword: [{required: true, message: '必填:请再次输入密码', trigger: 'blur'}],
        }
      };
    },
    methods:{
      init(){
        this.dialogVisible = true;
      },
      addEmp(addSysMamForm){
        var _this = this;
        this.$refs[addSysMamForm].validate((valid) => {
          if (valid) {
              //添加
              this.postRequest("/system/hr/register/SysMan", this.emp).then(resp=> {
                if (resp && resp.status == 200) {
                  _this.dialogVisible = false;
                  _this.emptyEmpData();
                }
              })
            } else {
            return false;
          }
        });
      },
      cancelEidt(){
        this.dialogVisible = false;
        this.emptyEmpData();
      },

      //注册界面刷新
      emptyEmpData(){
        this.emp ={
          name: '',
          username:'',
          phone: '',
          address: '',
          telephone: '',
          password:'',
          annotation:'',
          actualpassword: '',
        }
      }
    },
  }
 </script>

<style scoped>

</style>
