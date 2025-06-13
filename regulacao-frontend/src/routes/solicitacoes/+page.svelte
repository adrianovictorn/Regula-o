<script>
    export let data;
    const { content } = data.solicitacoesPage;
  </script>
  
  <div class="flex h-screen">

   <aside class="w-64 bg-gray-800 text-white flex flex-col items-center py-6">

      <nav class="space-y-4 flex flex-col items-center">
         <a href="#" class="hover:text-teal-300">Home</a>
         <a href="#" class="hover:text-teal-300">Solicitações</a>
         <a href="#" class="hover:text-teal-300">INFO2</a>
         <a href="#" class="hover:text-teal-300">INFO3</a>
      </nav>
   </aside>

   <div class="flex-1 flex flex-col">
      <header class="bg-emerald-900 text-white p-4 flex justify-center text-center">
         <h1 class="text-center text-xl">Regula System</h1>
      </header>

      <main class="flex-1 bg-gray-100 overflow-auto">
      </main>
   </div>
  </div>

 