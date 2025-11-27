package com.estudo_raul.java.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "telefone")
@Builder
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", length = 10, unique = true)
    private String numero;
    @Column(name = "ddd", length = 3)
    private String ddd;
    @Column(name = "tipo", length = 10)
    private String tipo; // Exemplo: "celular", "fixo".
    @Column(name = "usuario_id")
    private Long usuario_id;
}
