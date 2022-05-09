# Gruppe: 6 Chicks

### Fordeling av roller:

Emil - gruppeleder<br>
For å distribuere og utlevere oppgaver kan det være lurt å ha en som tar en lederrolle.

Olaug - skrive ansvarlig<br>
For å sikre at de skriftlige dokumentene oppholder en viss standard, og ikke er full av rot.

Thorgal - kodeansvarlig<br>
For å sikre at koden er ryddig og holder en lignende stil gjennom hele prosjektet har vi valgt å ha ein kode ansvarlig som ser igjennom og “tweaker” på koden.

Åsmund - testansvarlig<br>
Samme som med kode-ansvarlig, så trenger tester å bli sett på og kvalitetssjekket.

Jonarthan - grafisk ansvarlig<br>
Når vi kommer til punktet hvor vi kan begynne å få prosjektet til å se bra ut trenger vi en som faktisk kan noko med karakterbygging og grafisk design/illustrasjon.

Mikal - Moralsk støtteperson :-)<br>
Blir en “alt mulig mann” i prosjektet. Stiller opp og hjelper hos dem som trenger det. 



### Prosess- og prosjektplanen/Prosjektmetodikk:

Scrum og parprogrammering (parprogrammering så det alltid er to personer som vet hva en del av en kode gjør, og unngå feil). Ved å bruke scrum metodikk vil arbeidet brytes ned i små blokker, og det vil bli gitt en tidsfrist for å nå “sprinten”.



### Metoder som hjelper å utvikle et veldokumentert programvare:

Trello, brukerhistorier, Git (ved commit, nevne hvem som har vore med på parprogrammering). Vi har valgt å bruke trello slik at vi til enhver tid kan se hva som har blitt gjort, hva som trengs å bli gjort og gjerne hvem som skal gjennomføre de ulike oppgavene. Disse gjøremålene kan få ulik prioriteringsgrad i trello. Har laget et gruppeprosjekt i git av oppgaven hvor vi alle kan commite våre siste endringer, og alle kan se endringene i prosjektet. Vi bruker også Discord for å kommunisere og dele ressurser. I tillegg har vi en git-commit-bot i Discord som sender ut melding om nye pushes på tvers av alle branchene, noe som gjør det enklere å holde styr på endringer som blir gjort.



### Organisering av møter:
I tillegg til mandags gruppe time har vi også et møte på fredager fra 10 - 14, flere dersom det blir nødvendig.


### Tildeling av oppgaver:
På møter blir det bestemt hvem som skal gjøre hva til hver sprint.


### Oppfølging av arbeid:
Gruppetimen på mandag skal bli brukt til å fordele arbeidsoppgaver og diskutere plan for uke. 
Fredagsmøte er satt av til arbeid, og gjennomgang av ukas arbeid.



### Målet for applikasjonen:

Brukerhistorier basert på MVP.
- Akseptansekriterier
- Arbeidsoppgåver
- Hvilke krav fyller brukerhistoriene


### BrukerHistorier:

##### Liste med brukerhistorier som skal med i første iterasjon:

For programmereren:
Som en gruppe vil eg bruke samme programmerings metoder for å gjøre kode-snittene mer oversiktlig og kompatibel med hverandre (unngå spaghetti kode).
Om en programmerer vil eg ha gode kommentarer i koden slik at det blir lettere å ta i bruk andres kode, med forståelse.
Som en programmerer vil jeg ha en ryddig og organisert kode. f.eks. state machines.

MVP: (Alle punktene er ikkje MVP, men er oppgitt som MVP i oppgaven)
- Som en spiller, vil eg ha noen fiender som går mot meg for å få en større utfordring // 6 Vise fiender/monstre; de skal interagere med terreng og spiller
- Som en spiller, vil eg ha et stort rom å kunne bevege meg i og interaktere med // 1 Viser et spillebrett, //4 Spiller interagerer med terreng
- Som en spiller vil eg ha et klart og tydelig mål for å vinne. // 8 Mål for spillbrett (enten et sted, en mengde poeng, drepe alle fiender e.l.)
- Som spiller vil eg ha en intuitiv og lettlært måte å bevege spill-karakteren min. // 3 Flytte spiller (hva taster e.l.)
- Som en spiller ønsker jeg noe å samle, f.eks. poeng og/eller ekstra liv // 5 Spiller har poeng og interagerer med poeng-gjenstander 
- Som spiller, vil eg ha eit risikoelement som skaper spenning, f.eks: å kunne dø (i spillet). // 7 Spiller kan dø (ved kontakt med fiender, eller ved å falle utfor skjermen)
- Som spiller vil eg ha en karakter som eg kan vere // 2 Vise spiller på spillebrett

