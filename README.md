#  Java ATM Simulator - Console Application

Acesta este un simulator de bancomat (ATM) , dezvoltat în Java, care gestionează utilizatori, conturi bancare și un istoric complet de tranzacții folosind fișiere text pe post de bază de date.

##  Caracteristici Principale

### Modul Utilizator
* **Autentificare securizată**: Acces pe bază de ID Cont și PIN.
* **Interfață Intuitivă**: Meniu interactiv pentru operațiuni rapide.
* **Gestionare Fonduri**: Depunere, retragere și transfer între conturi.
* **Extras de Cont**: Vizualizarea istoricului tranzacțiilor financiare (filtrat pentru a exclude evenimentele de securitate).
* **Schimbare PIN**: Opțiune de actualizare a codului de acces direct din consolă.

### Modul Administrator
* **Audit Log Complet**: Monitorizarea tuturor activităților din sistem (inclusiv creări/ștergeri de conturi și schimbări de PIN).
* **Gestionare Clienți**: Crearea automată de profiluri (User + Cont) cu generare de ID-uri unice.
* **Închidere Conturi**: Logica de business care previne ștergerea conturilor cu sold pozitiv.
* **Monitorizare în timp real**: Afișarea tranzacțiilor formatată tabelar pentru o citire ușoară.

## Tehnologii Utilizate
* **Java 17+**: Utilizarea conceptelor de POO (Încapsulare, Moștenire, Polimorfism).
* **File I/O**: Gestiunea persistenței datelor prin `BufferedReader` și `BufferedWriter`.
* **Maven**: Gestionarea structurii proiectului și a dependențelor.
* **Arhitectură N-Tier**: Separarea clară între Modele, Repository, Service și Controller.

## Structura Proiectului
* `com.atmsimulator.model`: Clasele de date (`User`, `Account`, `Transaction`).
* `com.atmsimulator.repository`: Logica de scriere/citire din fișierele `.txt`.
* `com.atmsimulator.service`: Logica de business (calcule, validări, securitate).
* `com.atmsimulator.controller`: Gestionarea interacțiunii cu utilizatorul în consolă.

## Instalare și Rulare
1. Clonează repository-ul.
2. Asigură-te că ai Java instalat.
3. Rulează clasa `Main`.
4. Fișierele de date (`users.txt`, `accounts.txt`, `transactions.txt`) vor fi create/actualizate automat în folderul rădăcină.

## Securitate și Validări
* PIN-ul este validat la fiecare operațiune sensibilă.
* Soldul nu poate deveni negativ în urma retragerilor.
* Tranzacțiile sunt înregistrate cu timestamp precis pentru audit.