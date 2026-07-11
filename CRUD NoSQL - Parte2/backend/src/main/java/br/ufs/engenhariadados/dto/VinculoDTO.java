package br.ufs.engenhariadados.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinculoDTO {
    private LocalDate dataEntrada;
    private String status;
    private LocalDate dataSaida;
}