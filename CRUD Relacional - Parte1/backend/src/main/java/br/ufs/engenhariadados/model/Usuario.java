package br.ufs.engenhariadados.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario", schema = "universidade")
public class Usuario {

    @Id
    @Column(name = "cpf", nullable = false, precision = 13, scale = 0)
    private BigDecimal cpf;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "email", columnDefinition = "varchar[]")
    private String[] email;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "telefone", columnDefinition = "varchar[]")
    private String[] telefone;

    @Column(name = "login", length = 45, unique = true)
    private String login;

    @Column(name = "senha", length = 32)
    private String senha;
}