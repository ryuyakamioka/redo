<template>
  <li>{{ task.taskTitle.value }} - {{ task.taskStatus }}</li>
</template>

<script setup lang="ts">
import { Task } from "~/types/task";

defineProps<{
  task: Task;
}>();
</script>
