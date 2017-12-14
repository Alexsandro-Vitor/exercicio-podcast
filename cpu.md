# Análise do uso de CPU

## Android Profiler

Os seguintes testes foram feitos para avaliar o uso de CPU do aplicativo.
Todos os testes, com exceção da abertura do aplicativo, foram feitos partindo do app recém-aberto.

* __Abertura do aplicativo:__ Ao abrir o aplicativo a CPU atinge um uso de 30% e em dois segundos desce a 0%.

* __Abertura e fechamento da EpisodeDetailActivity:__ Após abrir e fechar várias vezes a tela de detalhe do podcast, o uso da CPU chegou a 50%. Voltando da tela de detalhe para a tela principal uma única vez, o consumo chegou apenas aos 30% da abertura do aplicativo.

* __Scroll na tela inicial:__ Após ir até o fim da lista de podcasts e voltar algumas vezes, o uso de memória subiu para 20%. Em outro teste, mantendo o dedo na tela e deslizando várias vezes o uso de memória subiu para 14%.

* __Download de podcast:__ Ao iniciar o download de um podcast, o uso de CPU se manteve quase sempre entre 2 e 5%, não chegando a 7%.

* __Segundo plano:__ Ao deixar o aplicativo em segundo plano, o uso de CPU não mudou. Retornando para o app, o consumo de CPU subiu para 
cerca de 40%.

* __Execução de podcast:__ Ao iniciar a execução de um podcast, o consumo de CPU tem um curto pico de 14%.

Em todos os casos o consumo de CPU baixa para 0% depois de um tempo.

### Modificação 1: RecyclerView ###

A ListView utilizada no app foi substituída por uma RecyclerView com a intenção de reduzir a quantidade de memória liberada pelo garbage collector. Em seguida, os testes acima foram refeitos para comparação.

A implemntação consistiu em trocar a classe do ListView de MainActivity para RecyclerView e substituir o XmlFeedAdapter pela implementação em RecyclerXmlFeedAdapter.

* __Abertura do aplicativo:__ Ao abrir o aplicativo a CPU atingiu um pico de 36%.

* __Abertura e fechamento da EpisodeDetailActivity:__ Após abrir e fechar várias vezes a tela de detalhe do podcast, o uso da CPU chegou a picos de 46%.

* __Scroll na tela inicial:__ Indo até o final da lista e voltando, o uso de CPU chegou a 25%. Esse uso de CPU foi menor na volta ficando entre 10 e 20%.

* __Download de podcast:__ Ao iniciar o download de um podcast, houve um pico de 11%, depois o uso de CPU ficou quase sempre abaixo de 2%.

* __Segundo plano:__ Ao deixar o aplicativo em segundo plano, o uso de CPU não mudou. Retornando para o app, o consumo de CPU subiu para 
cerca de 40%.

* __Execução de podcast:__ Ao iniciar a execução de um podcast, o consumo de CPU tem um curto pico de 11%.

Em todos os casos o consumo de CPU baixa para 0% depois de um tempo.

Comparando a versão modificada com a original, pode-se perceber que o RecyclerView sozinho não necessariamente economiza processamento, com todas as operações o envolvendo tendo o uso de CPU aumentado.

### Modificação 2: Singleton + Checagem de atualização ###

Foi feito um singleton do RecyclerXmlFeedAdapter com o objetivo de evitar múltiplas criações do mesmo, devido a seu tamanho considerável. Porém, também era preciso reduzir o número de criações da lista de objetos do adapter, já que ela era uma grande parte do mesmo. Isso foi feito checando quando foi a última atualização do XML e só criando um novo ListView quando o antigo estiver desatualizado.

* __Abertura do aplicativo:__ Ao abrir o aplicativo a CPU atingiu um pico de 40%, o que era esperado, já que ao abrir, o aplicativo ainda não gerou o singleton.

* __Abertura e fechamento da EpisodeDetailActivity:__ Após abrir e fechar várias vezes a tela de detalhe do podcast, o uso da CPU chegou a picos de 27%.

* __Scroll na tela inicial:__  Indo até o final da lista e voltando, o uso de CPU se manteve entre 10 e 20%, com pico de 21%. Com o movimento rápido do dedo para cima e para baixo, o pico foi de 16%.

* __Download de podcast:__ Comportamento similar ao teste anterior.

* __Segundo plano:__ Comportamento similar ao teste anterior.

* __Execução de podcast:__ O consumo de CPU se manteve baixo, sem picos.

Em todos os casos o consumo de CPU baixa para 0% depois de um tempo.

O maior efeito foi com o EpisodeDetailActivity, ao retornar para a MainActivity com um singleton do adapter e sem a necessidade de realizar um novo download da lista de podcasts, foi possível reduzir o consumo de CPU reduziu consideravelmente na transição entre telas.
