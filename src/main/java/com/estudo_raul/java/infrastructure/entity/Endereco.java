package com.estudo_raul.java.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rua")
    private String rua;

    @Column(name = "numero")
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "cep", length = 9)
    private String cep;

    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "usuario_id")
    private Long usuario_id;

}
