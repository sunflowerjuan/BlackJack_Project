package co.edu.uptc.view.title;

import javax.swing.*;

import co.edu.uptc.utils.ImgManager;

import java.awt.*;

public class MainTitlePane extends JPanel {

    private Image backgroundImage;

    public MainTitlePane() {
        // Cargar imagen de fondo
        backgroundImage = ImgManager.getImage("main_title");
        setLayout(new GridBagLayout());

        // Configurar alineación de los botones a la derecha
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado
        gbc.anchor = GridBagConstraints.EAST; // Alinear a la derecha
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Crear botones
        JButton btn1 = new JButton("Botón 1");
        JButton btn2 = new JButton("Botón 2");
        JButton btn3 = new JButton("Botón 3");

        // Agregar un espacio vacío en la primera columna para empujar los botones
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Empuja todo a la derecha
        add(Box.createHorizontalGlue(), gbc);

        gbc.gridx = 1; // Columna donde van los botones
        gbc.weightx = 0; // Evita que los botones se expandan
        add(btn1, gbc);

        gbc.gridy = 1;
        add(btn2, gbc);

        gbc.gridy = 2;
        add(btn3, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
