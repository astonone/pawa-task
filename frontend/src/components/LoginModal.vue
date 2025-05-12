<template>
  <div class="overlay" v-if="visible">
    <div class="modal">
      <div class="header">
        <h2>{{ isRegister ? 'Register' : 'Login' }}</h2>
        <button class="close-btn" @click="$emit('close')">×</button>
      </div>

      <form @submit.prevent="handleSubmit">
        <label>Username</label>
        <input v-model="username" type="text" required />

        <label>Password</label>
        <input v-model="password" type="password" required />

        <div v-if="isRegister">
          <label>Full Name</label>
          <input v-model="fullName" type="text" required />
        </div>

        <button type="submit" class="login-btn">
          {{ isRegister ? 'Register' : 'Login' }}
        </button>
      </form>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

      <div class="footer">
        <span v-if="isRegister">Already have an account?</span>
        <span v-else>Don't have an account?</span>
        <a href="#" @click.prevent="isRegister = !isRegister">
          {{ isRegister ? 'Log in' : 'Register' }}
        </a>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import {userApi} from "@/plugins/axios";

export default Vue.extend({
  props: {
    visible: Boolean
  },
  data() {
    return {
      username: '',
      password: '',
      fullName: '',
      isRegister: false,
      errorMessage: ''
    }
  },
  watch: {
    isRegister() {
      this.username = ''
      this.password = ''
      this.fullName = ''
      this.errorMessage = ''
    }
  },
  methods: {
    async handleSubmit() {
      try {
        if (this.isRegister) {
          await userApi.post('/auth/register', {
            username: this.username,
            password: this.password,
            fullName: this.fullName
          })

          await this.$store.dispatch('auth/login', {
            username: this.username,
            password: this.password
          })

          this.$emit('close')
          if (this.$route.path !== '/') {
            this.$router.push('/')
          }
        } else {
          await this.$store.dispatch('auth/login', {
            username: this.username,
            password: this.password
          })

          this.$emit('close')
          if (this.$route.path !== '/') {
            this.$router.push('/')
          }
        }
      } catch (err) {
        this.errorMessage = this.isRegister
            ? 'Registration failed. Please check your data.'
            : 'Login failed. Please check your credentials.'
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
.header {
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
input {
  width: 100%;
  padding: 8px;
  margin-top: 4px;
  box-sizing: border-box;
}
.login-btn {
  margin-top: 20px;
  width: 100%;
  background-color: #f04f3e;
  color: white;
  border: none;
  padding: 10px;
  font-weight: bold;
  cursor: pointer;
}
.login-btn:hover {
  background-color: #d33a2f;
}
.footer {
  margin-top: 16px;
  text-align: center;
}
.footer a {
  margin-left: 8px;
  color: #f04f3e;
  font-weight: bold;
  cursor: pointer;
  text-decoration: none;
}
.footer a:hover {
  text-decoration: underline;
}
.error {
  margin-top: 16px;
  color: #d33a2f;
  font-weight: bold;
  text-align: center;
}
</style>