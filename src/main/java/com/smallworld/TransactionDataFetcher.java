package com.smallworld;

import com.smallworld.model.Transaction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TransactionDataFetcher {
    TransactionManager transactionManager;
    public static TransactionDataFetcher instance;
    private TransactionDataFetcher(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public static TransactionDataFetcher getInstance(TransactionManager transactionManager){
        if (instance == null) {
            instance = new TransactionDataFetcher(transactionManager);
        }
        return instance;
    }

    /**
     * Returns the sum of the amounts of all transactionManager
     */
    public double getTotalTransactionAmount() {

        return transactionManager.getTransactions().stream().collect(
                Collectors.toMap(Transaction::getMtn,transaction -> transaction, // keeping the first found object
                        (existing,replacement) -> existing))
                .values()
                .stream().mapToDouble(Transaction::getAmount).sum();
    }

    /**
     * Returns the sum of the amounts of all transactionManager sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        List<Transaction> clientTransactions = transactionManager.getTransactions().stream().filter(t -> t.getSenderFullName()
                .equals(senderFullName))
                .toList();
        return clientTransactions.stream().collect(
                        Collectors.toMap(Transaction::getMtn,transaction -> transaction, // keeping the first found object
                                (existing, replacement) -> existing))
                .values()
                .stream().mapToDouble(Transaction::getAmount).sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        List<Transaction> maxTransactionAmount = transactionManager.getTransactions().stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .toList();
        return maxTransactionAmount.get(0).getAmount();
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        return  transactionManager.getTransactions().stream().flatMap(transaction ->
                Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()))
                .distinct()
                .count();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return  transactionManager.getTransactions().stream().filter(t -> t.getSenderFullName()
                        .equals(clientFullName) || t.getBeneficiaryFullName()
                        .equals(clientFullName))
                .toList().stream().anyMatch(transaction -> !transaction.isIssueSolved());
    }

    /**
     * Returns all transactionManager indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        List<Transaction> uniqueTransactions = transactionManager.getTransactions().stream().collect(
                        Collectors.toMap(Transaction::getMtn,transaction -> transaction, // keeping the first found object
                                (existing,replacement) -> existing))
                .values()
                .stream()
                .toList();
        Map<String, Transaction> transactionsByBenefieciary = new HashMap<>();
        for (Transaction transaction: uniqueTransactions) {
            transactionsByBenefieciary.put(transaction.getBeneficiaryFullName(), transaction);
        }
        return transactionsByBenefieciary;
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        return transactionManager.getTransactions().stream().filter(t -> !t.isIssueSolved())
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return transactionManager.getTransactions().stream()
                .filter(t -> t.isIssueSolved() && t.getIssueMessage() != null)
                .map(Transaction::getIssueMessage)
                .toList();
    }

    /**
     * Returns the 3 transactionManager with highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {

        return transactionManager.getTransactions().stream().collect(
                        Collectors.toMap(Transaction::getMtn,transaction -> transaction, // keeping the first found object
                                (existing, replacement) -> existing))
                .values().stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .toList();
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        List<Transaction> uniqueTransactions = transactionManager.getTransactions().stream().collect(
                        Collectors.toMap(Transaction::getMtn,transaction -> transaction, // keeping the first found object
                                (existing,replacement) -> existing))
                .values()
                .stream()
                .toList();
        Map<String, Double> transactionsByClient = new HashMap<>();
        for (Transaction transaction: uniqueTransactions) {
            transactionsByClient.put(
                    transaction.getSenderFullName(),
                    transactionsByClient.getOrDefault(
                            transaction.getSenderFullName(),0.0)+transaction.getAmount());
        }
        return transactionsByClient.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

}
