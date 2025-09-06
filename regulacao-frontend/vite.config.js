import tailwindcss from '@tailwindcss/vite';
import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig, loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const BACKEND_URL = env.BACKEND_URL || `http://localhost:${env.BACKEND_PORT || 8080}`;

  return {
    plugins: [tailwindcss(), sveltekit()],
    server: {
      proxy: {
        '/api': {
          target: BACKEND_URL,
          changeOrigin: true,
          secure: false
        }
      }
    }
  };
});
