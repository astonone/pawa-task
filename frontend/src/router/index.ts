import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import TaskView from '../views/TaskView.vue';

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'home',
    component: TaskView
  }
];

const router = new VueRouter({
  routes
});

export default router;
