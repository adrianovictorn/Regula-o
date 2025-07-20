<script>
  import { user } from '$lib/stores/auth.js';
  import Menu from '$lib/Menu.svelte';
  import Menu2 from '$lib/Menu2.svelte';

  export let activePage = '';
</script>

{#if $user}
  {#if $user.role === 'ADMIN'}
    <Menu {activePage} />
  {:else}
    <Menu2 {activePage} />
  {/if}
{/if}