<template>
  <div class="page">
    <div class="container">
      <header class="header">
        <h1 class="title">PawaTask</h1>
        <span :title="!isAuthenticated ? 'Only authorized users can add new tasks' : ''">
          <button
            class="new-task-btn"
            :class="{ disabled: !isAuthenticated }"
            :disabled="!isAuthenticated"
            @click="showModal = true"
          >
            Add a new task
          </button>
        </span>
      </header>

      <div class="filters">
        <label class="filter-label">Filter by priority:</label>
        <select v-model="priorityFilter" class="priority-filter">
          <option>All</option>
          <option>LOW</option>
          <option>MEDIUM</option>
          <option>HIGH</option>
          <option>CRITICAL</option>
        </select>

        <label class="filter-label">Show only done tasks:</label>
        <input v-model="onlyDone" type="checkbox" class="done-checkbox" />
      </div>

      <AddTaskModal :visible="showModal" @close="showModal = false" @task-created="reloadTasks" />
      <template v-if="filteredTasks.length === 0 && !loadError">
        <div class="empty-state">
          <p>
            You do not have any tasks
            <a href="#" @click.prevent="showModal = true">add a new task</a>
          </p>
        </div>
      </template>
      <template v-else>
        <TaskItem
          v-for="t in filteredTasks"
          :key="t.id"
          :task="t"
          :can-edit="isAuthenticated"
          @task-updated="reloadTasks"
        />
      </template>
      <template v-if="loadError">
        <div class="empty-state error">
          <p>⚠️ {{ loadError }}</p>
        </div>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue';
import { taskApi } from '@/plugins/axios';
import TaskItem from '@/components/task/TaskItem.vue';
import { mapGetters } from 'vuex';
import AddTaskModal from '@/components/task/modal/AddTaskModal.vue';
import { AxiosError } from 'axios';
import { TaskDto } from '@/types/tasks';

export default Vue.extend({
  name: 'TaskView',
  components: { TaskItem, AddTaskModal },
  data() {
    return {
      tasks: [] as TaskDto[],
      showModal: false,
      loadError: '',
      priorityFilter: 'All',
      onlyDone: false
    };
  },
  computed: {
    ...mapGetters('auth', ['isAuthenticated']),
    filteredTasks(): TaskDto[] {
      return this.tasks.filter((task: TaskDto) => {
        const matchesPriority =
          this.priorityFilter === 'All' || task.priority === this.priorityFilter;
        const matchesDone = this.onlyDone ? task.done : true;
        return matchesPriority && matchesDone;
      });
    }
  },
  mounted() {
    this.fetchTasks();
  },
  methods: {
    async fetchTasks() {
      this.loadError = '';
      try {
        const res = await taskApi.get('/tasks');
        this.tasks = res.data;
      } catch (err: unknown) {
        const axiosErr = err as AxiosError;
        const status = axiosErr.response?.status;
        const msg = (axiosErr.response?.data as any)?.message || 'Unknown error';
        this.loadError = `Error ${status || '???'}: ${msg}`;
      }
    },
    async reloadTasks() {
      await this.fetchTasks();
    }
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

.new-task-btn.disabled {
  background-color: #ccc;
}

.new-task-btn.disabled:hover {
  cursor: default;
}

.filters {
  margin-bottom: 20px;
}

.priority-filter {
  margin-top: 4px;
  margin-bottom: 12px;
  padding: 8px;
  font-size: 14px;
  width: 100%;
  box-sizing: border-box;
}

.filter-label {
  font-weight: bold;
  display: block;
  margin-top: 8px;
}

.done-checkbox {
  margin-top: 8px;
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
