package br.com.reservasti.controller;

import br.com.reservasti.domain.equipamento.dto.EquipamentoDTO;
import br.com.reservasti.domain.equipamento.dto.EquipamentoReturnDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @PostMapping("/cadastrar")
    public EquipamentoReturnDTO cadastrarCategoria(@RequestBody @Valid EquipamentoDTO dto){

    }
}
