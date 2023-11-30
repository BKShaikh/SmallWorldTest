package com.smallworld;

import com.smallworld.model.Transaction;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDataFetcherTest {
    TransactionManager transactionManager = new TransactionManager();
    TransactionDataFetcher transactionDataFetcher = TransactionDataFetcher.getInstance(transactionManager);

    @Test
    void getTotalTransactionAmount() {
        assertEquals(2889.17, transactionDataFetcher.getTotalTransactionAmount());
        assertTrue(transactionDataFetcher.getTotalTransactionAmount() > 0.0);
    }

    @Test
    void getTotalTransactionAmountSentBy() {
        assertEquals(678.06, transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby"));
        assertNotEquals(129.00, transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby"));
    }

    @Test
    void getTop3TransactionsByAmount() {
        List<Transaction> top3Transactions = transactionDataFetcher.getTop3TransactionsByAmount();
        assertEquals(3,top3Transactions.size());
        Double[] actual = top3Transactions.stream().map(Transaction::getAmount).toArray(size-> new Double[top3Transactions.size()]);
        Double[] expected = {985.0, 666.0, 430.2} ;
        assertArrayEquals(expected, actual);
    }

    @Test
    void countUniqueClients() {
        assertEquals(14, transactionDataFetcher.countUniqueClients());
        assertFalse(transactionDataFetcher.countUniqueClients() < 0);
    }

    @Test
    void hasOpenComplianceIssues() {
        assertTrue(transactionDataFetcher.hasOpenComplianceIssues("Arthur Shelby"));
        assertFalse(transactionDataFetcher.hasOpenComplianceIssues("Aunt Polly"));
    }

    @Test
    void getTransactionsByBeneficiaryName() {
        assertNotNull(transactionDataFetcher.getTransactionsByBeneficiaryName());
        assertEquals(10,transactionDataFetcher.getTransactionsByBeneficiaryName().size());
    }

    @Test
    void getUnsolvedIssueIds() {
        assertEquals(5,transactionDataFetcher.getUnsolvedIssueIds().size());
    }

    @Test
    void getAllSolvedIssueMessages() {
        assertTrue(transactionDataFetcher.getAllSolvedIssueMessages().contains("Never gonna give you up"));
    }

    @Test
    void getTopSender() {
        assertEquals("Arthur Shelby",transactionDataFetcher.getTopSender().orElse(null));
    }

}
