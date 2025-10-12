<script>
  import { user } from '$lib/stores/auth.js';
  import Menu from '$lib/Menu.svelte';
  import Menu2 from '$lib/Menu2.svelte';
  import Menu3 from './Menu3.svelte';
  import Menu4 from './Menu4.svelte';


  export let activePage = '';
</script>

{#if $user}
  {#if $user.role === 'ADMIN'}
    <Menu {activePage} />
  {:else if $user.role === 'RECEPCAO' || $user.role === 'ENFERMEIRO' || $user.role === 'MEDICO'}
   <Menu3  {activePage}/>
  {:else if $user.role === 'COORD_TRANSPORTE'}
   <Menu4 {activePage}/>
  {:else}
    <Menu2 {activePage} />
  {/if}
{/if}

