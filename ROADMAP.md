# ShopFlow API — Roadmap di apprendimento Spring Boot

> Un backend e-commerce / order management costruito **per fasi**, dove ogni fase
> aggiunge una competenza concreta e spendibile sul CV. Non è un progetto
> "lo finisco una volta": è un percorso. Ogni fase = un traguardo su Git + una
> riga in più nel curriculum.

---

## Filosofia del progetto

1. **Si parte semplice e si stratifica.** La Fase 0–1 è quasi banale apposta:
   serve a consolidare ciò che hai già visto nel corso. Da lì in poi aggiungiamo
   un mattone alla volta.
2. **Ogni fase è un "Definition of Done".** Finché i criteri di una fase non sono
   verdi (test passano, codice committato, README aggiornato), non si passa oltre.
3. **Git è parte dell'esercizio, non un dettaglio.** Ogni fase si sviluppa su un
   branch dedicato e si chiude con una Pull Request (anche se sei solo tu: serve
   a imparare il workflow aziendale).
4. **Il README cresce col progetto.** A fine percorso il README è la tua vetrina.

---

## Il dominio: ShopFlow

Un'API per un negozio online. Le entità principali, introdotte gradualmente:

- **Product** — un prodotto in vendita (nome, descrizione, prezzo, stock).
- **Category** — categoria a cui appartiene un prodotto (relazione).
- **User** — utente registrato (con ruoli: `CUSTOMER`, `ADMIN`).
- **Cart / CartItem** — carrello dell'utente.
- **Order / OrderItem** — ordine confermato, con stato e logica di business.

È un dominio piccolo abbastanza da non perdersi, ma ricco abbastanza da toccare
relazioni, transazioni, sicurezza e (più avanti) microservizi.

---

## Stack tecnologico (cresce con le fasi)

| Area               | Tecnologia                                  |
|--------------------|---------------------------------------------|
| Linguaggio         | Java 21 (LTS)                               |
| Framework          | Spring Boot 3.x                             |
| Build              | Maven (o Gradle, a scelta)                  |
| Database           | H2 (inizio) → PostgreSQL (Fase 2+)          |
| Migrazioni DB      | Flyway                                      |
| Sicurezza          | Spring Security + JWT                       |
| Testing            | JUnit 5, Mockito, MockMvc, Testcontainers   |
| Documentazione API | springdoc-openapi (Swagger UI)              |
| Container          | Docker + docker-compose                     |
| CI/CD              | GitHub Actions                              |
| Avanzato           | Redis (cache), Kafka (eventi), Spring Cloud |

---

## Come lavoreremo insieme (workflow ricorrente)

Per **ogni** fase ripeti questo ciclo. È sempre lo stesso, così lo interiorizzi:

```
1. git checkout main && git pull
2. git checkout -b feature/fase-N-nome
3. [sviluppi la fase, io ti guido e rivedo il codice]
4. scrivi/aggiorni i test → devono passare
5. aggiorni README.md e segni la fase come completata in questa roadmap
6. git add . && git commit -m "feat: <descrizione fase N>"
7. git push -u origin feature/fase-N-nome
8. apri una Pull Request su GitHub, la rileggi come se fossi un revisore, mergi
9. tagghi il traguardo: git tag v0.N && git push --tags
```

**Convenzione commit (Conventional Commits):** `feat:`, `fix:`, `test:`, `docs:`,
`refactor:`, `chore:`. Fa molto "developer professionista" su un repo pubblico.

**Il mio ruolo:** in ogni fase ti spiego il concetto, ti do la struttura, ti faccio
scrivere il codice (così impari davvero), poi lo rivediamo insieme e correggiamo.
Quando ti blocchi, partiamo da dove sei, non da capo.

---

## Le fasi

### Fase 0 — Setup e fondamenta del progetto
**Obiettivo:** avere uno scheletro che parte e un repo pulito.
- Progetto da Spring Initializr (Web, Spring Boot DevTools, Actuator, Lombok).
- Struttura a package per layer: `controller`, `service`, `repository`, `model`, `dto`.
- Endpoint `/health` (o Actuator) che risponde.
- `README.md` iniziale + `.gitignore` Java + primo push su GitHub.

