<script lang="ts">
  export let header: string;
  export let title: string;
  export let value: string | number;
  export let href: string;
  export let color: 'emerald' | 'emerald-dark' | 'default' = 'emerald';

  const colorStyles: Record<string, { title: string; value: string; container: string }> = {
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

<a href={href} class="block">
  <div class={`rounded-xl p-4 shadow-md transform hover:scale-102 hover:shadow-xl transition-all duration-300 cursor-pointer text-center flex flex-col items-center justify-center ${styles.container}`}>
    <h1 class="text-base font-semibold p-0.5">{header}</h1>
    <h2 class={`text-base font-semibold ${styles.title}`}>{title}</h2>
    <h2 class={`text-lg font-bold ${styles.value}`}>{value}</h2>
  </div>
</a>
