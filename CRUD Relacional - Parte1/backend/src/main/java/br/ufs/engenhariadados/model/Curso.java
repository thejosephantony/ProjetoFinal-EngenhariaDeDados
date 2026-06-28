package br.ufs.engenhariadados.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "curso", schema = "universidade")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcurso")
    private Integer idCurso;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "grau", columnDefinition = "universidade.tipo_grau")
    @ColumnTransformer(write = "?::universidade.tipo_grau")
    private String grau;

    @Column(name = "turno", nullable = false, columnDefinition = "universidade.tipo_turno")
    @ColumnTransformer(write = "?::universidade.tipo_turno")
    private String turno;

    @Column(name = "campus", length = 100)
    private String campus;

    @Column(name = "nivel", columnDefinition = "universidade.tipo_nivel")
    @ColumnTransformer(write = "?::universidade.tipo_nivel")
    private String nivel;
}