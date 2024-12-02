package com.nutritiongame.tutorial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TutorialStep extends JFrame {
    private int currentMessage = 0;
    private final String[] messages;
    private final Image background;
    private final Image doctor;

    public TutorialStep(String backgroundPath, String doctorPath, String[] tutorialMessages) {
        setTitle("Tutorial de Alimentación");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cargar imágenes y mensajes
        this.background = new ImageIcon(backgroundPath).getImage();
        this.doctor = new ImageIcon(doctorPath).getImage();
        this.messages = tutorialMessages;

        // Detectar teclas
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (currentMessage < messages.length - 1) {
                        currentMessage++;
                    } else {
                        JOptionPane.showMessageDialog(null, "¡Fin del tutorial! Ahora empieza el juego.");
                        dispose();
                    }
                    repaint();
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar fondo
        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        // Dibujar personaje
        g2d.drawImage(doctor, 50, 200, 150, 300, this);

        // Dibujar mensaje actual
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.setColor(Color.BLACK);
        g2d.drawString(messages[currentMessage], 220, 150);

        // Instrucción para continuar
        String instruction = currentMessage < messages.length - 1
                ? "Presiona ESPACIO para continuar"
                : "Presiona ESPACIO para comenzar el juego";
        g2d.drawString(instruction, 220, 500);
    }
}
