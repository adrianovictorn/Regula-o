// front-regulacao/svelte.config.js
import adapter from '@sveltejs/adapter-static';

const config = {
  kit: {
    adapter: adapter({
      // Caminho completo e absoluto para a pasta 'static' do seu projeto Spring Boot
      out: '/home/adriano-victor/Documentos/REGULAÇÃO/regulacao-marcarcao-dev/src/main/resources/static',
      fallback: 'index.html',
      strict: false // Esta linha é crucial para rotas dinâmicas em SPA
    }),
  }
};

export default config;