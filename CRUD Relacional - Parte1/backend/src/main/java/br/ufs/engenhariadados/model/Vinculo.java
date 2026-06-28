package br.ufs.engenhariadados.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vinculo", schema = "universidade")
public class Vinculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idvinculo")
    private Integer idVinculo;

    @ManyToOne
    @JoinColumn(name = "mat_estudante", referencedColumnName = "mat_estudante")
    private Estudante estudante;

    @ManyToOne
    @JoinColumn(name = "curso", referencedColumnName = "idcurso")
    private Curso curso;

    @Column(name = "data_entrada")
    private LocalDate dataEntrada;

    @Column(name = "status", columnDefinition = "universidade.status_estudante")
    @ColumnTransformer(write = "?::universidade.status_estudante")
    private String status;

    @Column(name = "data_saida")
    private LocalDate dataSaida;
}