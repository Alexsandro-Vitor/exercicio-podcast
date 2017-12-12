# Análise do uso de rede

## Android Profiler

Os seguintes testes foram feitos para avaliar o uso de rede do aplicativo.
Todos os testes, com exceção da abertura do aplicativo, foram feitos partindo do app recém-aberto.

* __Abertura do aplicativo:__ O aplicativo teve um pico de 27KB/s e em 1 segundo desceu a 0.

* __Abertura e fechamento da EpisodeDetailActivity:__ O aplicativo teve um rápido pico de 69KB/s ao retornar para a tela inicial.

* __Download de podcast:__ Ao realizar o download do podcast Darwin e a Evolução", o consumo de rede oscilou entre 0 e 2 MB/s, até o fim do download.

* __Segundo plano:__ Ao retornar para o app, o consumo de rede subiu para 132KB/s.

* __Execução de podcast:__ O consumo de rede foi similar o do download do podcast. Ao pausar e continur a execução, o consumo diminui para 0,3 MB/s.
