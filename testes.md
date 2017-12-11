# Testes do Aplicativo

Foram feitos testes das seguintes classes:

### PodcastProvider

Esta classe teve 5 de seus 6 m�todos p�blicos testados (getType n�o foi testado por n�o ser usado na aplica��o). Originalmente era pra ser um teste de unidade, por�m o m�todo onCreate() chama o m�todo getContext() em sua execu��o, que s� pode ser utilizado em testes instrumentados.

* __Preparo dos testes__: O teste de cada fun��o � feito em um DB contendo apenas um item, sendo limpo e tendo a mesma entrada inclu�da ap�s cada teste

```java
@Before
public void setUp() throws Exception {
    provider.onCreate();
    cv = new ContentValues();
    cv.put(PodcastProviderContract.TITLE, "Titulo 1");
    cv.put(PodcastProviderContract.LINK, "Link 1");
    cv.put(PodcastProviderContract.DATE, "Data 1");
    cv.put(PodcastProviderContract.DESC, "Descricao 1");
    cv.put(PodcastProviderContract.DOWNLOAD_LINK, "Link 1");
    cv.put(PodcastProviderContract.FILE_URI, "");
    provider.insert(PodcastProviderContract.EPISODE_LIST_URI, cv);
}

@After
public void tearDown() throws Exception {
    provider.delete(PodcastProviderContract.EPISODE_LIST_URI, null, null);
}
```

* __delete()__: Tenta remover um objeto que contenha o mesmo t�tulo do �nico objeto do DB, funciona se conseguir remover 1 objeto.

```java
@Test
public void delete() throws Exception {
    String[] selectionArgs = {(String) cv.get(PodcastProviderContract.TITLE)};
    assertEquals(1, provider.delete(PodcastProviderContract.EPISODE_LIST_URI,
            PodcastProviderContract.TITLE + " =?",
            selectionArgs));
}
```

* __insert()__: Insere um novo objeto no DB. Funciona se retornar uma Uri.

```java
@Test
public void insert() throws Exception {
    ContentValues cv = new ContentValues();
    cv.put(PodcastProviderContract.TITLE, "Titulo 2");
    cv.put(PodcastProviderContract.LINK, "Link 2");
    cv.put(PodcastProviderContract.DATE, "Data 2");
    cv.put(PodcastProviderContract.DESC, "Descricao 2");
    cv.put(PodcastProviderContract.DOWNLOAD_LINK, "Link 2");
    cv.put(PodcastProviderContract.FILE_URI, "");
    assertNotNull(provider.insert(PodcastProviderContract.EPISODE_LIST_URI, cv));
}
```

* __onCreate()__: Executa o m�todo onCreate() de PodcastProvider. Se e somente se o provider tiver obtido uma inst�ncia do PodcastDBHelper, retorna true. Funciona se provider tiver obtido esta inst�ncia.

```java
//Teste
@Test
public void onCreate() throws Exception {
    assertTrue(provider.onCreate());
}
```

```java
//PodcastProvider
@Override
public boolean onCreate() {
    db = PodcastDBHelper.getInstance(getContext());
    return db != null;
}
```

* __query()__: Tenta buscar um objeto que est� no DB por seu t�tulo, funciona se conseguir obter um.

```java
@Test
public void query() throws Exception {
    String[] selectionArgs = {(String) cv.get(PodcastProviderContract.TITLE)};
    Cursor cursor = provider.query(PodcastProviderContract.EPISODE_LIST_URI, PodcastProviderContract.ALL_COLUMNS,
            PodcastProviderContract.TITLE + " =?", selectionArgs, null);
    assertNotNull(cursor);
    assertTrue(cursor.getCount() > 0);
}
```

* __update()__: Tenta alterar a descri��o de um objeto no DB, funciona se conseguir alterar um objeto.

```java
@Test
public void update() throws Exception {
    String[] selectionArgs = {(String) cv.get(PodcastProviderContract.TITLE)};
    cv.put(PodcastProviderContract.DESC, "Descricao 2");
    assertEquals(1, provider.update(PodcastProviderContract.EPISODE_LIST_URI, cv,
            PodcastProviderContract.TITLE + " =?", selectionArgs));
}
```