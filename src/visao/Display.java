package visao;

import modelo.Memoria;
import modelo.MemoriaObservador;

import javax.swing.*;
import java.awt.*;

public class Display extends JPanel implements MemoriaObservador {

    private final JLabel label;

    public Display() {
        Memoria.getInstancia().adicionarObservador(this);

        setBackground(Color.DARK_GRAY);

        label = new JLabel(Memoria.getInstancia().getTextoAtual());
        label.setForeground(Color.WHITE);
        label.setFont(new Font("courrier", Font.PLAIN, 30));

        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        add(label);

    }

    @Override
    public void valorAlterado(String novoValor) {
        label.setText(novoValor);
    }
}
