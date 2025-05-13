<template>
  <div class="task-item">
    <div class="left">
      <span :title="!canEdit ? 'Only authorized users can complete tasks' : ''">
        <input
            type="checkbox"
            :checked="task.done"
            @change="toggleDone"
            :disabled="!canEdit"
            class="checkbox"
            :class="{ disabled: !canEdit }"
        />
      </span>

      <span class="title" :class="{ done: task.done }">{{ task.title }}</span>
    </div>

    <div class="right">
      <i class="fas fa-calendar-alt"></i>
      <span class="date">{{ formattedDate }}</span>

      <span :title="!canEdit ? 'Only authorized users can add comments' : ''">
        <button
          class="btn"
          :class="{ disabled: !canEdit }"
          :disabled="!canEdit"
        >
          <i class="fas fa-comment-alt"></i>
        </button>
      </span>

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
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import {TaskDto} from "@/types/tasks";
import EditTaskModal from "@/components/task/modal/EditTaskModal.vue";
import {taskApi} from "@/plugins/axios";

export default Vue.extend({
  components: {EditTaskModal},
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
      showEdit: false
    }
  },
  methods: {
    async toggleDone() {
      if (!this.canEdit) return;

      try {
        await taskApi.patch(`/tasks/${this.task.id}/toggle`)
        this.$emit('task-updated')
      } catch (err) {
        alert('Failed to update task status')
      }
    },
    openEditModal() {
      if (this.canEdit) {
        this.showEdit = true
      }
    }
  },
  computed: {
    formattedDate(): string {
      const date = new Date(this.task.todoDate)
      return date.toLocaleString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      })
    }
  }
})
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