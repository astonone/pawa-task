<template>
  <div class="overlay">
    <div class="modal">
      <div class="modal-header">
        <h2>Session expired</h2>
      </div>
      <div class="modal-body">
        <p>Your session has expired or the user service is unavailable.</p>
        <p>Please log in again to continue.</p>
      </div>
      <div class="modal-footer">
        <button class="confirm-btn" @click="handleConfirm">Login again</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue';

export default Vue.extend({
  methods: {
    handleConfirm() {
      this.$emit('close');
      this.$root.$emit('show-login');
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
  z-index: 1100;
}

.modal {
  background: white;
  padding: 24px;
  border-radius: 4px;
  width: 400px;
  box-shadow: 0 0 16px rgba(0, 0, 0, 0.25);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.modal-header h2 {
  margin: 0;
}

.modal-body p {
  margin: 0;
  line-height: 1.4;
}

.modal-footer {
  text-align: right;
}

.confirm-btn {
  background-color: #f04f3e;
  color: white;
  border: none;
  padding: 10px 16px;
  font-weight: bold;
  cursor: pointer;
}

.confirm-btn:hover {
  background-color: #d33a2f;
}
</style>
