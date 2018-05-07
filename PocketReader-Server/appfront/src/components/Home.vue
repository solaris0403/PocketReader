<!--<template>-->
<!--<div class="home">-->
<!--<el-row display="margin-top:10px">-->
<!--<el-input v-model="book_url" placeholder="请输入链接" style="display:inline-table; width: 30%; float:left"></el-input>-->
<!--<el-button type="primary" @click="addBook()" style="float:left; margin: 2px;">新增</el-button>-->
<!--</el-row>-->
<!--<el-row display="margin-top:10px">-->
<!--<el-input v-model="book_id" placeholder="请输入编号" style="display:inline-table; width: 30%; float:left"></el-input>-->
<!--<el-button type="primary" @click="removeBook()" style="float:left; margin: 2px;">删除</el-button>-->
<!--</el-row>-->
<!--<el-row>-->
<!--<el-table :data="bookList" style="width: 100%" border>-->
<!--<el-table-column prop="id" label="编号" min-width="100">-->
<!--<template scope="scope"> {{ scope.row.pk }} </template>-->
<!--</el-table-column>-->
<!--<el-table-column prop="book_name" label="链接" min-width="100">-->
<!--<template scope="scope"> {{ scope.row.fields.book_name }} </template>-->
<!--</el-table-column>-->
<!--<el-table-column prop="add_time" label="添加时间" min-width="100">-->
<!--<template scope="scope"> {{ scope.row.fields.add_time }} </template>-->
<!--</el-table-column>-->
<!--</el-table>-->
<!--</el-row>-->
<!--</div>-->
<!--</template>-->
<template>
  <div id="home">
    <el-container>
      <el-header>
      </el-header>

      <el-container>
        <el-aside width="200px">
          <el-col :span="4">
            <el-menu :default-active="$route.path" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
                     theme="dark"
                     router>
              <el-menu-item index="/upload_img">图片上传</el-menu-item>
              <el-menu-item index="/upload_video">视频上传</el-menu-item>
              <el-menu-item index="/https">网络请求</el-menu-item>
              <el-menu-item index="/other">其他</el-menu-item>
            </el-menu>
          </el-col>
        </el-aside>


        <el-main>Main</el-main>
      </el-container>


    </el-container>
  </div>
</template>

<script>
  export default {
    name: 'home',
    data () {
      return {
        input: '',
        bookList: [],
        visible: false,
        activeIndex: '1',
        activeIndex2: '1'
      }
    },
    mounted: function () {
      this.showBooks()
    },
    methods: {
      addBook(){
        this.$http.get('http://127.0.0.1:8000/api/add_book?url=' + this.book_url)
          .then((response) => {
            var res = JSON.parse(response.bodyText)
            if (res.error_num == 0) {
              this.showBooks()
            } else {
              this.$message.error('新增链接失败，请重试')
              console.log(res['msg'])
            }
          })
      },
      showBooks(){
        this.$http.get('http://127.0.0.1:8000/api/show_books')
          .then((response) => {
            var res = JSON.parse(response.bodyText)
            console.log(res)
            if (res.error_num == 0) {
              this.bookList = res['list']
            } else {
              this.$message.error('查询链接失败')
              console.log(res['msg'])
            }
          })
      },
      removeBook(){
        this.$http.get('http://127.0.0.1:8000/api/remove_book?id=' + this.book_id)
          .then((response) => {
            var res = JSON.parse(response.bodyText)
            if (res.error_num == 0) {
              this.showBooks()
              this.book_id = ''
            } else {
              this.$message.error('删除链接失败，请重试')
              console.log(res['msg'])
            }
          })
      },
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      }
    }
  }
</script>

<style>
  .el-header, .el-footer {
    background-color: #B3C0D1;
    color: #333;
    text-align: center;
    line-height: 60px;
  }

  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 200px;
  }

  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 160px;
  }

  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }

  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
</style>
