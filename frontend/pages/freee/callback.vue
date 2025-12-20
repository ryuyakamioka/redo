<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <div class="max-w-md w-full bg-white rounded-lg shadow-lg p-8">
      <div v-if="loading" class="text-center">
        <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>
        <p class="text-gray-700">freee認証処理中...</p>
      </div>

      <div v-else-if="success" class="text-center">
        <div class="mx-auto flex items-center justify-center h-16 w-16 rounded-full bg-green-100 mb-4">
          <svg class="h-10 w-10 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
          </svg>
        </div>
        <h2 class="text-2xl font-bold text-gray-900 mb-2">認証成功</h2>
        <p class="text-gray-600 mb-6">freeeとの連携が完了しました</p>
        <button
          @click="closeWindow"
          class="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
        >
          ウィンドウを閉じる
        </button>
      </div>

      <div v-else class="text-center">
        <div class="mx-auto flex items-center justify-center h-16 w-16 rounded-full bg-red-100 mb-4">
          <svg class="h-10 w-10 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </div>
        <h2 class="text-2xl font-bold text-gray-900 mb-2">認証失敗</h2>
        <p class="text-gray-600 mb-6">{{ errorMessage }}</p>
        <button
          @click="closeWindow"
          class="px-6 py-2 bg-gray-600 text-white rounded hover:bg-gray-700 transition-colors"
        >
          ウィンドウを閉じる
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const route = useRoute();
const loading = ref(true);
const success = ref(false);
const errorMessage = ref('');

onMounted(async () => {
  const code = route.query.code as string;

  if (!code) {
    loading.value = false;
    errorMessage.value = '認証コードが取得できませんでした';
    return;
  }

  try {
    const response = await useApi().get(`/freee/callback?code=${code}`);
    if (response.data.status === 'success') {
      success.value = true;
    } else {
      errorMessage.value = response.data.message || '認証に失敗しました';
    }
  } catch (error) {
    console.error('freee認証エラー:', error);
    errorMessage.value = '認証処理中にエラーが発生しました';
  } finally {
    loading.value = false;
  }
});

const closeWindow = () => {
  window.close();
};
</script>
