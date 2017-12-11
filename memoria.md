# Análise do consumo de memória

## Android profiler ##

Os seugintes testes foram feitos para avaliar o uso de memória do aplicativo.

* __Abertura do aplicativo:__ O aplicativo quando aberto inicialmente se manteve com um consumo de memória entre 11 e 12 MB. Todos os testes após este foram feitos partindo do app recém-aberto.

* __Abertura e fechamento da EpisodeDetailActivity:__ Após abrir e fechar várias vezes a tela de detalhe do podcast, o consumo de memória subiu para 16 MB, porém depois de um tempo regrediu para pouco menos de 14 MB.

* __Scroll na tela inicial:__ Após ir até o fim da lista de podcasts e voltar algumas vezes, o uso de memória subiu para 16 MB, depois desceu para 14 MB. Em outro teste, mantendo o dedo na tela e deslizando várias vezes o uso de memória subiu para 15,5 MB e depois desceu para 15 MB.

* __Download de podcast:__ Ao inciar o download do podcast "Ciência e Pseudociência", o uso de memória subiu pouco, chegnado a 11,79 MB. Quando o download terminou, esse valor subiu para 12 MB e permaneceu nele.

* __Segundo plano:__ Ao deixar o aplicativo em segundo plano, o uso de memória desceu para 10 MB.
