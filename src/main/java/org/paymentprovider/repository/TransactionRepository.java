package org.paymentprovider.repository;

import org.paymentprovider.dto.TransactionSearchFilter;
import org.paymentprovider.entity.Transaction;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;

import static org.springframework.data.relational.core.query.Criteria.where;

public interface TransactionRepository extends R2dbcRepository<Transaction, Long> {
  default Query createTransactionFilter(TransactionSearchFilter filter) {
    var criteria = Criteria.from();

    if (filter == null) {
      return Query.query(criteria);
    }

    if (filter.getStartDate() != null) {
      criteria = criteria.and(where("createdAt").greaterThanOrEquals(filter.getStartDate()));
    }
    if (filter.getEndDate() != null) {
      criteria = criteria.and(where("createdAt").lessThanOrEquals(filter.getEndDate()));
    }

    return Query.query(criteria);
  }
}
