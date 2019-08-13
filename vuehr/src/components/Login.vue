<template >
  <el-form ref="loginForm" :model="form" :rules="rules" class="login-container" label-position="left"
           label-width="0px" >
    <h3 class="login_title">系统登录</h3>
    <el-form-item prop="username">
      <el-input type="text" v-model="form.username"
                auto-complete="off" placeholder="请输入账号"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="form.password"
                auto-complete="off" placeholder="请输入密码"></el-input>
    </el-form-item>
    <el-checkbox class="login_remember" v-model="checked"
                 label-position="left">记住密码</el-checkbox>
    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 100%"  :loading="loading" @click="submitClick" >
        <span v-if="!loading">登录</span>
        <span v-else>登陆中...</span>
      </el-button>
      <br><br>
      <el-button type="primary" style="width: 100%" @click="resetClick">重置</el-button>
      <br>
      <el-button type="text"  @click="userAdd">点击注册</el-button>
    </el-form-item>
    <register-user  ref="registerUser"  @userAddSuccess="resetClick"></register-user>
  </el-form>
</template>

<script>
  import RegisterUser from './system/components/RegisterUser' //导入注册组件

  export default{
    name: 'loginForm',
    components: {RegisterUser},
    props:{
      accountRules: {
        type: Array,
        default: () =>{
          return [
            {required: true, message: '账号不能为空',trigger: 'blur'}
          ]
        }
      },
      checkPassRules: {
        type: Array,
        default: () =>{
          return [
            {required: true, message: '密码不能为空',trigger: 'blur'}
          ]
        }
      }
    },
    data(){
      return{
        form: {
          username: 'admin',
          password: '123'
        },
        checked: true,
        loading: false
      }
    },
    computed: {
      rules(){
        return {
          username: this.accountRules,
          password: this.checkPassRules
        }
      }
    },
    methods: {
      //重置
      resetClick: function () {
        var _this = this;
        this.loading = false;
        this.form.username = "";
        this.form.password="";
      },
      //登陆
      submitClick () {
        var _this = this;
        this.$refs.loginForm.validate((valid) => {
          if(valid){
            this.loading = true;
             this.postRequest('/login', {
               username: this.form.username,
               password: this.form.password
             }).then(resp=> {
               if (resp && resp.status == 200) {
                 var data = resp.data;
                 _this.$store.commit('login', data.obj);
                 var path = _this.$route.query.redirect;
                 _this.$router
                   .replace({path: path == '/' || path == undefined ? '/home' : path});
               }
               else
                 this.loading = false;
             });
          }
        })
      },
      //用户注册
      userAdd (){
        this.$refs.registerUser.init();
      }
    }
  }
</script>
<style>
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #ad6cea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
  .login_remember {
    margin: 0px 0px 35px 0px;
    text-align: left;
  }
</style>
