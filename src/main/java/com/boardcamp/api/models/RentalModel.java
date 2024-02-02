package com.boardcamp.api.models;

import java.time.LocalDate;

import com.boardcamp.api.dtos.RentalDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data              
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rentals")
public class RentalModel {
  RentalModel(RentalDTO dto, GameModel game, CustomerModel customer, LocalDate rentDate, Long originalPrice){
    this.customer = customer;
    this.game = game;
    this.daysRented = dto.getDaysRented();
    this.rentDate = rentDate;
    this.originalPrice = originalPrice;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customerId", referencedColumnName = "id", nullable = false)
  private CustomerModel customer;

  @ManyToOne
  @JoinColumn(name = "gameId", referencedColumnName = "id", nullable = false)
  private GameModel game;

  @Column(nullable = false)
  private LocalDate rentDate;

  @Column(nullable = false)
  private Long daysRented;

  @Column(nullable = true)
  private LocalDate returnDate;

  @Column(nullable = false)
  private Long originalPrice;

  @Column(columnDefinition = "bigint default 0")
  private Long delayFee;
}
