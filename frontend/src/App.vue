<template>
  <MainLayout />
</template>

<script lang="ts">
import Vue from 'vue'
import MainLayout from '@/layouts/MainLayout.vue'

export default Vue.extend({
  components: { MainLayout },
  async mounted() {
    const token = localStorage.getItem('token')
    if (token) {
      await this.$store.dispatch('auth/fetchUserInfo')
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
