package business;

import dao.EventoDAO;
import dao.InscricaoDAO;
import model.Evento;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class RegrasNegocio {

    
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

    
    public static void verificarConflitoHorario(String local, LocalDateTime dataHora,
                                                int idEventoExcluir) throws SQLException, NegocioException {
        EventoDAO eventoDAO = new EventoDAO();
        boolean conflito = eventoDAO.existeConflitoHorario(local, dataHora, idEventoExcluir);
        if (conflito) {
            throw new NegocioException("Ja existe um evento agendado no local \""
                    + local + "\" para esta data e horario.");
        }
    }

    
    public static void verificarInscricaoDuplicada(int idUsuario,
                                                   int idEvento) throws SQLException, NegocioException {
        InscricaoDAO inscricaoDAO = new InscricaoDAO();
        boolean duplicada = inscricaoDAO.existeInscricao(idUsuario, idEvento);
        if (duplicada) {
            throw new NegocioException("Este usuario ja esta inscrito neste evento.");
        }
    }
}