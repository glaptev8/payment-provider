package org.paymentprovider.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.paymentprovider.dto.BalanceDto;
import org.paymentprovider.dto.MerchantDto;
import org.paymentprovider.dto.PayOutDto;
import org.paymentprovider.dto.TransactionDto;
import org.paymentprovider.dto.WebHookPayOutRequest;
import org.paymentprovider.dto.WebHookTransactionRequest;
import org.paymentprovider.entity.Balance;
import org.paymentprovider.entity.Merchant;
import org.paymentprovider.entity.PayOut;
import org.paymentprovider.entity.Transaction;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
  MerchantDto merchantToDto(Merchant merchant);
  Merchant merchantToEntity(MerchantDto merchant);

  BalanceDto balanceToDto(Balance balance);
  Balance balanceToEntity(BalanceDto balance);

  @Mappings({
    @Mapping(target = "customer", expression = "java(mapToCustomerPayOut(payOut))"),
    @Mapping(target = "cardData", expression = "java(mapToCardDataPayOut(payOut))"),
  })
  PayOutDto payOutToDto(PayOut payOut);

  @Mappings({
    @Mapping(target = "firstName", source = "customer.firstName"),
    @Mapping(target = "lastName", source = "customer.lastName"),
    @Mapping(target = "country", source = "customer.country"),
    @Mapping(target = "cardNumber", source = "cardData.cardNumber"),
  })
  PayOut payOutToEntity(PayOutDto payOut);
  PayOutDto.Customer mapToCustomerPayOut(PayOut transaction);
  PayOutDto.CardData mapToCardDataPayOut(PayOut transaction);

  @Mappings({
    @Mapping(target = "customer", expression = "java(mapToCustomerTransaction(transaction))"),
    @Mapping(target = "cardData", expression = "java(mapToCardDataTransaction(transaction))"),
  })
  TransactionDto transactionToDto(Transaction transaction);

  @Mappings({
    @Mapping(target = "firstName", source = "customer.firstName"),
    @Mapping(target = "lastName", source = "customer.lastName"),
    @Mapping(target = "country", source = "customer.country"),
    @Mapping(target = "cardNumber", source = "cardData.cardNumber"),
    @Mapping(target = "cvv", source = "cardData.cvv"),
    @Mapping(target = "expDate", source = "cardData.expDate"),
  })
  Transaction transactionToEntity(TransactionDto transaction);

  TransactionDto.Customer mapToCustomerTransaction(Transaction transaction);
  TransactionDto.CardData mapToCardDataTransaction(Transaction transaction);

  @Mappings({
    @Mapping(source = "id", target = "providerTransactionId"),
    @Mapping(target = "customer", expression = "java(mapToCustomerWebHookTransaction(transaction))"),
    @Mapping(target = "cardData", expression = "java(mapToCardDataWebHookTransaction(transaction))"),
  })
  WebHookTransactionRequest transactionToRequest(Transaction transaction);
  WebHookTransactionRequest.Customer mapToCustomerWebHookTransaction(Transaction transaction);
  WebHookTransactionRequest.CardData mapToCardDataWebHookTransaction(Transaction transaction);

  @Mappings({
    @Mapping(source = "id", target = "externalTransactionId"),
    @Mapping(target = "customer", expression = "java(mapToCustomerWebHookPayOut(transaction))"),
    @Mapping(target = "cardData", expression = "java(mapToCardDataWebHookPayOut(transaction))"),
  })
  WebHookPayOutRequest payOutToRequest(PayOut transaction);
  WebHookPayOutRequest.Customer mapToCustomerWebHookPayOut(PayOut transaction);
  WebHookPayOutRequest.CardData mapToCardDataWebHookPayOut(PayOut transaction);
}
