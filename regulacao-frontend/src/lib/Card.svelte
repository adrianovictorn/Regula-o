<script lang="ts">
  export let title: string;
  export let value: string | number;
  export let color: 'emerald' | 'emerald-dark' | 'default' = 'emerald';

  const colorStyles: Record<string, { container: string; title: string; value: string }> = {
    emerald: {
      container: 'bg-white',
      title: 'text-emerald-800',
      value: 'text-emerald-500'
    },
    'emerald-dark': {
      container: 'bg-white',
      title: 'text-emerald-900',
      value: 'text-emerald-700'
    },
    default: {
      container: 'bg-white',
      title: 'text-gray-800',
      value: 'text-gray-600'
    }
  };

  $: styles = colorStyles[color] ?? colorStyles.default;
</script>

<div class={`shadow-md rounded-lg p-6 flex flex-col items-center justify-center text-center ${styles.container}`}>
  <h2 class={`text-base font-semibold ${styles.title}`}>{title}</h2>
  <h2 class={`text-lg font-bold ${styles.value}`}>{value}</h2>
</div>

