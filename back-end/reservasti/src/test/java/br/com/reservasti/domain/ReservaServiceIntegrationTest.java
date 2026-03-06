package br.com.reservasti.domain; // Ajuste o pacote se necessário

import br.com.reservasti.domain.categoria.Categoria;
import br.com.reservasti.domain.categoria.CategoriaRepository;
import br.com.reservasti.domain.equipamento.Equipamento;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import br.com.reservasti.domain.equipamento.StatusEquipamento;
import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.domain.reserva.Reserva;
import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.domain.reserva.ReservaService;
import br.com.reservasti.domain.reserva.StatusReserva;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.domain.reserva.dto.ReservaRetornoDTO;
import br.com.reservasti.domain.usuario.Usuario;
import br.com.reservasti.domain.usuario.UsuarioRepository;
import br.com.reservasti.infra.ValidationExeception;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ReservaServiceIntegrationTest {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Teste de Integração: Deve agendar reserva testando do Service até o Banco Real")
    void deveAgendarReservaNoBancoReal() {

        Usuario usuarioReal = new Usuario();
        usuarioReal.setLogin("ana.contabilidade");
        usuarioReal.setSenha("123456");
        usuarioReal = usuarioRepository.save(usuarioReal);

        Funcionario funcionarioReal = new Funcionario();
        funcionarioReal.setNomeCompleto("Ana da Contabilidade");
        funcionarioReal.setAtivo(true);
        funcionarioReal.setId(usuarioReal.getId());
        funcionarioReal = funcionarioRepository.save(funcionarioReal);

        Categoria categoriaReal = new Categoria();
        categoriaReal.setNome("Notebooks");
        categoriaReal = categoriaRepository.save(categoriaReal);


        Equipamento equipamentoReal = new Equipamento();
        equipamentoReal.setNome("Notebook Dell");
        equipamentoReal.setMarca("Dell");
        equipamentoReal.setModelo("Inspiron");
        equipamentoReal.setNumeroPatrimonio("PAT-10001");

        equipamentoReal.setStatus(StatusEquipamento.DISPONIVEL);
        equipamentoReal.setCategoria(categoriaReal);
        equipamentoReal = equipamentoRepository.save(equipamentoReal);

        LocalDate dataRetirada = LocalDate.now().plusDays(1);
        LocalDate dataDevolucao = LocalDate.now().plusDays(5);
        ReservaDTO dto = new ReservaDTO(funcionarioReal.getId(), equipamentoReal.getId(), dataRetirada, dataDevolucao);


        ReservaRetornoDTO resultado = reservaService.solicitarReserva(dto);

        assertNotNull(resultado);
        assertEquals("Ana da Contabilidade", resultado.nomeFuncionario());

        assertEquals("Notebook Dell", resultado.nomeEquipamento());

        List<Reserva> reservasSalvas = reservaRepository.findAll();
        assertEquals(1, reservasSalvas.size());
        assertEquals(StatusReserva.AGENDADA, reservasSalvas.get(0).getStatus());
    }

    @Test
    @DisplayName("Teste de Integração: A própria lógica do sistema deve barrar conflito de datas")
    void logicaRealDeveLancarExcecaoQuandoHouverConflito() {


        Usuario usuarioReal = new Usuario();
        usuarioReal.setLogin("joao.ti");
        usuarioReal.setSenha("123456");
        usuarioReal = usuarioRepository.save(usuarioReal);

        Funcionario funcionarioReal = new Funcionario();
        funcionarioReal.setNomeCompleto("Joao da TI");
        funcionarioReal.setId(usuarioReal.getId());
        funcionarioReal.setAtivo(true);
        funcionarioReal = funcionarioRepository.save(funcionarioReal);

        Categoria categoriaReal = new Categoria();
        categoriaReal.setNome("Monitores");
        categoriaReal = categoriaRepository.save(categoriaReal);

        Equipamento equipamentoReal = new Equipamento();
        equipamentoReal.setNome("Monitor Ultrawide LG");
        equipamentoReal.setMarca("LG");
        equipamentoReal.setModelo("34WP550");
        equipamentoReal.setNumeroPatrimonio("PAT-20002");

        equipamentoReal.setStatus(StatusEquipamento.DISPONIVEL);
        equipamentoReal.setCategoria(categoriaReal);
        equipamentoReal = equipamentoRepository.save(equipamentoReal);



        LocalDate dataRetirada = LocalDate.now().plusDays(1);
        LocalDate dataDevolucao = LocalDate.now().plusDays(5);


        ReservaDTO dto1 = new ReservaDTO(funcionarioReal.getId(), equipamentoReal.getId(), dataRetirada, dataDevolucao);
        ReservaDTO dto2 = new ReservaDTO(funcionarioReal.getId(), equipamentoReal.getId(), dataRetirada, dataDevolucao);


        reservaService.solicitarReserva(dto1);


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservaService.solicitarReserva(dto2);
        });

        assertEquals("Este equipamento já está reservado neste período.", exception.getMessage());
        assertEquals(1, reservaRepository.count());
    }

    @Test
    @DisplayName("Teste de Integração: A própria lógica do sistema deve barrar quando o funcionário atingir o limite de 2 reservas")
    void logicaRealDeveLancarExcecaoAoExcederLimiteDeReservas() {


        Usuario usuarioReal = new Usuario();
        usuarioReal.setLogin("maria.ti");
        usuarioReal.setSenha("123456");
        usuarioReal = usuarioRepository.save(usuarioReal);

        Funcionario funcionarioReal = new Funcionario();
        funcionarioReal.setNomeCompleto("Maria da TI");
        funcionarioReal.setId(usuarioReal.getId());
        funcionarioReal.setAtivo(true);
        funcionarioReal = funcionarioRepository.save(funcionarioReal);

        Categoria categoriaReal = new Categoria();
        categoriaReal.setNome("Notebooks");
        categoriaReal = categoriaRepository.save(categoriaReal);


        Equipamento equipamento1 = new Equipamento();
        equipamento1.setNome("Notebook Dell 1");
        equipamento1.setMarca("Dell");
        equipamento1.setModelo("Inspiron 15");
        equipamento1.setNumeroPatrimonio("NTBK-0001");
        equipamento1.setStatus(StatusEquipamento.DISPONIVEL);
        equipamento1.setCategoria(categoriaReal);
        equipamento1 = equipamentoRepository.save(equipamento1);

        Equipamento equipamento2 = new Equipamento();
        equipamento2.setNome("Notebook Dell 2");
        equipamento2.setMarca("Dell");
        equipamento2.setModelo("Inspiron 15");
        equipamento2.setNumeroPatrimonio("NTBK-0002");
        equipamento2.setStatus(StatusEquipamento.DISPONIVEL);
        equipamento2.setCategoria(categoriaReal);
        equipamento2 = equipamentoRepository.save(equipamento2);

        Equipamento equipamento3 = new Equipamento();
        equipamento3.setNome("Monitor Ultrawide");
        equipamento3.setMarca("LG");
        equipamento3.setModelo("34WP550");
        equipamento3.setNumeroPatrimonio("MNTR-0001");
        equipamento3.setStatus(StatusEquipamento.DISPONIVEL);
        equipamento3.setCategoria(categoriaReal);
        equipamento3 = equipamentoRepository.save(equipamento3);

        LocalDate dataRetirada = LocalDate.now().plusDays(1);
        LocalDate dataDevolucao = LocalDate.now().plusDays(5);

        ReservaDTO dto1 = new ReservaDTO(funcionarioReal.getId(), equipamento1.getId(), dataRetirada, dataDevolucao);
        ReservaDTO dto2 = new ReservaDTO(funcionarioReal.getId(), equipamento2.getId(), dataRetirada, dataDevolucao);
        ReservaDTO dto3 = new ReservaDTO(funcionarioReal.getId(), equipamento3.getId(), dataRetirada, dataDevolucao);

        reservaService.solicitarReserva(dto1);
        reservaService.solicitarReserva(dto2);

        Exception exception = assertThrows(ValidationExeception.class, () -> {
            reservaService.solicitarReserva(dto3);
        });

        assertEquals("Limite atingido! O funcionário já possui 2 equipamentos reservados ou em uso.", exception.getMessage());

        assertEquals(2, reservaRepository.count());
    }
}