<template>
  <header class="app-header">
    <div class="header-content">
      <div class="logo">
        <i class="fas fa-tasks"></i>
      </div>
      <div class="auth">
        <template v-if="isAuthenticated">
          <span class="user-info">Logged as: {{ username }}</span>
          <button class="auth-btn" @click="logout">Logout</button>
        </template>
        <template v-else>
          <button class="auth-btn" @click.prevent="$emit('show-login')">Login</button>
        </template>
      </div>
    </div>
  </header>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapGetters, mapActions } from 'vuex'

export default Vue.extend({
  computed: {
    ...mapGetters('auth', ['isAuthenticated', 'token']),
    username: function (): string {
      const userInfo = this.$store.state.auth.userInfo;
      return userInfo ? userInfo.fullName : '';
    }
  },
  methods: {
    ...mapActions('auth', ['logout'])
  }
})
</script>

<style scoped>
.app-header {
  background-color: white;
  border-bottom: 1px solid #ddd;
  padding: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.auth {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-info {
  font-weight: bold;
}

.auth-btn {
  background-color: #f04f3e;
  color: white;
  border: none;
  padding: 6px 12px;
  font-weight: bold;
  cursor: pointer;
  border-radius: 0;
}

.auth-btn:hover {
  background-color: #d33a2f;
}

</style>