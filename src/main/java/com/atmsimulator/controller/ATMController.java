package com.atmsimulator.controller;

import com.atmsimulator.model.Account;
import com.atmsimulator.model.Transaction;
import com.atmsimulator.model.User;
import com.atmsimulator.service.AccountService;
import com.atmsimulator.service.AdminService;
import com.atmsimulator.service.AuthService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ATMController {
    private final AuthService authService = new AuthService();
    private final AccountService accountService = new AccountService();
    private final AdminService adminService = new AdminService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n=== BINE ATI VENIT LA ATM SIMULATOR ===");
            System.out.print("Introduceti ID Cont (sau 'exit' pentru a opri): ");
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
            System.out.println("\n--- MENIU CLIENT (" + account.getCurrency() + ") ---");
            System.out.println("1. Interogare Sold");
            System.out.println("2. Retragere Numerar");
            System.out.println("3. Depunere Numerar");
            System.out.println("4. Extras de Cont");
            System.out.println("5. Iesire (Deconectare)");
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
                        System.out.println("--- ULTIMELE TRANZACTII ---");
                        List<Transaction> istoric = adminService.getTransactionsForAccount(account.getAccountId());
                        if (istoric.isEmpty()) {
                            System.out.println("Nu exista tranzactii.");
                        } else {
                            for (Transaction t : istoric) {
                                System.out.println(t.getTimestamp() + " | " + t.getType() + " | " + t.getAmount() + " " + t.getCurrency());
                            }
                        }
                        break;
                    case "5":
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
            System.out.println("3. Iesire (Deconectare)");
            System.out.print("Alegeti o optiune: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    List<Transaction> all = adminService.getAllSystemTransactions();
                    System.out.println("S-au gasit " + all.size() + " tranzactii totale.");
                    for (Transaction t : all) {
                        System.out.println(t.getAccountId() + " | " + t.getType() + " | " + t.getAmount());
                    }
                    break;
                case "2":
                    System.out.print("Introduceti ID Cont client: ");
                    String searchId = scanner.nextLine();
                    List<Transaction> clientTx = adminService.getTransactionsForAccount(searchId);
                    for (Transaction t : clientTx) {
                        System.out.println(t.getType() + " | " + t.getAmount() + " | " + t.getTimestamp());
                    }
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }
}