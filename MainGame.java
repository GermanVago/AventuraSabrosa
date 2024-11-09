import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainGame {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ArrayList<Carta> mazoJugador1;
    private ArrayList<Carta> mazoJugador2;
    private Personaje jugador1;
    private Personaje jugador2;
    private boolean modoBot;
    
    public MainGame() {
        frame = new JFrame("Juego de Nutrición");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        inicializarPantallas();
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private void inicializarPantallas() {
        // Pantalla de carga
        JPanel pantallaCredenciales = crearPantallaCarga();
        mainPanel.add(pantallaCredenciales, "CARGA");
        
        // Menú principal
        JPanel menuPrincipal = crearMenuPrincipal();
        mainPanel.add(menuPrincipal, "MENU");
        
        // Pantalla de selección de personajes
        JPanel seleccionPersonajes = crearSeleccionPersonajes();
        mainPanel.add(seleccionPersonajes, "SELECCION");
    }
    
    private JPanel crearPantallaCarga() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imagen = new ImageIcon(getClass().getResource("/resources/Pantalla de carga.png"));
                g.drawImage(imagen.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        JButton btnComenzar = new JButton("Comenzar");
        btnComenzar.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        
        panel.add(btnComenzar, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel crearMenuPrincipal() {
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imagen = new ImageIcon(getClass().getResource("/resources/fondo_ladrillo_blanco_final.png"));
                g.drawImage(imagen.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        JButton btnIniciar = new JButton("INICIAR");
        JButton btnOpciones = new JButton("OPCIONES");
        JButton btnTutorial = new JButton("TUTORIAL");
        
        btnIniciar.setBounds(300, 200, 200, 50);
        btnOpciones.setBounds(300, 280, 200, 50);
        btnTutorial.setBounds(300, 360, 200, 50);
        
        btnIniciar.addActionListener(e -> mostrarSubmenuIniciar());
        
        panel.add(btnIniciar);
        panel.add(btnOpciones);
        panel.add(btnTutorial);
        
        return panel;
    }
    
    private void mostrarSubmenuIniciar() {
        JDialog submenu = new JDialog(frame, "Modo de Juego", true);
        submenu.setLayout(new FlowLayout());
        
        JButton btn1vs1 = new JButton("1 VS 1");
        JButton btn1vsBot = new JButton("1 VS BOT");
        
        btn1vs1.addActionListener(e -> {
            modoBot = false;
            submenu.dispose();
            cardLayout.show(mainPanel, "SELECCION");
        });
        
        btn1vsBot.addActionListener(e -> {
            modoBot = true;
            submenu.dispose();
            cardLayout.show(mainPanel, "SELECCION");
        });
        
        submenu.add(btn1vs1);
        submenu.add(btn1vsBot);
        submenu.setSize(200, 100);
        submenu.setLocationRelativeTo(frame);
        submenu.setVisible(true);
    }
    
    private JPanel crearSeleccionPersonajes() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        JLabel descripcionLabel = new JLabel("Selecciona un personaje", SwingConstants.CENTER);
        
        PersonajeInfo[] personajes = {
            new PersonajeInfo("nina_WH_NL_LT.png", "NIÑA, sueter azul, con lentes"),
            new PersonajeInfo("nina_WH_NL.png", "NIÑA, sueter azul, sin lentes"),
            new PersonajeInfo("nina_WH_SD_LT.png", "NIÑA, sueter negro, con lentes"),
            new PersonajeInfo("nina_WH_SD.png", "NIÑA, sueter negro, sin lentes"),
            new PersonajeInfo("nino_WH_NL_LT1.png", "NIÑO, sueter azul, con lentes"),
            new PersonajeInfo("nino_WH_NL.png", "NIÑO, sueter azul, sin lentes"),
            new PersonajeInfo("nino_WH_SD_1.png", "NIÑO, sueter negro, con lentes"),
            new PersonajeInfo("nino_WH_SD_LT1.png", "NIÑO, sueter negro, sin lentes")
        };
        
        for (PersonajeInfo personaje : personajes) {
            JPanel personajePanel = new JPanel(new BorderLayout());
            JButton btnPersonaje = new JButton();
            
            try {
                ImageIcon imagen = new ImageIcon(getClass().getResource("/resources/" + personaje.getImagen()));
                Image scaledImage = imagen.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                btnPersonaje.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                btnPersonaje.setText("Error imagen");
                e.printStackTrace();
            }
            
            btnPersonaje.addActionListener(e -> {
                descripcionLabel.setText(personaje.getDescripcion());
                seleccionarPersonaje(personaje);
            });
            
            btnPersonaje.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    descripcionLabel.setText(personaje.getDescripcion());
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    descripcionLabel.setText("Selecciona un personaje");
                }
            });
            
            personajePanel.add(btnPersonaje, BorderLayout.CENTER);
            gridPanel.add(personajePanel);
        }
        
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(descripcionLabel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private void seleccionarPersonaje(PersonajeInfo personaje) {
        if (jugador1 == null) {
            jugador1 = new Personaje(personaje);
            JOptionPane.showMessageDialog(frame, 
                "Jugador 1 seleccionado:\n" + personaje.getDescripcion());
            if (modoBot) {
                // Selección automática para el bot
                seleccionarPersonajeBot();
            }
        } else if (jugador2 == null && !modoBot) {
            jugador2 = new Personaje(personaje);
            JOptionPane.showMessageDialog(frame, 
                "Jugador 2 seleccionado:\n" + personaje.getDescripcion());
            iniciarJuego();
        }
    }
    
    private void seleccionarPersonajeBot() {
        // El bot siempre será el "Señor Grasoso"
        PersonajeInfo personajeBot = new PersonajeInfo("senor_grasoso.png", "Señor Grasoso");
        jugador2 = new Personaje(personajeBot);
        JOptionPane.showMessageDialog(frame, "¡El Señor Grasoso será tu oponente!");
        iniciarJuego();
    }
    
    private void iniciarJuego() {
        JuegoCartas juego = new JuegoCartas(jugador1, jugador2);
        JPanel panelJuego = crearPantallaJuego(juego);
        mainPanel.add(panelJuego, "JUEGO");
        cardLayout.show(mainPanel, "JUEGO");
    
        // Iniciar el juego
        iniciarDuelos(juego);
    }

private void iniciarDuelos(JuegoCartas juego) {
    if (!juego.hayCartasRestantes()) {
        determinarGanador(jugador1, jugador2);
        return;
    }
    
    // El juego ahora se maneja por turnos a través de la interfaz gráfica
    // La lógica de juego se ha movido a los event listeners de los botones
}
    
    private JPanel crearPantallaJuego(JuegoCartas juego) {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior para información del juego
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        JLabel jugador1Label = new JLabel("Jugador 1: " + jugador1.getPuntos() + " pts");
        JLabel turnLabel = new JLabel("Turno: Jugador 1", SwingConstants.CENTER);
        JLabel jugador2Label = new JLabel("Bot: " + jugador2.getPuntos() + " pts", SwingConstants.RIGHT);
        infoPanel.add(jugador1Label);
        infoPanel.add(turnLabel);
        infoPanel.add(jugador2Label);
        panel.add(infoPanel, BorderLayout.NORTH);
    
        // Panel central para las cartas
        JPanel cartasPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // Panel para la carta del jugador 1
        JPanel jugador1Panel = new JPanel(new BorderLayout());
        JLabel cartaJugador1 = new JLabel();
        cartaJugador1.setPreferredSize(new Dimension(200, 300));
        jugador1Panel.add(cartaJugador1, BorderLayout.CENTER);
        
        // Panel para la carta del bot
        JPanel jugador2Panel = new JPanel(new BorderLayout());
        JLabel cartaJugador2 = new JLabel();
        cartaJugador2.setPreferredSize(new Dimension(200, 300));
        jugador2Panel.add(cartaJugador2, BorderLayout.CENTER);
        
        cartasPanel.add(jugador1Panel);
        cartasPanel.add(jugador2Panel);
        panel.add(cartasPanel, BorderLayout.CENTER);
    
        // Panel inferior para controles
        JPanel controlPanel = new JPanel(new FlowLayout());
        
        // Botones de nutrientes
        JButton btnProteina = new JButton("Proteína");
        JButton btnCarbohidratos = new JButton("Carbohidratos");
        JButton btnGrasas = new JButton("Grasas");
        JButton btnVitaminas = new JButton("Vitaminas");
        JButton btnRobarCarta = new JButton("Robar Carta");
    
        // Acción para robar carta
        btnRobarCarta.addActionListener(e -> {
            Carta cartaJugador = juego.robarCartaJugador1();
            if (cartaJugador != null) {
                ImageIcon iconJugador = new ImageIcon(getClass().getResource(cartaJugador.getImagen()));
                Image imgJugador = iconJugador.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                cartaJugador1.setIcon(new ImageIcon(imgJugador));
                
                // El bot roba su carta automáticamente
                Carta cartaBot = juego.robarCartaJugador2();
                if (cartaBot != null) {
                    ImageIcon iconBot = new ImageIcon(getClass().getResource(cartaBot.getImagen()));
                    Image imgBot = iconBot.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                    cartaJugador2.setIcon(new ImageIcon(imgBot));
                    
                    // Habilitar botones de nutrientes
                    btnProteina.setEnabled(true);
                    btnCarbohidratos.setEnabled(true);
                    btnGrasas.setEnabled(true);
                    btnVitaminas.setEnabled(true);
                    btnRobarCarta.setEnabled(false);
                }
            }
        });
    
        // Configurar acciones para los botones de nutrientes
        ActionListener nutrienteListener = e -> {
            JButton btnPresionado = (JButton) e.getSource();
            String nutriente = btnPresionado.getText().toLowerCase();
            
            juego.jugarDuelo(nutriente);
            
            // Actualizar puntuaciones
            jugador1Label.setText("Jugador 1: " + jugador1.getPuntos() + " pts");
            jugador2Label.setText("Bot: " + jugador2.getPuntos() + " pts");
            
            // Limpiar cartas y preparar siguiente ronda
            cartaJugador1.setIcon(null);
            cartaJugador2.setIcon(null);
            
            // Deshabilitar botones de nutrientes y habilitar robar carta
            btnProteina.setEnabled(false);
            btnCarbohidratos.setEnabled(false);
            btnGrasas.setEnabled(false);
            btnVitaminas.setEnabled(false);
            btnRobarCarta.setEnabled(true);
            
            // Verificar si el juego ha terminado
            if (!juego.hayCartasRestantes()) {
                determinarGanador(jugador1, jugador2);
                cardLayout.show(mainPanel, "MENU");
            }
        };
    
        btnProteina.addActionListener(nutrienteListener);
        btnCarbohidratos.addActionListener(nutrienteListener);
        btnGrasas.addActionListener(nutrienteListener);
        btnVitaminas.addActionListener(nutrienteListener);
        
        // Inicialmente, deshabilitar botones de nutrientes
        btnProteina.setEnabled(false);
        btnCarbohidratos.setEnabled(false);
        btnGrasas.setEnabled(false);
        btnVitaminas.setEnabled(false);
    
        controlPanel.add(btnRobarCarta);
        controlPanel.add(btnProteina);
        controlPanel.add(btnCarbohidratos);
        controlPanel.add(btnGrasas);
        controlPanel.add(btnVitaminas);
        
        // Agregar panel de consejos del Diablito
        JPanel consejoPanel = new JPanel(new FlowLayout());
        JLabel consejoLabel = new JLabel();
        JButton btnConsejo = new JButton("¿Qué dice el Diablito?");
        btnConsejo.addActionListener(e -> {
            String consejo = Diablito.obtenerConsejoAleatorio();
            consejoLabel.setText(consejo);
        });
        consejoPanel.add(btnConsejo);
        consejoPanel.add(consejoLabel);
        
        // Panel para información de cartas restantes
        JPanel cartasRestantesPanel = new JPanel(new FlowLayout());
        JLabel cartasRestantesLabel = new JLabel("Cartas restantes: " + juego.getCartasRestantesJugador1());
        cartasRestantesPanel.add(cartasRestantesLabel);
        
        // Panel combinado para controles inferiores
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        bottomPanel.add(controlPanel);
        bottomPanel.add(consejoPanel);
        bottomPanel.add(cartasRestantesPanel);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Timer para actualizar la información
        Timer updateTimer = new Timer(1000, e -> {
            cartasRestantesLabel.setText("Cartas restantes: " + juego.getCartasRestantesJugador1());
        });
        updateTimer.start();
        
        return panel;
    }
    
    // Actualización del método determinarGanador
    private void determinarGanador(Personaje jugador1, Personaje jugador2) {
        String mensaje;
        if (jugador1.getPuntos() > jugador2.getPuntos()) {
            mensaje = "¡Felicidades! Has ganado al Señor Grasoso\n" +
                     "Puntaje final:\n" +
                     "Tú: " + jugador1.getPuntos() + " puntos\n" +
                     "Señor Grasoso: " + jugador2.getPuntos() + " puntos";
        } else if (jugador2.getPuntos() > jugador1.getPuntos()) {
            mensaje = "¡El Señor Grasoso ha ganado!\n" +
                     "Puntaje final:\n" +
                     "Señor Grasoso: " + jugador2.getPuntos() + " puntos\n" +
                     "Tú: " + jugador1.getPuntos() + " puntos";
        } else {
            mensaje = "¡Es un empate!\n" +
                     "Puntaje final: " + jugador1.getPuntos() + " puntos";
        }
        
        int opcion = JOptionPane.showConfirmDialog(
            frame,
            mensaje + "\n¿Quieres jugar otra vez?",
            "Fin del juego",
            JOptionPane.YES_NO_OPTION
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            reiniciarJuego();
        } else {
            cardLayout.show(mainPanel, "MENU");
        }
    }
    
    // Método para reiniciar el juego
    private void reiniciarJuego() {
        // Reiniciar puntuaciones
        jugador1 = new Personaje(jugador1.getInfo());
        jugador2 = new Personaje(jugador2.getInfo());
        
        // Crear nuevo juego
        JuegoCartas nuevoJuego = new JuegoCartas(jugador1, jugador2);
        JPanel panelJuego = crearPantallaJuego(nuevoJuego);
        
        // Reemplazar el panel de juego anterior
        mainPanel.remove(mainPanel.getComponent(mainPanel.getComponentCount() - 1));
        mainPanel.add(panelJuego, "JUEGO");
        cardLayout.show(mainPanel, "JUEGO");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGame());
    }
}