**Sblocca sul CV:** *"Bootstrap di applicazioni Spring Boot, struttura a layer."*
**Done quando:** l'app parte, l'endpoint risponde, il repo è online.

---

### Fase 1 — CRUD base (consolidamento del corso)
**Obiettivo:** rifare con le tue mani il cuore del corso, ma sul tuo dominio.
- Entità `Product` (per ora in memoria o H2).
- `ProductController` con GET (lista + singolo), POST, PUT, DELETE.
- DTO con i `record` Java + mapping entity↔DTO.
- Status code corretti (200, 201 + header `Location`, 204, 404).

**Sblocca sul CV:** *"Progettazione e implementazione di API REST CRUD."*
**Done quando:** tutti i verbi funzionano e li hai testati (anche solo con Postman/curl).

---

### Fase 2 — Persistenza vera: JPA + PostgreSQL
**Obiettivo:** passare dal giocattolo a un DB reale. **Questo è il salto più importante.**
- Spring Data **JPA** (al posto di JDBC).
- **PostgreSQL** avviato via Docker (`docker-compose.yml`).
- Relazione `Product` ↔ `Category` (`@ManyToOne` / `@OneToMany`).
- Migrazioni con **Flyway** (niente `ddl-auto: update` in produzione).
- Query derivate (`findByCategoryName`) e una `@Query` custom.

**Sblocca sul CV:** *"Spring Data JPA/Hibernate, modellazione relazionale, PostgreSQL, Flyway."*
**Done quando:** i dati persistono dopo il riavvio e le relazioni funzionano.

---

### Fase 3 — Validazione, errori, paginazione
**Obiettivo:** rendere l'API robusta e professionale.
- Bean Validation (`@NotBlank`, `@Positive`, `@Valid` sui body).
- Gestione errori centralizzata con `@RestControllerAdvice` e risposte d'errore consistenti (formato JSON uniforme).
- Paginazione e ordinamento (`Pageable`, `Page<T>`).

**Sblocca sul CV:** *"Validazione input, error handling centralizzato, paginazione."*
**Done quando:** un input non valido restituisce un 400 ben formattato, non uno stacktrace.

---

### Fase 4 — Sicurezza con JWT
**Obiettivo:** autenticazione e autorizzazione reali. Skill molto richiesta.
- Entità `User` + `Role`.
- Endpoint `/auth/register` e `/auth/login`.
- Spring Security + **JWT** (token al login, filtro che lo valida).
- Autorizzazione per ruolo: solo `ADMIN` crea/elimina prodotti; `CUSTOMER` legge.
- Password con BCrypt.

**Sblocca sul CV:** *"Spring Security, autenticazione JWT, autorizzazione role-based."*
**Done quando:** senza token sei bloccato, con token del ruolo giusto passi.

---

### Fase 5 — Dominio ricco: carrello e ordini
**Obiettivo:** logica di business vera, non solo CRUD.
- `Cart` / `CartItem`: aggiungi/rimuovi prodotti.
- `Order` / `OrderItem`: conferma ordine, calcolo totale, decremento stock.
- Transazioni con `@Transactional` (se qualcosa fallisce, rollback completo).
- Stato dell'ordine (`PENDING`, `PAID`, `SHIPPED`) come enum.

**Sblocca sul CV:** *"Logica di business transazionale, gestione ordini e inventario."*
**Done quando:** un ordine aggiorna lo stock in modo atomico e coerente.

---

### Fase 6 — Testing serio
**Obiettivo:** dimostrare che sai testare. Quasi sempre richiesto negli annunci.
- Unit test dei service con **Mockito** (mock dei repository).
- Test del web layer con **MockMvc** (`@WebMvcTest`).
- Integration test con **Testcontainers** (PostgreSQL vero in un container, in test).
- Misura la coverage (JaCoCo) e punta a un valore ragionevole sul codice di dominio.

**Sblocca sul CV:** *"Unit, web e integration testing (JUnit 5, Mockito, Testcontainers)."*
**Done quando:** `mvn test` è verde e copre i percorsi principali.

---

