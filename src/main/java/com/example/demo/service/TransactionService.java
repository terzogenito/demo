package com.example.demo.service;

import com.example.demo.entity.MembershipEntity;
import com.example.demo.entity.ServiceEntity;
import com.example.demo.entity.TransactionEntity;
import com.example.demo.model.TransactionRequest;
import com.example.demo.repository.MembershipRepository;
import com.example.demo.repository.ServiceRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.response.TransactionHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private MembershipService membershipService;

    private final TransactionRepository transactionRepository;
    private final MembershipRepository membershipRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public TransactionService(
        MembershipRepository membershipRepository,
        ServiceRepository serviceRepository,
        TransactionRepository transactionRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.membershipRepository = membershipRepository;
        this.serviceRepository = serviceRepository;
    }

    public ApiResponse topUpBalance(String userEmail, Double topUpAmount) {
        MembershipEntity membership = membershipRepository.findByEmail(userEmail);
        if (membership == null) {
            throw new UsernameNotFoundException("User not found");
        }
        membership.setBalance(membership.getBalance() + topUpAmount);
        membershipRepository.save(membership);
        Map<String, Double> data = new HashMap<>();
        data.put("balance", membership.getBalance());
        return new ApiResponse(0, "Top Up Balance berhasil", data);
    }

    public ApiResponse makeTransaction(String userEmail, TransactionRequest transactionRequest) {
        MembershipEntity membership = membershipRepository.findByEmail(userEmail);
        if (membership == null) {
            throw new UsernameNotFoundException("User not found");
        }
        ServiceEntity service = serviceRepository.findByServiceCode(transactionRequest.getServiceCode());
        if (service == null) {
            throw new IllegalArgumentException("Service not found");
        }
        String invoiceNumber = generateInvoiceNumber();
        String serviceCode = service.getServiceCode();
        String serviceName = service.getServiceName();
        String transactionType = service.getTransactionType();
        double totalAmount = service.getServiceTariff();
        String createdOn = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        Map<String, Object> data = new HashMap<>();
        data.put("invoice_number", invoiceNumber);
        data.put("service_code", serviceCode);
        data.put("service_name", serviceName);
        data.put("transaction_type", transactionType);
        data.put("total_amount", totalAmount);
        data.put("created_on", createdOn);

        return new ApiResponse(0, "Transaksi berhasil", data);
    }

    private String generateInvoiceNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uniquePart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "INV" + datePart + "-" + uniquePart;
    }

    public List<TransactionHistoryResponse> getTransactionHistory(String userEmail, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        List<TransactionEntity> transactions = transactionRepository.findByUserId(membershipService.getUserIdByEmail(userEmail), pageable);
        return transactions.stream().map(this::convertToHistoryResponse).collect(Collectors.toList());
    }

    public TransactionHistoryResponse convertToHistoryResponse(TransactionEntity transaction) {
        ServiceEntity service = serviceRepository.findByServiceCode(transaction.getServiceCode());
        if (service == null) {
            throw new IllegalArgumentException("Service not found");
        }
        return new TransactionHistoryResponse(
            transaction.getInvoiceNumber(),
            service.getTransactionType(),
            service.getDescription(),
            transaction.getTotalAmount(),
            transaction.getCreatedOn()
        );
    }
}
