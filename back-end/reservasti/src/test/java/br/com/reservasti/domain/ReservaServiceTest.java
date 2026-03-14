package br.com.reservasti.domain;

import br.com.reservasti.domain.equipamento.Equipamento;
import br.com.reservasti.domain.equipamento.EquipamentoRepository;
import br.com.reservasti.domain.funcionario.Funcionario;
import br.com.reservasti.domain.funcionario.FuncionarioRepository;
import br.com.reservasti.domain.reserva.Reserva;
import br.com.reservasti.domain.reserva.ReservaRepository;
import br.com.reservasti.domain.reserva.ReservaService;
import br.com.reservasti.domain.reserva.StatusReserva;
import br.com.reservasti.domain.reserva.dto.ReservaDTO;
import br.com.reservasti.domain.reserva.dto.ReservaRetornoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import br.com.reservasti.domain.reserva.validacoes.IValidatorReservaAbertura;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Spy;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private EquipamentoRepository equipamentoRepository;

    @Mock
    private IValidatorReservaAbertura validadorMock;

    @Spy
    private List<IValidatorReservaAbertura> validadores = new ArrayList<>();

    @BeforeEach
    void setUp() {

        validadores.add(validadorMock);
    }


    @Test
    @DisplayName("Deve agendar uma reserva com sucesso quando não houver conflito de datas")
    void deveAgendarReservaComSucesso() {
        LocalDate dataRetirada = LocalDate.now().plusDays(1);
        LocalDate dataDevolucao = LocalDate.now().plusDays(5);
        ReservaDTO dto = new ReservaDTO(1L, 1L, dataRetirada, dataDevolucao);

        Funcionario funcionarioMock = new Funcionario();
        funcionarioMock.setId(1L);
        funcionarioMock.setNomeCompleto("João da TI");
        funcionarioMock.setAtivo(true);

        Equipamento equipamentoMock = new Equipamento();
        equipamentoMock.setId(1L);
        equipamentoMock.setNome("Notebook Dell");

        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionarioMock));
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamentoMock));

        doNothing().when(validadorMock).validar(any());


        ReservaRetornoDTO resultado = reservaService.solicitarReserva(dto);

        assertNotNull(resultado);
        assertEquals("João da TI", resultado.nomeFuncionario());
        assertEquals("Notebook Dell", resultado.nomeEquipamento());

        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar reservar equipamento que falha na validação de conflito")
    void deveLancarExcecaoQuandoHouverConflito() {
        LocalDate dataRetirada = LocalDate.now().plusDays(1);
        LocalDate dataDevolucao = LocalDate.now().plusDays(5);
        ReservaDTO dto = new ReservaDTO(1L, 1L, dataRetirada, dataDevolucao);

        Funcionario funcionarioMock = new Funcionario();
        funcionarioMock.setAtivo(true);

        Equipamento equipamentoMock = new Equipamento();
        equipamentoMock.setId(1L);

        doThrow(new IllegalStateException("Este equipamento já está reservado neste período."))
                .when(validadorMock).validar(any());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservaService.solicitarReserva(dto);
        });

        assertEquals("Este equipamento já está reservado neste período.", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se a data de devolução for menor que a data de retirada")
    void deveLancarExcecaoQuandoDataDevolucaoInvalida() {
        LocalDate dataRetirada = LocalDate.now().plusDays(2);
        LocalDate dataDevolucao = LocalDate.now().minusDays(1);
        ReservaDTO dto = new ReservaDTO(1L, 1L, dataRetirada, dataDevolucao);

        doThrow(new IllegalArgumentException("A data de devolução não pode ser anterior à retirada."))
                .when(validadorMock).validar(any());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.solicitarReserva(dto);
        });

        assertEquals("A data de devolução não pode ser anterior à retirada.", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve permitir a devolução do equipamento com sucesso")
    void deveAceitarADevolucao() {

        Reserva reservaMock = new Reserva();
        reservaMock.setId(1L);
        reservaMock.setStatus(StatusReserva.ATIVA);


        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaMock));


        reservaService.devolverEquipamento(1L);

        assertEquals(StatusReserva.CONCLUIDA, reservaMock.getStatus());
        assertNotNull(reservaMock.getDataDevolucaoReal());


        verify(reservaRepository, times(1)).save(reservaMock);
    }
}
