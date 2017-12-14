# Análise do uso de rede

## Android Profiler

Os seguintes testes foram feitos para avaliar o uso de rede do aplicativo.
Todos os testes, com exceção da abertura do aplicativo, foram feitos partindo do app recém-aberto.

* __Abertura do aplicativo:__ O aplicativo teve um pico de 27KB/s e em 1 segundo desceu a 0.

* __Abertura e fechamento da EpisodeDetailActivity:__ O aplicativo teve um rápido pico de 69KB/s ao retornar para a tela inicial.

* __Download de podcast:__ Ao realizar o download de um podcast, o consumo de rede oscilou entre 0 e 2 MB/s, até o fim do download.

* __Segundo plano:__ Ao retornar para o app, o consumo de rede subiu para 132KB/s.

* __Execução de podcast:__ O consumo de rede foi similar o do download do podcast. Ao pausar e continuar a execução, o consumo diminui para 0,3 MB/s.

### Modificação 2: Checagem de atualização ###

Checando quando foi a última atualização do XML, é possível baixar uma nova lista de podcasts somente quando a atual estiver desatualizada. Isso evita constantes downloads ao visualizar as informações dos podcasts.

* __Abertura do aplicativo:__ O aplicativo teve um pico de 52KB/s e em 1 segundo desceu a 0.

* __Abertura e fechamento da EpisodeDetailActivity:__ O aplicativo teve um rápido pico de 118KB/s ao retornar para a tela inicial.

* __Download de podcast:__ Ao realizar o download de um podcast, o consumo de rede oscilou entre 0 e 1,5 MB/s, até o fim do download.

* __Segundo plano:__ Ao retornar para o app, o consumo de rede teve um curto pico de 1,5KB/s.

* __Execução de podcast:__ O consumo de rede foi similar o do download do podcast. Ao pausar e continuar a execução, o consumo diminui para 1,7 MB/s.

O consumo de banda envolvendo diretamente os podcasts domina o consumo de banda, uma vez que os downloads de podcasts são da ordem de MB/s e o download da lista é da ordem de KB/s. Uma vez que todos os downloads são feitos apenas quando necessário, é improvável que hajam outras possibilidades de otimização neste sentido.
