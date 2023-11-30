package com.smallworld;

import com.smallworld.model.Transaction;

import java.util.List;
import java.util.Map;

public class Main {
     public static void main(String[] args) {
         TransactionManager transactionManager = new TransactionManager();
         TransactionDataFetcher transactionDataFetcher = TransactionDataFetcher.getInstance(transactionManager);
         System.out.println("Sum of all transactions: "+ transactionDataFetcher.getTotalTransactionAmount());
         System.out.println("Sum of all transactions by specific client: "+ transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby"));
         System.out.println("Individual Maximum transactions by: "+ transactionDataFetcher.getMaxTransactionAmount());
         System.out.println("Top 3 Transactions by Amount in Descending: " );
         List<Transaction> top3Transactions = transactionDataFetcher.getTop3TransactionsByAmount();
         top3Transactions.forEach(transaction -> System.out.println(transaction.getAmount()));
         System.out.println("Uniques clients count: "+ transactionDataFetcher.countUniqueClients());
         System.out.println("Client has open compliance: "+ transactionDataFetcher.hasOpenComplianceIssues("Arthur Shelby"));
         System.out.println("Transactions Linked to Beneficiary: ");
         Map<String,Transaction> transactionsByBeneficiaryName= transactionDataFetcher.getTransactionsByBeneficiaryName();
         for(String beneficiaryName: transactionsByBeneficiaryName.keySet()){
             System.out.println(beneficiaryName +": \n" + transactionsByBeneficiaryName.get(beneficiaryName));
         }
         System.out.println("Unsolved Issues: "+ transactionDataFetcher.getUnsolvedIssueIds());
         System.out.println("List of issues resolved: ");
         List<String> solvedIssuesMessages = transactionDataFetcher.getAllSolvedIssueMessages();
         solvedIssuesMessages.forEach(System.out::println);
         System.out.println("Client which have sent highest amount: " +transactionDataFetcher.getTopSender());

     }
}