### Fase 7 — Documentazione e qualità
**Obiettivo:** un'API che altri possono usare e leggere.
- **springdoc-openapi** → Swagger UI auto-generato su `/swagger-ui.html`.
- Profili Spring (`dev`, `prod`) con configurazioni separate.
- Logging strutturato e configurazione via variabili d'ambiente (niente segreti nel codice).

**Sblocca sul CV:** *"Documentazione OpenAPI/Swagger, gestione profili e configurazione."*
**Done quando:** un estraneo capisce e prova l'API solo dallo Swagger.

---

### Fase 8 — Containerizzazione e CI/CD
**Obiettivo:** portare il progetto verso lo standard "production-ready".
- **Dockerfile** multi-stage per l'app.
- **docker-compose** che avvia app + PostgreSQL insieme.
- **GitHub Actions**: pipeline che a ogni push compila ed esegue i test.
- (Opzionale) badge di build verde nel README.

**Sblocca sul CV:** *"Docker, docker-compose, CI/CD con GitHub Actions."*
**Done quando:** `docker-compose up` avvia tutto e la CI è verde su GitHub.
> Nota: qui parti avvantaggiato, hai già usato Docker e GitHub Actions nei tuoi progetti.

---

### Fase 9 — "In grande": il salto verso i microservizi
**Obiettivo:** la parte che ti distingue da un altro junior.
- Caching con **Redis** (es. cache del catalogo prodotti).
- Eventi asincroni: alla conferma di un ordine, pubblichi un evento su **Kafka**
  (un `notification-service` separato lo consuma e "invia" la conferma).
- Splitting in microservizi: `product-service` + `order-service` che comunicano.
- (Opzionale) API Gateway + service discovery con Spring Cloud.

**Sblocca sul CV:** *"Architettura a microservizi, messaggistica con Kafka, caching Redis."*
**Done quando:** un ordine confermato genera un evento consumato da un altro servizio.

---

### Fase 10 — Deploy in cloud
**Obiettivo:** un link funzionante da mettere nel CV.
- Deploy su un provider con free tier (Railway, Render, o AWS).
- Database gestito in cloud.
- URL pubblico dell'API + Swagger raggiungibile online.

**Sblocca sul CV:** *"Deploy e gestione di applicazioni in cloud."*
**Done quando:** l'API risponde da un URL pubblico.

---

## Mappa: fase → competenza CV

| Fase | Competenza chiave per il colloquio           |
|------|----------------------------------------------|
| 1    | REST API, CRUD, DTO                          |
| 2    | JPA/Hibernate, PostgreSQL, relazioni, Flyway |
| 3    | Validazione, error handling, paginazione     |
| 4    | Spring Security, JWT, ruoli                  |
| 5    | Logica di business, transazioni              |
| 6    | Testing (unit/integration), Testcontainers   |
| 7    | OpenAPI/Swagger, profili, configurazione     |
| 8    | Docker, CI/CD                                |
| 9    | Microservizi, Kafka, Redis                   |
| 10   | Cloud deploy                                 |

Già dalla **Fase 6** hai un progetto solido da mettere sul CV. Le fasi 7–10 lo
trasformano da "esercizio" a "portfolio che impressiona".

---

## Da dove partiamo ORA

1. Decidi il nome del repo (suggerito: `shopflow-api`) e se Maven o Gradle.
2. Generiamo insieme il progetto da Spring Initializr (Fase 0).
3. Primo commit + push su GitHub con questa `ROADMAP.md` dentro.
4. Passiamo alla Fase 1.

> Aggiorna questo file man mano: metti una ✅ accanto a ogni fase completata e la
> data. A fine percorso questa roadmap racconta da sola il tuo progresso.

### Stato avanzamento
- [x] Fase 0 — Setup
- [ ] Fase 1 — CRUD base
- [ ] Fase 2 — JPA + PostgreSQL
- [ ] Fase 3 — Validazione & errori
- [ ] Fase 4 — Sicurezza JWT
- [ ] Fase 5 — Carrello & ordini
- [ ] Fase 6 — Testing
- [ ] Fase 7 — Documentazione & qualità
- [ ] Fase 8 — Docker & CI/CD
- [ ] Fase 9 — Microservizi
- [ ] Fase 10 — Cloud deploy
