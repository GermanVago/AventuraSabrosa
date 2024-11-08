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
        mazoJugador1 = generarMazoInicial();
        mazoJugador2 = modoBot ? generarMazoBot() : generarMazoInicial();
        
        JPanel panelJuego = crearPantallaJuego();
        mainPanel.add(panelJuego, "JUEGO");
        cardLayout.show(mainPanel, "JUEGO");
    }
    
    private ArrayList<Carta> generarMazoInicial() {
        ArrayList<Carta> mazo = new ArrayList<>();
        // Aquí agregarías las cartas con sus valores nutricionales
        return mazo;
    }
    
    private ArrayList<Carta> generarMazoBot() {
        ArrayList<Carta> mazo = new ArrayList<>();
        // Aquí agregarías las cartas específicas para el bot
        return mazo;
    }
    
    private JPanel crearPantallaJuego() {
        JPanel panel = new JPanel(new BorderLayout());
        // Implementar la interfaz del juego
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGame());
    }
}