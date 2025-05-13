<template>
  <div id="app">
    <MainLayout />
    <SessionExpiredModal v-if="sessionExpired" @close="sessionExpired = false" />
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import MainLayout from '@/layouts/MainLayout.vue'
import SessionExpiredModal from "@/components/SessionExpiredModal.vue";

export default Vue.extend({
  components: {SessionExpiredModal, MainLayout },
  data() {
    return {
      sessionExpired: false
    }
  },
  created() {
    this.$root.$on('session-expired', () => {
      this.sessionExpired = true
    })
  },
  async mounted() {
    const token = localStorage.getItem('token')
    if (token) {
      try {
        await this.$store.dispatch('auth/fetchUserInfo')
      } catch (err) {
        this.$store.dispatch('auth/logout')
        this.$root.$emit('session-expired')
      }
    }
  }
})
</script>

<style>
/* Global reset */
html, body {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>