Ikke MVP:
- Som en spiller trenger jeg noen lyder (audio) for å bli oppslukt i spillet.
- Som en spiller, vil eg lagre “progressen”.
- Som en spiller vil eg ha en ryddig og oversiktlig start-meny, med mulighet for å endre innstillinger. // 10 Start-skjerm ved oppstart / game over
- Som en programmerer vil jeg ha et TileMap (Tiled) for å gjøre det raskere å bygge level.  
- Som en spiller vil eg kunne bytte spill-nivå uten bry, når eg er ferdig med eit tidligere eit. Og velge å gå tilbake til tidligere levels. // 9 Nytt spillbrett når forrige er ferdig
- Som en programmerer vil eg ha gode metoder for å lett legge til nye spillbrett, i en eventuell “Level Select”.
- Som spiller synes eg det er gøy å kunne spille med mine venner, og vil dermed ha mulighet til å gjøre det. (Multiplayer) // 11 Støtte flere spillere (enten på samme maskin eller over nettverk).
- Som en spiller vil jeg ha et optimalisert spill som ikke bruker unødvendig lang tid/ressurser (BIG O)
- Som spiller vil eg velge knapper sjølv til å styre karakteren min, og som spiller liker eg å bruke playstation kontrolleren min.


Programmerings-stil (dvs Lik programmeringsmetode og indetering):
– Kommentar over hver metode og test (/**	…   **/)
- Java Standard (minMetode(), ikkje min_metode())
- Bruk av State Machines der det gir mening? (player, enemies, boss aka mer kompleks oppførsel).


Manuelle tester.
Teste bevegeligheten av player:

Step 1:
Kjøre main:
 - spillet starter og man ser et vindu med en Player og et Gameboard.

Step 2:
Bevege spiller:
- Trykke “W” og ser at player beveger seg oppover.
- Trykke “A” og ser at player beveger seg til venstre.
- Trykke “S” og ser at player beveger seg nedover.
- Trykke “D” og ser at player beveger seg til høyre.
- Trykke “W + D” og ser at player beveger seg skrått Oppover mot Høyre.
- Trykke “S + D” og ser at player beveger seg skrått Nedover mot Høyre.
- Trykke “W + A” og ser at player beveger seg skrått Oppover mot Venstre.
- Trykke “S + A” og ser at player beveger seg skrått Nedover mot Venstre.
- Trykke “A + D” og ser at player står stille.
- Trykke “W + S” og ser at player står stille.



- Trykke “Pil-opp” og ser at player beveger seg oppover.
- Trykke “Pil-venstre” og ser at player beveger seg til venstre.
- Trykke “Pil-ned” og ser at player beveger seg nedover.
- Trykke “Pil-høyre” og ser at player beveger seg til høgre.
- Trykke “Pil-Høyre + Pil-Opp” og ser at player beveger seg skrått Oppover mot Høyre.
- Trykke “Pil-Høyre + Pil-Ned” og ser at player beveger seg skrått Nedover mot Høyre.
- Trykke “Pil-Venstre + Pil-Opp” og ser at player beveger seg skrått Oppover mot Venstre.
- Trykke “Pil-Venstre + Pil-Ned” og ser at player beveger seg skrått Nedover mot Venstre.
- Trykke “Pil-Venstre + Pil-Høyre” og ser at player står stille.
- Trykke “Pil-Opp + Pil-Ned” og ser at player står stille.

- Trykke “W + Pil-Venstre” og ser at player beveger seg oppover mot Venstre.

### Oppsummering:

Ved dette prosjektet har vi møtt på både oppturer og nedturer. En del av problemet er at vi har hatt litt sykdom i gruppa, hvor da ikke alle har kunne møtt til de planlagte møtene. Nå i startfasen har det vært litt vanskelig å fordele arbeid siden det meste har vært planlegging, noe som vi har syntes å være best å gjøre i fellesskap. I og med at det i oblig 1 har vært mye planlegging har ikke metodikken scrum vært enkelt å utføre, heller ikke parprogrammering. Det vi opplevde var derimot at vi raskt kom i gang med arbeidet, og var tidlig ferdig med alt av planlegging. Det har derfor blitt gjort en del programmering allerede av det mest grunnleggende slik som spillbrett, player, osv. 

For oss har det foreløpig holdt med å møtes to ganger i uken. Det vi må bli flinkere til ved de neste obligatoriske innleveringene er å utføre de metodikkene som vi har planlagt å bruke. Vi ser for oss at dette blir lettere når det er mer konkrete oppgaver i programmeringen som skal utføres. I tillegg har vi ikke laget noen tester foreløpig, noe vi må gjøre senere i programmeringen. Da er planen at hver enkelt skriver test til det dem selv utfører, og så vil testansvarlig se gjennom i etterkant og se om testen er nyttig og god nok. 

Foreløpig har gruppa fungert godt, og vi har ikke enda noen konkrete aktiviteter eller verktøy vi ønsker å prøve. Dette kan selvsagt endre seg når vi kommer lengre ut i prosjektet.

### Link til Trello (Prosjektplanen)
Link: https://trello.com/invite/b/toDOLHwu/2e8eeb822e39ff808d991dd147805219/obligatorisk-oppg%C3%A5ve-1

