package com.rev.service;

import com.rev.modal.Order;
import com.rev.modal.Seller;
import com.rev.modal.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
