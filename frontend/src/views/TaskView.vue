<template>
  <div class="page">
    <div class="container">
      <header class="header">
        <h1 class="title">PawaTask</h1>
        <button class="new-task-btn" @click="showModal = true">Add a new task</button>
      </header>

      <AddTaskModal :visible="showModal" @close="showModal = false" />
      <template v-if="tasks.length === 0 && !loadError">
        <div class="empty-state">
          <p>
            You do not have any tasks
            <a href="#" @click.prevent="showModal = true">add a new task</a>
          </p>
        </div>
      </template>
      <template v-else>
        <TaskItem v-for="t in tasks" :key="t.id" :task="t" :canEdit="isAuthenticated" />
      </template><template v-if="loadError">
      <div class="empty-state error">
        <p>⚠️ Failed to load tasks. Please try again later.</p>
      </div>
    </template>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue';
import {taskApi} from "@/plugins/axios";
import TaskItem from "@/components/TaskItem.vue";
import {mapGetters} from "vuex";
import AddTaskModal from "@/components/AddTaskModal.vue";

export default Vue.extend({
  name: 'TaskView',
  components: {TaskItem, AddTaskModal},
  data() {
    return {
      tasks: [],
      showModal: false,
      loadError: false
    }
  },
  async mounted() {
    try {
      const res = await taskApi.get('/tasks')
      this.tasks = res.data
    } catch (e) {
      this.loadError = true
    }
  },
  computed: {
    ...mapGetters('auth', ['isAuthenticated'])
  }
});
</script>

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
  width: 100%;
  max-width: 500px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  margin: 0 16px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  border-top: 1px solid #ddd;
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

@media (max-width: 480px) {
  .title {
    font-size: 20px;
  }

  .new-task-btn {
    padding: 6px 12px;
    font-size: 14px;
  }
}
@media (min-width: 768px) {
  .container {
    width: 500px;
  }
}
</style>
