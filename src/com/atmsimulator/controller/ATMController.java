package com.atmsimulator.controller;

import com.atmsimulator.model.Account;
import com.atmsimulator.model.Transaction;
import com.atmsimulator.model.User;
import com.atmsimulator.service.AccountService;
import com.atmsimulator.service.AdminService;
import com.atmsimulator.service.AuthService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ATMController {
    private final AuthService authService = new AuthService();
    private final AccountService accountService = new AccountService();
    private final AdminService adminService = new AdminService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\nATM SIMULATOR");
            System.out.print("Introduceti ID Cont (sau 'exit' pentru a iesi): ");
            String accountId = scanner.nextLine();

            if (accountId.equalsIgnoreCase("exit")) break;

            System.out.print("Introduceti PIN: ");
            String pin = scanner.nextLine();

            try {
                Account loggedInAccount = authService.login(accountId, pin);
                User owner = authService.getOwnerDetails(loggedInAccount.getUserId());

                System.out.println("\nAutentificare reusita! Bun venit, " + owner.getFullName() + "!");

                if (owner.getRole().equals("ADMIN")) {
                    showAdminMenu();
                } else {
                    showClientMenu(loggedInAccount);
                }

            } catch (RuntimeException e) {
                System.out.println("EROARE: " + e.getMessage());
            }
        }
    }

    private void showClientMenu(Account account) {
        boolean running = true;
        while (running) {
            System.out.println("\nMENIU CLIENT (" + account.getCurrency() + ")\n");
            System.out.println("1. Interogare Sold");
            System.out.println("2. Retragere Numerar");
            System.out.println("3. Depunere Numerar");
            System.out.println("4. Extras de Cont");
            System.out.println("5. Transfer bancar");
            System.out.println("6. Schimbare PIN");
            System.out.println("7. Iesire (Deconectare)");
            System.out.print("Alegeti o optiune: ");

            String option = scanner.nextLine();

            try {
                switch (option) {
                    case "1":
                        System.out.println("Soldul dvs. este: " + account.getBalance() + " " + account.getCurrency());
                        break;
                    case "2":
                        System.out.print("Suma de retras: ");
                        BigDecimal withdrawAmount = new BigDecimal(scanner.nextLine());
                        accountService.withdraw(account, withdrawAmount);
                        System.out.println("SUCCES: Ati retras " + withdrawAmount + " " + account.getCurrency());
                        break;
                    case "3":
                        System.out.print("Suma de depus: ");
                        BigDecimal depositAmount = new BigDecimal(scanner.nextLine());
                        accountService.deposit(account, depositAmount);
                        System.out.println("SUCCES: Ati depus " + depositAmount + " " + account.getCurrency());
                        break;
                    case "4":
                        System.out.println("\nEXTRAS DE CONT");
                        List<Transaction> istoric = adminService.getTransactionsForAccount(account.getAccountId());

                        if (istoric.isEmpty()) {
                            System.out.println("Nu exista tranzactii.");
                        } else {
                            boolean hasTransactions = false;
                            for (Transaction t : istoric) {
                                String type = t.getType().trim();

                                if (type.equals("RETRAGERE") || type.equals("DEPUNERE") ||
                                        type.equals("TRANSFER") || type.equals("PRIMIRE TRANSFER")) {

                                    System.out.println(String.format("%-30s", t.getTimestamp()) + " | " +
                                            String.format("%-16s", type) + " | " +
                                            String.format("%-7s", t.getAmount()) + " " + t.getCurrency());
                                    hasTransactions = true;
                                }
                            }

                            if (!hasTransactions) {
                                System.out.println("Nu exista tranzactii financiare de afisat.");
                            }
                        }
                        break;
                    case "5":
                        System.out.print("Introduceti ID-ul contului destinatie : ");
                        String toAccountId = scanner.nextLine();

                        System.out.print("Suma de transferat: ");
                        BigDecimal transferAmount = new BigDecimal(scanner.nextLine());
                        accountService.transfer(account, toAccountId, transferAmount);

                        System.out.println("SUCCES: Ati transferat " + transferAmount + " " + account.getCurrency() + " catre " + toAccountId);
                        break;
                    case "6":
                        System.out.println("\nSCHIMBARE PIN");
                        System.out.print("Introduceți PIN-ul actual: ");
                        String currentPin = scanner.nextLine();

                        System.out.print("Introduceți NOUL PIN (4 cifre): ");
                        String pin1 = scanner.nextLine();

                        System.out.print("Confirmați NOUL PIN: ");
                        String pin2 = scanner.nextLine();

                        try {
                            if (!pin1.equals(pin2)) {
                                throw new RuntimeException("Cele două PIN-uri noi nu coincid!");
                            }

                            authService.changePin(account, currentPin, pin1);

                            System.out.println("SUCCES: PIN-ul a fost schimbat cu succes!");
                        } catch (Exception e) {
                            System.out.println("EROARE: " + e.getMessage());
                        }
                        break;
                    case "7":
                        running = false;
                        System.out.println("La revedere!");
                        break;
                    default:
                        System.out.println("Optiune invalida!");
                }
            } catch (Exception e) {
                System.out.println("EROARE OPERATIUNE: " + e.getMessage());
            }
        }
    }

    private void showAdminMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- MENIU ADMINISTRATOR ---");
            System.out.println("1. Vezi TOATE tranzactiile din sistem");
            System.out.println("2. Cauta tranzactii dupa ID Cont");
            System.out.println("3. Creaza cont nou");
            System.out.println("4. Sterge cont existent");
            System.out.println("5. Iesire (Deconectare)");
            System.out.print("Alegeti o optiune: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    List<Transaction> all = adminService.getAllSystemTransactions();
                    System.out.println("TRANZCTII - " + all.size() + " GASITE");


                    DateTimeFormatter formatData = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

                    for (Transaction t : all) {
                        String date = t.getTimestamp().format(formatData);
                        String type = t.getType().trim();
                        String accountId = String.format("%-10s", t.getAccountId());

                        if (type.equals("SCHIMBARE PIN") || type.equals("INCHIDERE CONT")) {
                            System.out.println(date + " | " + accountId + " | " +
                                    String.format("%-18s", type) + " | [OPERATIUNE SECURITATE]");

                        } else if (type.equals("CREARE CONT")) {
                            System.out.println(date + " | " + accountId + " | " +
                                    String.format("%-18s", type) + " | Sold Initial: " +
                                    t.getAmount() + " " + t.getCurrency());

                        } else {
                            System.out.println(date + " | " + accountId + " | " +
                                    String.format("%-18s", type) + " | " +
                                    t.getAmount() + " " + t.getCurrency());
                        }
                    }
                    break;
                case "2":
                    System.out.print("Introduceti ID Cont client: ");
                    String searchId = scanner.nextLine();
                    List<Transaction> clients = adminService.getTransactionsForAccount(searchId);
                    for (Transaction t : clients) {
                        System.out.println(t.getType() + " | " + t.getAmount() + " | " + t.getTimestamp());
                    }
                    break;
                case "3":
                    System.out.println("\nDESCHIDERE CONT NOU");
                    System.out.print("Nume complet detinator: ");
                    String fullName = scanner.nextLine();

                    System.out.print("Setati PIN initial (4 cifre): ");
                    String newPin = scanner.nextLine();

                    System.out.print("Suma depusa initial: ");
                    BigDecimal initialSum = new BigDecimal(scanner.nextLine());

                    System.out.print("Moneda contului (RON/EUR/USD/GBP): ");
                    String currency = scanner.nextLine();

                    try {
                        adminService.createFullProfile(fullName, newPin, initialSum, currency);
                        System.out.println("SUCCES: Profilul si contul au fost generate si salvate.");
                    } catch (Exception e) {
                        System.out.println("EROARE: " + e.getMessage());
                    }
                    break;
                case "4":
                    System.out.print("Introduceți ID-ul contului pentru închidere: ");
                    String idToClose = scanner.nextLine();

                    try {
                        adminService.closeAccount(idToClose);
                        System.out.println("SUCCES: Contul a fost închis definitiv.");
                    } catch (Exception e) {
                        System.out.println("NOTIFICARE: " + e.getMessage());
                    }
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }
}