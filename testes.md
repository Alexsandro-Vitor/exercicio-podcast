# Testes do Aplicativo

Foram feitos testes das seguintes classes:

### PodcastProvider

Esta classe teve 5 de seus 6 métodos públicos testados (getType não foi testado por não ser usado na aplicação). Originalmente era pra ser um teste de unidade, porém o método onCreate() chama o método getContext() em sua execução, que só pode ser utilizado em testes instrumentados.

* __Preparo dos testes__: O teste de cada função é feito em um DB contendo apenas um item, sendo limpo e tendo a mesma entrada incluída após cada teste

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

* __delete()__: Tenta remover um objeto que contenha o mesmo título do único objeto do DB, funciona se conseguir remover 1 objeto.

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

* __onCreate()__: Executa o método onCreate() de PodcastProvider. Se e somente se o provider tiver obtido uma instância do PodcastDBHelper, retorna true. Funciona se provider tiver obtido esta instância.

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

* __query()__: Tenta buscar um objeto que está no DB por seu título, funciona se conseguir obter um.

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

* __update()__: Tenta alterar a descrição de um objeto no DB, funciona se conseguir alterar um objeto.

```java
@Test
public void update() throws Exception {
    String[] selectionArgs = {(String) cv.get(PodcastProviderContract.TITLE)};
    cv.put(PodcastProviderContract.DESC, "Descricao 2");
    assertEquals(1, provider.update(PodcastProviderContract.EPISODE_LIST_URI, cv,
            PodcastProviderContract.TITLE + " =?", selectionArgs));
}
```