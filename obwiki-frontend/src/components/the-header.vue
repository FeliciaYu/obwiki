<template>
  <a-layout-header class="header">
    <div class="logo" >海洋生物知识库 </div>
    <a-menu
      theme="dark"
      mode="horizontal"
      :style="{ lineHeight: '64px' , display:'block'}"
    >
      <a-menu-item key="/" :style="{float:'left'}">
        <router-link to="/">首页</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/ebook" :style="{float:'left'}" >
        <router-link to="/admin/ebook">海洋生物种类管理</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/category" :style="{float:'left'}">
        <router-link to="/admin/category">海洋生物分类管理</router-link>
      </a-menu-item>

      <a-menu-item key="/admin/user" :style="{float:'left'}">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>

      <a-menu-item key="/about" :style="{float:'left'}">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>

      <a-menu-item key="user"  :style="{float:'right'}"  >
        <span v-show="user.id" >您好：{{user.name}}</span>
      </a-menu-item>

      <a-menu-item key="login"  :style="{float:'right'}" >
        <a v-show="!user.id"   @click="showLoginModal">登录</a>
      </a-menu-item>

      <a-menu-item key="logout"  :style="{float:'right'}" >
        <a-popconfirm
          title="确认退出登录?"
          ok-text="是"
          cancel-text="否"
          @confirm="logout()"
        >
          <a  v-show="user.id" >退出登录</a>
        </a-popconfirm>
      </a-menu-item>

    </a-menu>

    <a-modal
      title="登录"
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>

  </a-layout-header>
</template>
<script lang="ts" setup>

import { message } from "ant-design-vue";
import api from '@/api/index';
import { computed, defineComponent, ref } from 'vue';
import forge from 'node-forge';
import store from '@/store';


//封装hexMd5函数
//没有使用本地脚本，使用的是forge库的
function hexMd5(msg: string): string {
  const md = forge.md.md5.create();
  md.update(msg, 'utf8');
  return md.digest().toHex();
}

const user =computed(()=>store.state.user);

const loginUser = ref({
  loginName: "",
  password: ""
});




const loginModalVisible = ref(false);
const loginModalLoading = ref(false);
const showLoginModal = () => {
  loginModalVisible.value = true;
};

let salt = ref("");//存储盐值

// 登录
const login = () => {
  console.log("开始登录");
  loginModalLoading.value = true;

  //前端发起请求获取salt值
  console.log("开始获取salt值");
  api.get('/user/getSalt',{
    params:{
      loginName:loginUser.value.loginName
    }
  }).then((resp) =>{
    const datasalt = resp.data;
    if(datasalt.success)
    {
      salt.value = datasalt.content;//后端返回的salt
      loginUser.value.password = hexMd5(loginUser.value.password + salt.value);


      //登录
      api.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;

        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");
          store.commit("setUser",data.content);

        } else {
          message.error(data.message);
          loginModalLoading.value = false;
        }
      });
    }else{
      message.error(datasalt.message);
      loginModalLoading.value = false;
    }
  });
};

// 退出登录
const logout = () => {
  console.log("退出登录开始");
  api.get('/user/logout/' + user.value.token).then((response) => {
    const data = response.data;
    if (data.success) {
      message.success("退出登录成功！");
      store.commit("setUser", {});
    } else {
      message.error(data.message);
    }
  });
};

</script>

<style scoped>
.logo {
  width: 150px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  color: white;
  font-size: 18px;
}

.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
</style>
