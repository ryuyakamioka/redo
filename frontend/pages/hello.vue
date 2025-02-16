<template>
  <div>
    <h1>hello world!!</h1>
    <div>現在時刻: {{ currentTime }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const currentTime = ref('')

const updateCurrentTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString()
}

onMounted(() => {
  // コンポーネントがマウントされたら、現在時刻を更新する関数を呼び出す
  updateCurrentTime()

  // 1秒ごとに現在時刻を更新する
  const intervalId = setInterval(updateCurrentTime, 1000)

  // コンポーネントがアンマウントされたら、インターバルをクリアする
  onUnmounted(() => clearInterval(intervalId))
})
</script>