// 初始化axios
import axios from 'axios';
import {message} from "ant-design-vue";
import store from "@/store";
import {Tool} from "@/utils/tool";
import router from "@/router";

const api = axios.create({
  baseURL: "http://localhost:8880/obwiki", // 使用环境变量设置基础URL
  timeout: 10000, // 设置请求超时时间为10秒
});

// 添加请求拦截器
/*
* 使用axios拦截器打印请求和响应的数据
* */

api.interceptors.request.use((config) => {
    console.log('请求参数：', config);

    // 从 store 获取 token
  console.log("获取token：",store.state.user?.token)
    const token = store.state.user?.token;

    if (Tool.isNotEmpty(token)) {
      // 使用标准 Authorization 头
      config.headers.token = `${token}`;
      console.log("请求headers增加token:", token);
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  });

// 添加响应拦截器处理错误
api.interceptors.response.use(response => {
  console.log('响应结果：', response);
  return response;
}, error => {
  const { response } = error;
  return Promise.reject(error);
});

// 导出API实例
export default api;
