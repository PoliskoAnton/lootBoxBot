package com.example.lootBot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lootbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // название лутбокса, например "Waifu Box"
    private Integer price;      // стоимость в монетах
    private Double dropChance;  // шанс выпадения награды (0.0 - 1.0)

    private String description; // описание лутбокса
}
