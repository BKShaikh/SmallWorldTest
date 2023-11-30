package com.smallworld;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.model.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class TransactionManager {

    private List<Transaction> transactions;

    public TransactionManager () {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File transactionJson = new File("transactions.json");
            if (!transactionJson.exists()) {
                throw new FileNotFoundException("file not found");
            }
            transactions = objectMapper.readValue(transactionJson, new TypeReference<List<Transaction>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

}
