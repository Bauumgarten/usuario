package com.estudo_raul.java.business.dtos;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnderecoDTO {

    private Long id;
    private String rua;
    private String numero;
    private String complemento;
    private String cidade;
    private String cep;
    private String estado;

}
