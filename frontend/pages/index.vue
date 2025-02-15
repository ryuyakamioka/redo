<template>
  <div>
    <h1>タスク一覧</h1>
    <ul v-if="tasks.length > 0">
      <li v-for="task in tasks" :key="task.taskId.value">
        <TaskItem :task="task" />
      </li>
    </ul>
    <p v-else>タスクはありません。</p>

    <h2>タスク登録</h2>
    <form @submit.prevent="createTask">
      <input type="text" v-model="newTaskTitle" placeholder="タスク名" />
      <select v-model="newTaskStatus">
        <option value="TODO">TODO</option>
        <option value="IN_PROGRESS">IN_PROGRESS</option>
        <option value="DONE">DONE</option>
      </select>
      <button type="submit">登録</button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { useTaskStore } from "~/stores/task";
import { ref, computed } from "vue";
import TaskItem from "~/components/TaskItem.vue";

const taskStore = useTaskStore();
await taskStore.fetchTasks();

const newTaskTitle = ref("");
const newTaskStatus = ref("TODO");

const createTask = async () => {
  const task = {
    taskId: { value: 0 }, // サーバー側で自動採番されるため0を設定
    taskTitle: { value: newTaskTitle.value },
    taskStatus: newTaskStatus.value,
  };
  await taskStore.createTask(task);
  newTaskTitle.value = "";
  newTaskStatus.value = "TODO";
};

const tasks = computed(() => taskStore.tasks);
</script>
