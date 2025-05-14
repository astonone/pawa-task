<template>
  <div class="overlay">
    <div class="modal">
      <div class="modal-header">
        <h2>Error</h2>
        <button class="close-btn" @click="$emit('close')">×</button>
      </div>
      <p>{{ message }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue';

export default Vue.extend({
  props: {
    message: {
      type: String,
      required: true
    }
  }
});
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
</style>
