<template>
   <div style="margin-top: 10px" v-loading="fullloading">
    <!--第一栏搜索框-->
    <div style="margin-bottom: 10px; display: flex;justify-content: left;align-items: center">
      <el-input
        placeholder="默认展示部分用户，可以通过用户名搜索更多用户..."
        prefix-icon="el-icon-search"
        size="small"
        style="width: 400px;margin-right: 10px"
        v-model="keywords">
      </el-input>
      <el-button size="small" type="primary" icon="el-icon-search" @click="searchClick">搜索</el-button>
      <el-button size="small"  icon="el-icon-refresh" @click="resetClick">重置</el-button>
      <el-button size="small" icon="el-icon-plus" style="margin-right: 5px" @click="addSysMan">添加管理员</el-button>
    </div>
    <!--第二块区域，显示hr人员列表-->
    <div style="display: flex;justify-content: space-around;flex-wrap: wrap;text-align: left">
      <el-card style="width: 350px;margin-bottom: 20px" v-for="(item,index) in hrs" :key="item.id"
               v-loading="cardLoading[index]">
        <div slot="header" class="clearfix">
          <span>{{item.name}}</span>
          <el-button type="text"
                     style="color: #f6061b;margin: 0px;float: right; padding: 3px 0;width: 15px;height:15px"
                     icon="el-icon-delete" @click="deleteHr(item.id)"></el-button>
        </div>
        <div>
          <div style="width: 100%;display: flex;justify-content: center">
            <img :src="item.userface" alt="item.name" style="width: 70px;height: 70px;border-radius: 70px">
          </div>
          <div style="margin-top: 20px">
            <div><span class="user-info">用户名:{{item.name}}</span></div>
            <div><span class="user-info">手机号码:{{item.phone}}</span></div>
            <div><span class="user-info">电话号码:{{item.telephone}}</span></div>
            <div><span class="user-info">地址:{{item.address}}</span></div>
            <div class="user-info" style="display: flex;align-items: center;margin-bottom: 3px">
              用户状态:
              <el-switch
                style="display: inline;margin-left: 5px"
                v-model="item.enabled"
                active-color="#13ce66"
                inactive-color="#aaaaaa"
                active-text="启用"
                :key="item.id"
                @change="switchChange(item.enabled,item.id,index)"
                inactive-text="禁用">
              </el-switch>
            </div>
            <div class="user-info">
              用户角色:
              <el-tag
                v-for="role in item.roles"
                :key="role.id"
                type="success"
                size="mini"
                style="margin-right: 5px"
                :disable-transitions="false">{{role.nameZh}}
              </el-tag>
              <el-popover
                v-loading="eploading[index]"
                placement="right"
                title="角色列表"
                width="200"
                @hide="updateHrRoles(item.id,index)"
                :key="item.id"
                trigger="click">
                <el-select v-model="selRoles" multiple placeholder="请选择角色">
                  <el-option
                    v-for="ar in allRoles"
                    :key="ar.id"
                    :label="ar.nameZh"
                    :value="ar.id">
                  </el-option>
                </el-select>
                <el-button type="text" icon="el-icon-more" style="color: #09c0f6;padding-top: 0px" slot="reference"
                           @click="loadSelRoles(item.roles,index)" :disabled="moreBtnState"></el-button>
<!--                <i class="el-icon-more" style="color: #09c0f6;cursor: pointer" slot="reference"
                   @click="loadSelRoles(item.roles,index)" disabled="true"></i>-->
              </el-popover>
            </div>
            <div><span class="user-info">备注:{{item.remark}}</span></div>
          </div>
        </div>
      </el-card>
    </div>
    <!--第三块区域，设置分页-->
    <div style="margin-top: 10px;display: flex;justify-content: left">
      <el-pagination
        @size-change="pageSizeChange"
        @current-change="pageChange"
        :current-page="page"
        :page-sizes="[4,8,16,24]"
        :page-size="rows"
        layout="total, prev, pager, next,sizes,jumper"
        :total="totalRows">
      </el-pagination>
    </div>
    <register-user  ref="registerUser"  @userAddSuccess="searchClick"></register-user>
  </div>
</template>

