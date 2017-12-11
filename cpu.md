# Análise do uso de CPU

## Android Profiler

Os seguintes testes foram feitos para avaliar o uso de CPU do aplicativo.
Todos os testes, com exceção da abertura do aplicativo, foram feitos partindo do app recém-aberto.

* __Abertura do aplicativo:__ Ao abrir o aplicativo a CPU atinge um uso de 30% e em dois segundos desce a 0%.

* __Abertura e fechamento da EpisodeDetailActivity:__ Após abrir e fechar várias vezes a tela de detalhe do podcast, o uso da CPU chegou a 50%. Voltando da tela de detalhe para a tela principal uma única vez, o consumo chegou apenas aos 30% da abertura do aplicativo.

* __Scroll na tela inicial:__ Após ir até o fim da lista de podcasts e voltar algumas vezes, o uso de memória subiu para 20%. Em outro teste, mantendo o dedo na tela e deslizando várias vezes o uso de memória subiu para 14%.

* __Download de podcast:__ Ao iniciar o download do podcast "O Homem foi mesmo até a Lua?", o uso de CPU se manteve quase sempre entre 2 e 5%, não chegando a 7%.

* __Segundo plano:__ Ao deixar o aplicativo em segundo plano, o uso de CPU não mudou. Retornando para o app, o consumo de CPU subiu para 
cerca de 40%.

* __Execução de podcast:__ Ao iniciar a execução do podcast "O Homem foi mesmo até a Lua?", o consumo de CPU é de 14%.

Em todos os casos o consumo de CPU baixa para 0% depois de um tempo.
