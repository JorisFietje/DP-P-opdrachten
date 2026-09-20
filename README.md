# Data & Persistency — P-opdrachten

Joris Fietje — herkansing Data & Persistency, Hogeschool Utrecht.

Dit is het project voor de **P-opdrachten**. Het bevat **geen Hibernate-code**: geen
Hibernate-DAO's, geen `persistence.xml`, geen `hibernate-core` dependency en geen
Hibernate-tests. De PnH-opdrachten staan in een aparte repository.

## Inhoud

| Map | Inhoud |
|---|---|
| `practicum/` | Het persistentiepracticum: DAO-pattern met JDBC op PostgreSQL |
| `opdracht/` | De SQL-opdrachten S1 t/m S9 |
| `casus/` | SQL om de casusdatabases (ov-chipkaart, bedrijf, sales) aan te maken |

## Opzet van `practicum/src/main/java`

| Package | Inhoud |
|---|---|
| `domain` | De POJO's: `Reiziger`, `Adres`, `OvChipkaart`, `Product` — geen SQL |
| `infra.dao` | De DAO-interfaces en hun PostgreSQL-implementaties |
| `globals` | Databaseconfiguratie |
| `Main` | `testReizigerDAO()`, waarin elke CRUD-operatie wordt uitgevoerd |

## Zelf draaien

De databaseverbinding wordt via dependency injection aan de DAO's meegegeven. Vul vóór
het draaien je eigen PostgreSQL-gegevens in bij
`practicum/src/main/java/globals/Database.java`:

```java
public static final String dbUserName = "postgres";
public static final String dbPassword = "...";
```

`Main` draait tegen een database met de naam `ovchip`, aan te maken met de scripts in
`casus/ovchip/`. Het testraamwerk maakt zijn eigen database aan en ruimt die daarna op.

```bash
cd practicum
mvn test
```
