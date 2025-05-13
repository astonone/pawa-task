<template>
  <div class="overlay">
    <div class="modal">
      <div class="modal-header">
        <h2 class="title">
          {{ task.title }}
          <button
              class="edit-link"
              @click="handleEditClick"
              :disabled="!canEdit"
              :title="!canEdit ? 'Only authorized users can edit tasks' : ''"
          >
            Edit task
          </button>
        </h2>
        <button class="close-btn" @click="$emit('close')">×</button>
      </div>

      <div class="description">
        {{ task.description || 'This task has no description.' }}
      </div>

      <div class="meta">
        <div><strong>Date:</strong> {{ formattedDate }}</div>
        <div><strong>Priority:</strong> {{ task.priority }}</div>
      </div>

      <hr />

      <div class="comments">
        <div v-if="task.comments.length === 0" class="empty-comments">
          No comments yet.
        </div>
        <div v-else>
          <div v-for="(comment, index) in task.comments" :key="index" class="comment">
            <strong>{{ comment.author }}</strong>
            <span class="timestamp">{{ formatCommentDate(comment.createdAt) }}</span>
            <div>{{ comment.text }}</div>
          </div>
        </div>
      </div>

      <div class="comment-form">
        <input
            v-model="commentText"
            placeholder="Write a comment..."
            :disabled="!canEdit"
            :title="!canEdit ? 'Only authorized users can leave comments' : ''"
        />
        <button
            class="submit-btn"
            :disabled="!canEdit || !commentText.trim()"
            @click="addComment"
            :title="!canEdit ? 'Only authorized users can leave comments' : ''"
        >
          Add comment
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { TaskDto } from '@/types/tasks'
import { taskApi } from '@/plugins/axios'

export default Vue.extend({
  props: {
    task: Object as () => TaskDto,
    canEdit: Boolean
  },
  data() {
    return {
      commentText: ''
    }
  },
  computed: {
    formattedDate(): string {
      const date = new Date(this.task.todoDate)
      return date.toLocaleDateString('en-GB', {
        day: '2-digit', month: '2-digit', year: 'numeric'
      })
    }
  },
  methods: {
    handleEditClick() {
      if (this.canEdit) {
        this.$emit('close')
        this.$emit('edit-task')
      }
    },
    async addComment() {
      if (!this.commentText.trim()) return

      try {
        await taskApi.post(`/tasks/${this.task.id}/comments`, {
          text: this.commentText
        })
        this.$emit('close')
        this.$emit('task-updated')
      } catch (e) {
        alert('Failed to add comment')
      }
    },
    formatCommentDate(raw: string): string {
      if (!raw) return ''
      const date = new Date(raw)
      return date.toLocaleString('en-GB', {
        day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit'
      })
    }
  }
})
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal {
  background: white;
  padding: 24px;
  border-radius: 4px;
  width: 500px;
  max-width: 90vw;
  box-shadow: 0 0 16px rgba(0, 0, 0, 0.25);
  box-sizing: border-box;
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}
.title {
  margin: 0;
  font-size: 20px;
  font-weight: bold;
  display: flex;
  gap: 10px;
  align-items: center;
}
.edit-link {
  background: none;
  border: none;
  color: #f04f3e;
  font-weight: bold;
  cursor: pointer;
}
.edit-link:disabled {
  color: #999;
  cursor: not-allowed;
}
.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}
.description {
  margin-bottom: 16px;
  white-space: pre-wrap;
}
.meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
hr {
  border: none;
  border-top: 1px solid #ccc;
  margin: 10px 0;
}
.comments {
  margin-bottom: 10px;
}
.comment {
  margin-bottom: 10px;
}
.timestamp {
  font-size: 0.8em;
  color: #888;
  margin-left: 5px;
}
.comment-form {
  display: flex;
  gap: 10px;
}
.comment-form input {
  flex: 1;
  padding: 8px;
  box-sizing: border-box;
}
.submit-btn {
  background-color: #f04f3e;
  color: white;
  border: none;
  padding: 10px 16px;
  font-weight: bold;
  cursor: pointer;
}
.submit-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
.empty-comments {
  color: #999;
  font-style: italic;
  text-align: center;
  margin-bottom: 10px;
}
</style>