# TODO:
### priorità alta:
- [x] fix confermaRicezione
- [x] ordinamento delle operazioni in metodi opportuni
- [x] whisper
- [ ] codice 300
- [ ] codice 310
- [x] codice 320
- [x] disponibilità nomeClient alla connessione
- [x] /nick
- [ ] ...

### priorità bassa:
- [x] cambio numero porta
- [ ] sleep 100 -> 10 ms
- [x] Nomi client univoci
- [ ] Cambio canale
- [ ] Canali Multipli
- [ ] Controllo Duplicati
- [ ] Replica protocollo reale
- [ ] studio protocollo reale (x idee implementazioni)
- [ ] documentazione completa codice
- [ ] documentazione completa protocollo
- [ ] unit testing
- [ ] ...

## Altro:
- [x] Git
- [x] README e TODO
- [ ] ...

## dettagli:
ordinamento dei metodi:
    dalla ricezione dentro run al chiamare un metodo ricevi() dentro run [Client]
    dall'inizializzazione di connessione dentro run al chiamare un metodo connetti() [ThreadCommunication]

cambio numero porta:
    la porta 4321 non è nel range di porte libere [49152 - 65535] quindi va cambiata in un'altra es. 42069

codice 300:
    alert avviso entrata/uscita di client nei canali
    [conferma delivery: 301]

codice 310:
    retrieve numero participanti al canale
    [conferma delivery: 311]

codice 320:
    (integrato nel codice 100 alla prima connessione)
    il client richiede al canale la validità/disponibilità del nome utente inserito
    se il nome richiesto è accettabile risponde con lo stesso, altrimenti risponde con il vecchio nome
    (fa eccezione l'inizializzazione, tramite codice 100, dove viene forzatamente generato e assegnato un nome client generato casualmente (l'utente potrà poi richiedere di modificarlo in futuro))
    in poche parole, la risposta contiene il nome da assegnare/sovrascrivere al client
    [conferma delivery: 321]

    integrità nome client inseriti dall'utente:
        ammette solo a-z A-Z 0-9
        non inizia con 'Client'
        no null o vuoto

/nick:
    richesta disponibilità nuovo nome utente
    eventuale aggiornamento/applicazione nome (vedi risposta a 320)
