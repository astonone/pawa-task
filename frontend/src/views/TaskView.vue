<template>
  <div class="page">
    <div class="container">
      <header class="header">
        <h1 class="title">PawaTask</h1>
        <button class="new-task-btn">Add a new task</button>
      </header>

      <div class="empty-state">
        <p>
          You do not have any tasks
          <a href="#">add a new task</a>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page {
  background-color: #f3f3f3;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  padding: 40px 0;
}

.container {
  background-color: white;
  padding: 30px;
  width: 500px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

.new-task-btn {
  background-color: #f04f3e;
  color: white;
  border: none;
  padding: 8px 16px;
  font-weight: bold;
  cursor: pointer;
}

.new-task-btn:hover {
  background-color: #d33a2f;
}

.empty-state {
  text-align: center;
}

.empty-state a {
  color: #d33a2f;
  font-weight: bold;
  text-decoration: none;
}

.empty-state a:hover {
  text-decoration: underline;
}
</style>

<script lang="ts">
import Vue from 'vue';

export default Vue.extend({
  name: 'TaskView',
  components: {},
});
</script>
