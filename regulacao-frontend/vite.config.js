import tailwindcss from '@tailwindcss/vite';
import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	plugins: [tailwindcss(), sveltekit()],
	server: {
		proxy: {
		  // tudo que bater em /api vai para o Spring Boot
		  '/api': {
			target: 'http://localhost:8080',
			changeOrigin: true,
			secure: false
		  }
		}
	  }
	}
);
