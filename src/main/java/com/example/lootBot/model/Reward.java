package com.example.lootBot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;     // например "IMAGE", "ITEM", "TEXT"
    private String content;  // ссылка на картинку или название предмета

    @ManyToOne
    @JoinColumn(name = "lootbox_id")
    private Lootbox lootbox; // к какому лутбоксу относится
}
