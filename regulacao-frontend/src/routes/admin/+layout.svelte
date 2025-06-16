<script>
  import { user } from '$lib/stores/auth.js';
  import { goto } from '$app/navigation';
  import { onMount } from 'svelte';

  // Esta verificação acontece sempre que uma página dentro de /admin é acessada.
  onMount(() => {
    // Acessa o valor da store. Se não houver usuário ou o perfil não for ADMIN...
    if ($user?.role !== 'ADMIN') {
      alert('Acesso negado. Você não tem permissão para ver esta página.');
      // ...redireciona o usuário para a página inicial.
      goto('/home');
    }
  });
</script>

<!-- Se o usuário for ADMIN, o SvelteKit renderiza o conteúdo da página aqui -->
{#if $user?.role === 'ADMIN'}
  <slot />
{/if}