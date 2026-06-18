package business;

import dao.EventoDAO;
import dao.InscricaoDAO;
import model.Evento;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Classe centraliza as 3 regras de negócio do sistema.
 * Cada regra é um método estatico:
 *    Se a regra for respeitada ela não faz nada (execução continua)
 *    Se a regra for violada ela lança NegocioException (execução para)
 * As telas chamam esses métodos antes de salvar no banco.
 */
public class RegrasNegocio {

    /**
     * Regra 1 — Verificação de lotação
     * Antes de inscrever alguém, conta quantos inscritos o evento já tem.
     * Se >= capacidade máxima, bloqueia.
     * Fluxo: Tela chama este método consulta InscricaoDAO + EventoDAO
     *         se lotado, lança exceção mostrando mensagem de erro na tela
     */
    public static void verificarLotacao(int idEvento) throws SQLException, NegocioException {
        EventoDAO eventoDAO = new EventoDAO();
        InscricaoDAO inscricaoDAO = new InscricaoDAO();

        Evento evento = eventoDAO.buscarPorId(idEvento);
        if (evento == null) {
            throw new NegocioException("Evento nao encontrado.");
        }

        int totalInscritos = inscricaoDAO.contarInscricoes(idEvento);
        if (totalInscritos >= evento.getCapacidade()) {
            throw new NegocioException("O evento \"" + evento.getTitulo()
                    + "\" ja esta lotado (" + evento.getCapacidade() + " vagas).");
        }
    }

    /**
     * REGRA 2 — Conflito de horário de local
     * Ao criar/editar um evento, verifica se já existe outro evento
     * no mesmo local na mesma data e hora.
     *  idEventoExcluir ID do evento sendo editado (para ignorar ele mesmo).
     *  Passar 0 quando for um evento novo.
     */
    public static void verificarConflitoHorario(String local, LocalDateTime dataHora,
                                                int idEventoExcluir) throws SQLException, NegocioException {
        EventoDAO eventoDAO = new EventoDAO();
        boolean conflito = eventoDAO.existeConflitoHorario(local, dataHora, idEventoExcluir);
        if (conflito) {
            throw new NegocioException("Ja existe um evento agendado no local \""
                    + local + "\" para esta data e horario.");
        }
    }

    /**
     * REGRA 3 — Inscrição duplicada
     * Verifica se o usuário já está inscrito no evento.
     * Usa a restrição UNIQUE(id_usuario, id_evento) do banco como segunda proteção.
     */
    public static void verificarInscricaoDuplicada(int idUsuario,
                                                   int idEvento) throws SQLException, NegocioException {
        InscricaoDAO inscricaoDAO = new InscricaoDAO();
        boolean duplicada = inscricaoDAO.existeInscricao(idUsuario, idEvento);
        if (duplicada) {
            throw new NegocioException("Este usuario ja esta inscrito neste evento.");
        }
    }
}