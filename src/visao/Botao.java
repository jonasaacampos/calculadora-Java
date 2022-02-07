package visao;

import javax.swing.*;
import java.awt.*;

public class Botao extends JButton {

    public Botao(String texto, Color cor) {
        setText(texto);
        setOpaque(true);
        setBackground(cor);
        setFont(new Font("courrier", Font.PLAIN, 25));
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder((Color.BLACK)));

    }
}
