package com.tk.gg.payment.infrastructure.repository;

import com.tk.gg.payment.domain.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefundRepository  extends JpaRepository<Refund, UUID> {

}
