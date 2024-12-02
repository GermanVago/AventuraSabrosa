package com.nutritiongame.screens;

import com.nutritiongame.Screen;

import javax.swing.*;
import java.awt.*;

public class TutorialScreen extends JPanel implements Screen {
    private final String[] messages;
    private int currentMessage;
    private final Image background;
    private final Image doctor;

    public TutorialScreen() {
        setLayout(null);
        currentMessage = 0;

        // Mensajes completos del tutorial
        messages = new String[]{
                "¡Hola, amiguito! Soy la Dra. Saludable, y estoy aquí para enseñarte algo muy importante: " +
                        "cómo elegir alimentos saludables para que tengas energía, fuerza, ¡y te sientas increíble todos los días! ¿Listo para aprender?",
                "Primero que nada, quiero que sepas que no todos los alimentos nos hacen bien. Algunos son excelentes " +
                        "para que crezcas sano y fuerte, como las frutas, las verduras y los cereales. Pero otros, como las papas fritas, " +
                        "los refrescos y los dulces, pueden ser deliciosos, pero si los comes demasiado, no le hacen bien a tu cuerpo.",
                "Por ejemplo, las frutas como la manzana, el plátano y la sandía son buenísimas para ti. Tienen vitaminas que " +
                        "te ayudan a crecer y a mantenerte fuerte. Y las verduras, como el pepino y la zanahoria, son como pequeños superhéroes: " +
                        "te dan energía y cuidan tu cuerpo para que no te enfermes.",
                "Ahora, cuidado con alimentos como las hamburguesas, la pizza, los refrescos o los chocolates. No son malos si los " +
                        "comes de vez en cuando, pero si los comes todos los días, tu cuerpo puede sentirse cansado, ¡y podrías perder toda tu energía para jugar y aprender cosas nuevas!",
                "¿Sabías que elegir alimentos saludables también te ayuda a concentrarte en la escuela y a jugar mejor? " +
                        "Es como si le dieras a tu cuerpo el combustible correcto, como un coche que necesita buena gasolina para correr rápido.",
                "La clave está en el equilibrio: come más frutas y verduras todos los días, toma agua en lugar de refrescos, " +
                        "y haz un poco de ejercicio. ¡Tu cuerpo te lo agradecerá con mucha energía y salud!",
                "Bueno, amiguito, ahora sabes lo importante que es comer bien. En este juego, aprenderás a identificar qué alimentos " +
                        "son saludables y cuáles debes comer con moderación. ¡Usa todo lo que aprendiste aquí para ganar y convertirte en un experto en alimentación saludable! ¡Nos vemos en el juego!"
        };

        // Cargar imágenes
        background = new ImageIcon("resources.images/backgrounds/fondo_ladrillo_blanco_final.png").getImage();
        doctor = new ImageIcon("resources.images/characters/personajemaestra.png").getImage();

        // Hacer que este panel sea enfocable
        setFocusable(true);
        requestFocus();
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                    if (currentMessage < messages.length - 1) {
                        currentMessage++;
                    } else {
                        JOptionPane.showMessageDialog(null, "¡Fin del tutorial!");
                        com.nutritiongame.GameController.getInstance().switchScreen(new MainMenu());
                    }
                    repaint(); // Redibujar después de cambiar el mensaje
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(doctor, 50, 200, 150, 300, this);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.BLACK);

        // Dividir el mensaje en líneas si es muy largo
        int x = 220, y = 150;
        String[] lines = messages[currentMessage].split("(?<=\\G.{70})"); // Divide cada 70 caracteres
        for (String line : lines) {
            g.drawString(line, x, y);
            y += 25;
        }

        String instruction = (currentMessage < messages.length - 1)
                ? "Presiona ESPACIO para continuar"
                : "Presiona ESPACIO para salir";
        g.drawString(instruction, x, 500);
    }

    @Override
    public void render() {
        repaint();
    }

    @Override
    public void update() {
    }

    @Override
    public void handleInput() {
    }
}
