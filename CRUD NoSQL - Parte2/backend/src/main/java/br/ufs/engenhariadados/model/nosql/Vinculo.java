package br.ufs.engenhariadados.model.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vinculo {

    private LocalDate dataEntrada;
    private String status;      // "Ativo", "Cancelado", "Graduado", "Formando"
    private LocalDate dataSaida; // pode ser null
}