package com.api.agenda.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contato implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "O campo nome é obrigatório!")
    private String nome;
    @NotEmpty(message = "O campo email é obrigatório!")
    @Email(message = "O campo e-mail esta incorreto.")

    private String email;

    @Lob
    private byte[] foto;

    private Boolean favorito;

}
