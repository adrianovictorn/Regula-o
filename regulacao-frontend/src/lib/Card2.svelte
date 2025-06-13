<script>
    export let header;
    export let title;
    export let value;
    export let href;
</script>

<a href={href} class="block">
    <div class="bg-white rounded-xl p-4 shadow-md transform hover:scale-102 hover:shadow-xl transition-all duration-300 cursor-pointer text-center flex flex-col items-center justify-center">
        <h1 class="text-base font-semibold p-0.5">{header}</h1>
        <h2 class="text-base font-semibold text-teal-800">{title}</h2>
        <h2 class="text-lg font-bold text-teal-500">{value}</h2>

</div>
</a>