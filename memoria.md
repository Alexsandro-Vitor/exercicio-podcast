# Análise do consumo de memória

## Android profiler ##

Os seguintes testes foram feitos para avaliar o uso de memória do aplicativo.
Todos os testes, com exceção da abertura do aplicativo, foram feitos partindo do app recém-aberto.

* __Abertura do aplicativo:__ O aplicativo quando aberto inicialmente se manteve com um consumo de memória entre 11 e 12 MB.

* __Abertura e fechamento da EpisodeDetailActivity:__ Após abrir e fechar várias vezes a tela de detalhe do podcast, o consumo de memória subiu para 16 MB, porém depois de um tempo regrediu para pouco menos de 14 MB.

* __Scroll na tela inicial:__ Após ir até o fim da lista de podcasts e voltar algumas vezes, o uso de memória subiu para 16 MB, depois desceu para 14 MB. Em outro teste, mantendo o dedo na tela e deslizando várias vezes o uso de memória subiu para 15,5 MB e depois desceu para 15 MB.

* __Download de podcast:__ Ao iniciar o download de um podcast, o uso de memória subiu pouco, chegando a 11,79 MB. Quando o download terminou, esse valor subiu para 12 MB e permaneceu nele.

* __Segundo plano:__ Ao deixar o aplicativo em segundo plano, o uso de memória desceu para 10 MB.

### Modificação 1: RecyclerView ###

A ListView utilizada no app foi substituída por uma RecyclerView com a intenção de reduzir o uso de memória reaproveitando views não visualizadas ao invés de manter centenas delas. Em seguida, os testes acima foram refeitos para comparação.

A implementação consistiu em trocar a classe do ListView de MainActivity para RecyclerView e substituir o XmlFeedAdapter pela implementação em RecyclerXmlFeedAdapter.

* __Abertura do aplicativo:__ O aplicativo quando aberto inicialmente se mantem com um consumo de memória entre 12 e 15 MB (houveram varições em múltiplos testes).

* __Abertura e fechamento da EpisodeDetailActivity:__ Abrindo e fechando várias vezes a tela de detalhe do podcast, o consumo de memória subiu a cada abertura da MainActivity, chegando a 19 MB, porém depois de um tempo regrediu para 18 MB.

* __Scroll na tela inicial:__ Indo até o fim da lista de podcasts e voltando algumas vezes, o uso de memória ficou entre 16 e 17 MB. Em outro teste, mantendo o dedo na tela e deslizando várias vezes o uso de memória atingiu um pico de 14 MB (não ultrapassado devido ao garbage collector) e depois desceu para 13 MB.

* __Execução do Garbage Collector:__ Usando a opção de forçar a execução do garbage collector, o uso de memória foi reduzido para pouco mais de 12 MB.

* __Download de podcast:__ Durante o download de um podcast, o uso de memória subiu progressivamente até 15 MB, quando o garbage collector foi acionado, reduzindo a 13 MB. Isso ocorreu algumas durante o download.

* __Segundo plano:__ Ao deixar o aplicativo em segundo plano, o uso de memória desceu para 11,5 MB.

Os experimentos acima levaram à suspeita de 2 memory leaks, um nos downloads, e outro ao abrir a MainActivity.

### Modificação 2: Singleton + Checagem de atualização ###

Foi feito um singleton do RecyclerXmlFeedAdapter com o objetivo de evitar múltiplas criações do mesmo, devido a seu tamanho considerável. Porém, também era preciso reduzir o número de criações da lista de objetos do adapter, já que ela era uma grande parte do mesmo. Isso foi feito checando quando foi a última atualização do XML e só criando um novo ListView quando o antigo estiver desatualizado.

* __Abertura do aplicativo:__ O aplicativo quando aberto teve um consumo de memória de 14,5 MB.

* __Abertura e fechamento da EpisodeDetailActivity:__ Abrindo e fechando várias vezes a tela de detalhe do podcast, o consumo de memória subiu a cada abertura da MainActivity, chegando a 18 MB, porém depois de um tempo regrediu para 16 MB.

* __Scroll na tela inicial:__ Indo até o fim da lista e voltando algumas vezes, o consumo de memória ficou em 15,5 MB. Mantendo o dedo na tela e deslizando várias vezes o uso de memória atingiu um pico de 15,5 MB.

* __Execução do Garbage Collector:__ O garbage collector reduziu o consumo de memória para 12,5 MB.

* __Download de podcast:__ Comportamento similar ao do teste anterior, oscilando entre 14 e 16 MB.

* __Segundo plano:__ Ao deixar o aplicativo em segundo plano, o uso de memória desceu para 12 MB.

O singleton não melhorou nenhum dos memory leaks.
