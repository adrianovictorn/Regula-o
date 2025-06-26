<script>
    export let header;
    export let title;
    export let value;
    export let href;
</script>

<a href={href} class="block">
    <div class="bg-yellow-500 rounded-xl p-4 shadow-md transform hover:scale-102 hover:shadow-xl transition-all duration-300 cursor-pointer text-center flex flex-col items-center justify-center">
        <h1 class="text-base font-semibold p-0.5">{header}</h1>
        <h2 class="text-base font-semibold text-white">{title}</h2>
        <h2 class="text-lg font-bold text-white">{value}</h2>

</div>
</a>