<template>
  <div class="layout">
    <AppHeader @show-login="openLogin" />
    <LoginModal :key="modalKey" :visible="showLogin" @close="showLogin = false" />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script lang="ts">
import Vue from 'vue';
import AppHeader from '@/components/main/AppHeader.vue';
import LoginModal from '@/components/login-modal/LoginModal.vue';

export default Vue.extend({
  components: { AppHeader, LoginModal },
  data() {
    return {
      showLogin: false,
      modalKey: 0
    };
  },
  created() {
    this.$root.$on('show-login', this.openLogin);
  },
  beforeDestroy() {
    this.$root.$off('show-login', this.openLogin);
  },
  methods: {
    openLogin() {
      this.modalKey++;
      this.showLogin = true;
    }
  }
});
</script>

<style scoped>
.layout {
  background-color: #f3f3f3;
  min-height: 100vh;
}

.main-content {
  display: flex;
  justify-content: center;
}
</style>
