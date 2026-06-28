package br.ufs.engenhariadados.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estudante", schema = "universidade")
public class Estudante {

    @Id
    @Column(name = "mat_estudante", nullable = false, length = 7)
    private String matricula;

    @OneToOne
    @JoinColumn(name = "cpf", referencedColumnName = "cpf")
    private Usuario usuario;

    @Column(name = "mc", precision = 2, scale = 0)
    private BigDecimal mc;

    @Column(name = "ano_ingresso")
    private Integer anoIngresso;
}