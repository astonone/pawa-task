<template>
  <div class="overlay" v-if="visible">
    <div class="modal">
      <div class="modal-header">
        <h2>New task</h2>
        <button class="close-btn" @click="$emit('close')">×</button>
      </div>

      <form @submit.prevent="handleSubmit">
        <label>Title:</label>
        <input v-model="title" type="text" />
        <p v-if="errors.title" class="error">{{ errors.title }}</p>

        <label>Description:</label>
        <textarea v-model="description" rows="3" />

        <label>Set due date:</label>
        <div class="date-inputs">
          <input v-model="day" type="text" maxlength="2" placeholder="DD" />
          <input v-model="month" type="text" maxlength="2" placeholder="MM" />
          <input v-model="year" type="text" maxlength="4" placeholder="YYYY" />
        </div>
        <p v-if="errors.date" class="error">{{ errors.date }}</p>

        <label>Priority:</label>
        <select v-model="priority">
          <option value="LOW">Low</option>
          <option value="MEDIUM">Medium</option>
          <option value="HIGH">High</option>
          <option value="CRITICAL">Critical</option>
        </select>

        <label>Comments:</label>
        <textarea v-model="comments" rows="2" />
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

        <button type="submit" class="submit-btn">Add a new task</button>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import {taskApi} from "@/plugins/axios";
import {AxiosError} from "axios";

export default Vue.extend({
  props: {
    visible: Boolean
  },
  data() {
    return {
      title: '',
      description: '',
      day: '',
      month: '',
      year: '',
      priority: 'LOW',
      comments: '',
      errorMessage: '',
      errors: {
        title: '',
        date: ''
      }
    }
  },
  watch: {
    visible(newVal: boolean) {
      if (newVal) {
        this.resetForm()
      }
    }
  },
  methods: {
    resetForm() {
      this.title = ''
      this.description = ''
      this.day = ''
      this.month = ''
      this.year = ''
      this.priority = 'LOW'
      this.comments = ''
      this.errors = {
        title: '',
        date: ''
      }
    },
    async handleSubmit() {
      this.errorMessage = ''
      this.errors = { title: '', date: '' }

      if (!this.title.trim()) {
        this.errors.title = 'Title is required'
      }

      const isValidDate =
          /^\d{2}$/.test(this.day) &&
          /^\d{2}$/.test(this.month) &&
          /^\d{4}$/.test(this.year)

      if (!isValidDate) {
        this.errors.date = 'Invalid date'
      }

      if (this.errors.title || this.errors.date) return

      const todoDate = `${this.year}-${this.month}-${this.day}T12:00:00`

      try {
        await taskApi.post('/tasks', {
          title: this.title,
          description: this.description,
          todoDate,
          priority: this.priority,
          comments: this.comments ? [
            { text: this.comments }
          ] : []
        })

        this.$emit('close')
        this.$emit('task-created')
      } catch (err: unknown) {
        const axiosErr = err as AxiosError

        if ((axiosErr.response?.data as any)?.message) {
          this.errorMessage = (axiosErr.response?.data as any).message
        } else {
          this.errorMessage = 'Something went wrong. Please try again later.'
        }
      }
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
  width: 400px;
  box-shadow: 0 0 16px rgba(0, 0, 0, 0.25);
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.close-btn {
  background: transparent;
  border: none;
  font-size: 24px;
  cursor: pointer;
}
label {
  display: block;
  margin-top: 16px;
  font-weight: bold;
}
input, textarea, select {
  width: 100%;
  padding: 8px;
  margin-top: 4px;
  box-sizing: border-box;
}
.date-inputs {
  display: flex;
  gap: 8px;
}
.date-inputs input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}
.submit-btn {
  margin-top: 20px;
  width: 100%;
  background-color: #f04f3e;
  color: white;
  border: none;
  padding: 10px;
  font-weight: bold;
  cursor: pointer;
}
.submit-btn:hover {
  background-color: #d33a2f;
}
.error {
  margin-top: 4px;
  color: #d33a2f;
  font-weight: bold;
  font-size: 0.85em;
}
</style>