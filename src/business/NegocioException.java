package business;
     /**
     NegocioException representa uma Violacao DE Regra de negocio:
      evento lotado, inscrição duplicada ou horário conflitante
      */
public class NegocioException extends Exception {

    public NegocioException(String mensagem) {
        super(mensagem);
    }
}