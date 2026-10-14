package com.spring_class.dscommerce.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_payment")
@Getter
@Setter
public class Payment extends BaseEntity {

    @NotNull(message = "Payment moment is required")
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
    private Instant moment;

    @NotNull(message = "Order is required")
    @OneToOne
    @MapsId
    @JoinColumn(name = "id", nullable = false)
    private Order order;

    public Payment() {
    }

    public Payment(Order order) {
        this.moment = Instant.now();
        this.order = order;
    }
}
