package modelo;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Memoria {

    private static Memoria instancia = new Memoria();

    private TipoOperacao ultimaoperacao = null;
    private boolean substituir = false;
    private String textoAtual = "";
    private String textoBuffer = "";

    private final List<MemoriaObservador> obsservadores = new ArrayList<>();

    private enum TipoOperacao {
        ZERAR, NUMERO, VIRGULA, SINAL, SOMA, SUBTRACAO, MULTIPLICACAO, DIVISAO, PORCENTAGEM, RESULTADO;
    }

    private Memoria() {
    }

    public static Memoria getInstancia() {
        return instancia;
    }

    public void adicionarObservador(MemoriaObservador observador) {
        obsservadores.add(observador);
    }

    public String getTextoAtual() {
        return textoAtual.isEmpty() ? "0" : textoAtual;
    }

    public void processarComando(String texto) {

        TipoOperacao operacao = detectarOperacao(texto);

        if (operacao == null) {
            return;
        } else if (operacao == TipoOperacao.ZERAR) {
            textoAtual = "";
            textoBuffer = "";
            substituir = false;
            ultimaoperacao = null;
        } else if (operacao == TipoOperacao.SINAL && !textoAtual.contains("-")) {
            textoAtual = "-" + textoAtual;
        } else if (operacao == TipoOperacao.SINAL && textoAtual.contains("-")) {
            textoAtual = textoAtual.substring(1);
        } else if (operacao == TipoOperacao.NUMERO || operacao == TipoOperacao.VIRGULA) {
            textoAtual = substituir ? texto : textoAtual + texto;
            substituir = false;
        } else {
            substituir = true;
            textoAtual = obterResultadoOperacao();
            textoBuffer = textoAtual;
            ultimaoperacao = operacao;
        }


        obsservadores.forEach(o -> o.valorAlterado(getTextoAtual()));
    }

    private String obterResultadoOperacao() {
        if (ultimaoperacao == null || ultimaoperacao == TipoOperacao.RESULTADO) {
            return textoAtual;
        }

        double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
        double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));

        double resultado = 0;

        if (ultimaoperacao == TipoOperacao.SOMA) {
            resultado = numeroBuffer + numeroAtual;
        } else if (ultimaoperacao == TipoOperacao.MULTIPLICACAO) {
            resultado = numeroBuffer * numeroAtual;
        } else if (ultimaoperacao == TipoOperacao.SUBTRACAO) {
            resultado = numeroBuffer - numeroAtual;
        } else if (ultimaoperacao == TipoOperacao.DIVISAO) {
            resultado = numeroBuffer / numeroAtual;
        } else if (ultimaoperacao == TipoOperacao.PORCENTAGEM) {
            resultado = (numeroBuffer * numeroAtual) / 100;
        }

        String resultadoString = Double.toString(resultado).replace(".", ",");
        boolean inteiro = resultadoString.endsWith(",0");

        return inteiro ? resultadoString.replace(",0", "") : resultadoString;
    }

    private TipoOperacao detectarOperacao(String texto) {
        if (textoAtual.isEmpty() && texto == "0") {
            return null;
        }

        try {
            Integer.parseInt(texto);
            return TipoOperacao.NUMERO;
        } catch (NumberFormatException e) {
            if ("AC".equals(texto)) {
                return TipoOperacao.ZERAR;
            } else if ("+".equals(texto)) {
                return TipoOperacao.SOMA;
            } else if ("*".equals(texto)) {
                return TipoOperacao.MULTIPLICACAO;
            } else if ("-".equals(texto)) {
                return TipoOperacao.SUBTRACAO;
            } else if ("/".equals(texto)) {
                return TipoOperacao.DIVISAO;
            } else if (",".equals(texto) && !textoAtual.contains(",")) {
                return TipoOperacao.VIRGULA;
            } else if ("±".equals(texto)) {
                return TipoOperacao.SINAL;
            } else if ("%".equals(texto)) {
                return TipoOperacao.PORCENTAGEM;
            } else if ("=".equals(texto)) {
                return TipoOperacao.RESULTADO;
            }

        }
        return null;
    }

    @Contract(pure = true)
    private @Nullable
    TipoOperacao detectarTipoOperacao(String texto) {

        return null;
    }

}
