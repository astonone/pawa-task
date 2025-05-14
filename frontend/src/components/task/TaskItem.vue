<template>
  <div class="task-item">
    <div class="left">
      <span :title="!canEdit ? 'Only authorized users can complete tasks' : ''">
        <input
          type="checkbox"
          :checked="task.done"
          :disabled="!canEdit"
          class="checkbox"
          :class="{ disabled: !canEdit }"
          @change="toggleDone"
        />
      </span>

      <span class="title" :class="{ done: task.done }">{{ task.title }}</span>
    </div>

    <div class="right">
      <i class="fas fa-calendar-alt"></i>
      <span class="date">{{ formattedDate }}</span>

      <button class="btn" @click="openDetailsModal">
        <i class="fas fa-comment-alt"></i>
      </button>

      <span :title="!canEdit ? 'Only authorized users can edit tasks' : ''">
        <button
          class="btn"
          :class="{ disabled: !canEdit }"
          :disabled="!canEdit"
          @click="openEditModal"
        >
          <i class="fas fa-edit"></i>
        </button>
      </span>
      <EditTaskModal
        :visible="showEdit"
        :task="task"
        @close="showEdit = false"
        @task-updated="$emit('task-updated')"
      />
      <TaskDetailsModal
        v-if="showDetails"
        :task="task"
        :can-edit="canEdit"
        @close="showDetails = false"
        @edit-task="openEditModal"
        @task-updated="$emit('task-updated')"
      />
      <ErrorModal v-if="showErrorModal" :message="errorMessage" @close="showErrorModal = false" />
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue';
import { TaskDto } from '@/types/tasks';
import EditTaskModal from '@/components/task/modal/EditTaskModal.vue';
import { taskApi } from '@/plugins/axios';
import TaskDetailsModal from '@/components/task/modal/TaskDetailsModal.vue';
import { AxiosError } from 'axios';
import ErrorModal from '@/components/error/ErrorModal.vue';

export default Vue.extend({
  components: { ErrorModal, TaskDetailsModal, EditTaskModal },
  props: {
    task: {
      type: Object as () => TaskDto,
      required: true
    },
    canEdit: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      showEdit: false,
      showDetails: false,
      showErrorModal: false,
      errorMessage: ''
    };
  },
  computed: {
    formattedDate(): string {
      const date = new Date(this.task.todoDate);
      return date.toLocaleString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      });
    }
  },
  methods: {
    async toggleDone() {
      if (!this.canEdit) return;

      try {
        await taskApi.patch(`/tasks/${this.task.id}/toggle`);
        this.$emit('task-updated');
      } catch (err: unknown) {
        const axiosErr = err as AxiosError;
        const code = axiosErr.response?.status || '???';
        const message = (axiosErr.response?.data as any)?.message || 'Failed to update task status';
        this.errorMessage = `Error ${code}: ${message}`;
        this.showErrorModal = true;
      }
    },
    openEditModal() {
      if (this.canEdit) {
        this.showEdit = true;
      }
    },
    openDetailsModal() {
      this.showDetails = true;
    }
  }
});
</script>

<style scoped>
.task-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-top: 1px solid #ddd;
  font-size: 14px;
}

.left {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
}

.right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.title {
  flex-grow: 1;
  font-weight: 500;
}

.date {
  margin-right: 10px;
}

.btn {
  background-color: #f04f3e;
  color: white;
  border: none;
  padding: 6px 8px;
  cursor: pointer;
}

.btn:hover {
  background-color: #d33a2f;
}

.btn.disabled {
  background-color: #ccc;
  cursor: default;
}

.btn.disabled {
  background-color: #ccc;
}

.checkbox.disabled:hover {
  cursor: default;
}
.checkbox:hover {
  cursor: pointer;
}
.title.done {
  text-decoration: line-through;
  color: #aaa;
}
.task-item.done {
  opacity: 0.7;
}
</style>
