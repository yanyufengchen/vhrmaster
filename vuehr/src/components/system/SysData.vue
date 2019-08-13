<template>
  <div style="margin-top: 10px" v-loading="fullloading">
    <el-container>
      <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center">
        <div style="display: inline">
          <el-input
            placeholder="默认展示部分备份结果，可以通过备份名搜索更多..."
            prefix-icon="el-icon-search"
            size="small"
            style="width: 400px;margin-right: 10px"
            v-model="name"
            clearable>
          </el-input>
          <el-button size="small" type="primary" icon="el-icon-search" @click="searchClick">搜索</el-button>
          <el-button size="small"  icon="el-icon-refresh" @click="resetClick">重置</el-button>
        </div>
        <div style="margin-left: 5px;margin-right: 20px;display: inline">
            <el-button type="primary" size="small" icon="el-icon-plus" style="margin-right: 5px" @click="backSqlData">
              <i style="margin-right: 5px"></i>数据备份
            </el-button>
        </div>
      </el-header>

      <el-main style="padding-left: 0px;padding-top: 0px">
      <div>
        <el-table
          :data="emps"
          v-loading="tableLoading"
          border
          stripe
          @selection-change="handleSelectionChange"
          size="mini"
          style="width: 100%">
          <el-table-column
            type="selection"
            align="left"
            width="30">
          </el-table-column>
          <el-table-column
            prop="name"
            align="left"
            fixed
            label="备份名"
            width="200">
          </el-table-column>
          <el-table-column
            prop="tablename"
            width="200"
            align="left"
            label="表名">
          </el-table-column>
          <el-table-column
            prop="startDate"
            label="备份开始时间"
            width="200"
            align="left">
          </el-table-column>
          <el-table-column
            prop="endDate"
            width="200"
            align="left"
            label="备份结束时间">
          </el-table-column>
          <el-table-column
            prop="operateDate"
            width="200"
            align="left"
            label="操作时间">
          </el-table-column>
          <el-table-column
            prop="hr_name"
            width="200"
            align="left"
            label="操作人">
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            align="center"
            width="300">
            <template slot-scope="scope">
              <el-button @click="renewData(scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                         size="mini">数据恢复
              </el-button>
              <el-button type = "primary" @click="showEditDataView(scope.row)" style="padding: 3px 4px 3px 4px;margin: 2px"
                         size="mini">编辑
              </el-button>
              <el-button type="danger" style="padding: 3px 4px 3px 4px;margin: 2px" size="mini"
                         @click="deleteData(scope.row)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

      <div style="margin-top: 10px;display: flex;justify-content: space-between">
        <el-button type="danger" size="mini"
                   @click="deleteManyBackData">批量删除
        </el-button>
          <el-pagination
            background
          @size-change="pageSizeChange"
          @current-change="pageChange"
          :current-page="page"
          :page-sizes="[10,20,30,40]"
          :page-size="rows"
          layout="total, prev, pager, next,sizes,jumper"
          :total="totalRows">
          </el-pagination>
        </div>
      </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import ElTableBody from "element-ui";
  export default{
    components: {ElTableBody},
    data(){
      return{
        name:'',
        fullloading: false,
        tableLoading: false,
        multipleSelection: [],
        emps:[],
        page: 1,
        rows: 10,
        totalRows: 0,
      }
    },
    mounted: function () {
      this.searchClick();
    },
    methods:{

      //page页数切换
      pageChange(page){
        this.page = page;
        this.searchClick();
      },

      //pageSize页数切换
      pageSizeChange(rows){
        this.rows = rows;
        this.searchClick();
      },

      //查询条件查询指定备份数据
      searchClick(){
          this.tableLoading = true;
          let params = {};
          params.name = this.name;
          params.page = this.page;
          params.rows = this.rows;
          this.getRequest("/sys/data/getBakbyName",params).then( resp=> {
            this.tableLoading = false;
            if(resp && resp.status == 200){
              var data = resp.data.data.data;
              this.emps = data;
              this.totalRows = resp.data.data.total;
            }
        })
      },

    //重置插叙条件
    resetClick: function () {
      this.name = "";
      this.searchClick();
      },

      //批量删除备份数据
      deleteManyBackData(){
        this.$confirm('此操作将删除[' + this.multipleSelection.length + ']条数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var ids = '';
          for (var i = 0; i < this.multipleSelection.length; i++) {
            ids += this.multipleSelection[i].id + ",";
          }
          this.doDelete(ids);
          this.searchClick();
        }).catch(() => {
        });
      },

      //数据备份
      backSqlData(){

      },

      showEditDataView(row){

      },

      renewData(row){

      },

      //删除数据备份弹出提示框
      deleteData(row){
        this.$confirm('此操作将永久删除[' + row.name + ']数据备份, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.doDelete(row.id);
        }).catch(() => {
          "不可预知错误";
        });
      },

      //删除备份数据
      doDelete(ids){
        let params = {};
        params.id = ids;
        this.tableLoading = true;
        var _this = this;
        this.deleteRequest("/sys/data/deleteBak",params).then(resp=> {
          _this.tableLoading = false;
          if (resp && resp.status == 200) {
            _this.searchClick();
          }
        })
      },

      handleSelectionChange(val) {
        console.log(val);
        this.multipleSelection = val;
      },
    }

  }
</script>