<script>
  import RegisterUser from  './components/RegisterUser'
  import ElPager from "element-ui/packages/pagination/src/pager";
  export default{
    components:{ElPager, RegisterUser},
    data(){
      return {
        keywords: '',
        fullloading: false,
        hrs: [],
        cardLoading: [],
        eploading: [],
        allRoles: [],
        moreBtnState:false,
        selRoles: [],
        selRolesBak: [],
        totalRows: 0,
        page: 1,
        rows:4,
      }
    },
    mounted: function () {
      this.initCards();
      this.loadAllRoles();
    },

    methods: {
      addSysMan(){
        this.$refs.registerUser.init();
      },

      //page页数切换
      pageChange(page){
        this.rows = page;
        this.searchClick();
      },

      //pageSize页数切换
      pageSizeChange(rows){
        this.rows = rows;
        this.searchClick();
      },

      //查询系统管理员
      searchClick(){
        this.initCards();
        this.loadAllRoles();
      },

      //重置插叙条件
      resetClick: function () {
        this.keywords = "";
        this.initCards();
        this.loadAllRoles();
      },

      //更新管理员角色
      updateHrRoles(hrId, index){
        this.moreBtnState = false;
        var _this = this;
        if (this.selRolesBak.length == this.selRoles.length) {
          for (var i = 0; i < this.selRoles.length; i++) {
            for (var j = 0; j < this.selRolesBak.length; j++) {
              if (this.selRoles[i] == this.selRolesBak[j]) {
                this.selRolesBak.splice(j, 1);
                break;
              }
            }
          }
          if (this.selRolesBak.length == 0) {
            return;
          }
        }
        this.eploading.splice(index, 1, true)
        this.putRequest("/system/hr/roles", {
          hrId: hrId,
          rids: this.selRoles
        }).then(resp=> {
          _this.eploading.splice(index, 1, false);
          if (resp && resp.status == 200) {
            var data = resp.data;
            
            if (data.status == 'success') {
              _this.refreshHr(hrId, index);
            }
            _this.searchClick();
          }
        });
      },
      refreshHr(hrId, index){
        var _this = this;
        _this.cardLoading.splice(index, 1, true)
        this.putRequest("/system/hr/id/" + hrId).then(resp=> {
          _this.cardLoading.splice(index, 1, false)
          _this.hrs.splice(index, 1, resp.data);
        })
      },
      loadSelRoles(hrRoles, index){
        this.moreBtnState = true;
        this.selRoles = [];
        this.selRolesBak = [];
        hrRoles.forEach(role=> {
          this.selRoles.push(role.id)
          this.selRolesBak.push(role.id)
        })
      },
      loadAllRoles(){
        var _this = this;
        this.getRequest("/system/basic/roles").then(resp=> {
          _this.fullloading = false;
          if (resp && resp.status == 200) {
            _this.allRoles = resp.data;
          }
        })
      },
      switchChange(newValue, hrId, index){
        var _this = this;
        _this.cardLoading.splice(index, 1, true)
        this.putRequest("/system/hr/", {
          enabled: newValue,
          id: hrId
        }).then(resp=> {
          _this.cardLoading.splice(index, 1, false)
          if (resp && resp.status == 200) {
            var data = resp.data;
            
            if (data.status == 'error') {
              _this.refreshHr(hrId, index);
            }
          } else {
            _this.refreshHr(hrId, index);
          }
        })
      },
      initCards(){
        this.fullloading = true;
        var _this = this;
        var searchWords;
        let params = {};
        params.page = this.page;
        params.rows = this.rows;
        params.keywords = this.keywords;
        if (this.keywords === '') {
          searchWords = 'all';
        } else {
          searchWords = this.keywords;
        }
        var url = "/system/hr/" + searchWords;
        this.getRequest(url,params).then(resp=> {
          console.log(resp)
          if (resp && resp.status == 200) {
            _this.hrs = resp.data.data.data;
            var length = resp.data.data.data.length;
            _this.totalRows = length;
            _this.cardLoading = Array.apply(null, Array(length)).map(function (item, i) {
              return false;
            });
            _this.eploading = Array.apply(null, Array(length)).map(function (item, i) {
              return false;
            });
          }
        })
      },
      deleteHr(hrId){
        var _this = this;
        this.fullloading = true;
        this.deleteRequest("/system/hr/" + hrId).then(resp=> {
          _this.fullloading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
           
            if (data.status == 'success') {
              _this.initCards();
              _this.loadAllRoles();
            }
          }
        })
      }
    }
  }
</script>
<style>
  .user-info {
    font-size: 12px;
    color: #09c0f6;
  }
</style>
