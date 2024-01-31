package com.boardcamp.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data              
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "games")
public class GamesModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(length = 150, nullable = false)
  private String name;

  @Column(nullable = false)
  private String image;

  @Column(nullable = false)
  private Long stockTotal;

  @Column(nullable = false)
  private Long pricePerDay;
}
