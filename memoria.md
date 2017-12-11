# An�lise do consumo de mem�ria

## Android profiler ##

* __Abertura do aplicativo:__ O aplicativo quando aberto inicialmente se manteve com um consumo de mem�ria entre 11 e 12 MB. Todos os testes ap�s este foram feitos partindo do app rec�m-aberto.

* __Abertura e fechamento da EpisodeDetailActivity:__ Ap�s abrir e fechar v�rias vezes a tela de detalhe do podcast, o consumo de mem�ria subiu para 16 MB, por�m depois de um tempo regrediu para pouco menos de 14 MB.

* __Scroll na tela inicial:__ Ap�s ir at� o fim da lista de podcasts e voltar algumas vezes, o uso de mem�ria subiu para 16 MB, depois desceu para 14 MB. Em outro teste, mantendo o dedo na tela e deslizando v�rias vezes o uso de mem�ria subiu para 15,5 MB e depois desceu para 15 MB.

* __Download de podcast:__ Ao inciar o download do podcast "Ci�ncia e Pseudoci�ncia", o uso de mem�ria subiu pouco, chegnado a 11,79 MB. Quando o download terminou, esse valor subiu para 12 MB e permaneceu nele.

* __Segundo plano:__ Ao deixar o aplicativo em segundo plano, o uso de mem�ria desceu para 10 MB.