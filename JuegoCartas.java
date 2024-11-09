import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class JuegoCartas {
    private ArrayList<Carta> mazoJugador1;
    private ArrayList<Carta> mazoJugador2;
    private Personaje jugador1;
    private Personaje jugador2;
    private Random random;
    private Carta cartaActualJugador1;
    private Carta cartaActualJugador2;
    private boolean turnoJugador1;

    public JuegoCartas(Personaje jugador1, Personaje jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.mazoJugador1 = generarMazoInicial();
        this.mazoJugador2 = generarMazoBot();
        this.random = new Random();
        this.turnoJugador1 = true;
    }

    private ArrayList<Carta> generarMazoInicial() {
        ArrayList<Carta> mazo = new ArrayList<>();
        
        // Agregar comida chatarra
        mazo.add(new Carta("Chocolate", 2, 25, 15, 1, "/resources/Cartas/Cartas Foodtrash/Comida_chocolate.png"));
        mazo.add(new Carta("Hamburguesa", 10, 30, 20, 1, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Hamburguesa.png"));
        mazo.add(new Carta("Hot Dog", 8, 25, 15, 1, "/resources/Cartas/Cartas Foodtrash/Foodtrash_HotDog.png"));
        mazo.add(new Carta("Pizza", 12, 35, 18, 2, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Pizza.png"));
        mazo.add(new Carta("Soda", 0, 40, 0, 0, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Soda.png"));

        // Agregar frutas
        mazo.add(new Carta("Sandía", 1, 8, 0, 8, "/resources/Cartas/Cartas Frutas/Carta_fruta_Sandia.png"));
        mazo.add(new Carta("Mango", 1, 15, 0, 10, "/resources/Cartas/Cartas Frutas/Carta_Mango.png"));
        mazo.add(new Carta("Manzana", 0, 14, 0, 7, "/resources/Cartas/Cartas Frutas/Carta_manzana.png"));
        mazo.add(new Carta("Naranja", 1, 12, 0, 12, "/resources/Cartas/Cartas Frutas/Carta_Naranja.png"));
        mazo.add(new Carta("Plátano", 1, 23, 0, 9, "/resources/Cartas/Cartas Frutas/carta_platano.png"));
        mazo.add(new Carta("Fresa", 1, 7, 0, 9, "/resources/Cartas/Cartas Frutas/fruta_fresa.png"));

        // Agregar verduras
        mazo.add(new Carta("Cebolla", 1, 9, 0, 7, "/resources/Cartas/Cartas Verduras/Carta_Cebolla.png"));
        mazo.add(new Carta("Lechuga", 1, 3, 0, 6, "/resources/Cartas/Cartas Verduras/Carta_lechuga.png"));
        mazo.add(new Carta("Pepino", 1, 4, 0, 5, "/resources/Cartas/Cartas Verduras/Carta_Pepino.png"));
        mazo.add(new Carta("Tomate", 1, 4, 0, 8, "/resources/Cartas/Cartas Verduras/Carta_Tomate.png"));
        mazo.add(new Carta("Zanahoria", 1, 10, 0, 15, "/resources/Cartas/Cartas Verduras/Carta_Zanahoria.png"));
        mazo.add(new Carta("Brócoli", 3, 7, 0, 12, "/resources/Cartas/Cartas Verduras/Cartas_Brocoli.png"));

        Collections.shuffle(mazo);
        return mazo;
    }

    private ArrayList<Carta> generarMazoBot() {
        // El Señor Grasoso prefiere comida chatarra
        ArrayList<Carta> mazo = new ArrayList<>();
        
        // Múltiples copias de comida chatarra
        for (int i = 0; i < 3; i++) {
            mazo.add(new Carta("Chocolate", 2, 25, 15, 1, "/resources/Cartas/Cartas Foodtrash/Comida_chocolate.png"));
            mazo.add(new Carta("Hamburguesa", 10, 30, 20, 1, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Hamburguesa.png"));
            mazo.add(new Carta("Hot Dog", 8, 25, 15, 1, "/resources/Cartas/Cartas Foodtrash/Foodtrash_HotDog.png"));
            mazo.add(new Carta("Pizza", 12, 35, 18, 2, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Pizza.png"));
            mazo.add(new Carta("Soda", 0, 40, 0, 0, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Soda.png"));
        }

        // Algunas frutas y verduras (menos cantidad)
        mazo.add(new Carta("Manzana", 0, 14, 0, 7, "/resources/Cartas/Cartas Frutas/Carta_manzana.png"));
        mazo.add(new Carta("Plátano", 1, 23, 0, 9, "/resources/Cartas/Cartas Frutas/carta_platano.png"));
        mazo.add(new Carta("Lechuga", 1, 3, 0, 6, "/resources/Cartas/Cartas Verduras/Carta_lechuga.png"));
        mazo.add(new Carta("Zanahoria", 1, 10, 0, 15, "/resources/Cartas/Cartas Verduras/Carta_Zanahoria.png"));

        Collections.shuffle(mazo);
        return mazo;
    }

    public String seleccionarNutrienteBot(Carta cartaBot) {
        // Estrategia del bot: elige el nutriente con mayor valor en su carta
        int[] valores = cartaBot.getValoresNutricionales();
        int maxValor = 0;
        String nutrienteElegido = "proteina";
        
        String[] nutrientes = {"proteina", "carbohidratos", "grasas", "vitaminas"};
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] > maxValor) {
                maxValor = valores[i];
                nutrienteElegido = nutrientes[i];
            }
        }
        return nutrienteElegido;
    }

    public Carta robarCartaJugador1() {
        if (!mazoJugador1.isEmpty()) {
            cartaActualJugador1 = mazoJugador1.remove(0);
            return cartaActualJugador1;
        }
        return null;
    }

    public Carta robarCartaJugador2() {
        if (!mazoJugador2.isEmpty()) {
            cartaActualJugador2 = mazoJugador2.remove(0);
            return cartaActualJugador2;
        }
        return null;
    }

    public void jugarDuelo(String nutrienteSeleccionado) {
        if (cartaActualJugador1 == null || cartaActualJugador2 == null) {
            return;
        }

        int valorJugador1 = cartaActualJugador1.getValorNutriente(nutrienteSeleccionado);
        int valorJugador2 = cartaActualJugador2.getValorNutriente(nutrienteSeleccionado);

        if (valorJugador1 > valorJugador2) {
            jugador1.aumentarPuntos(10);
        } else if (valorJugador2 > valorJugador1) {
            jugador2.aumentarPuntos(10);
        }

        cartaActualJugador1 = null;
        cartaActualJugador2 = null;
    }

    public boolean hayCartasRestantes() {
        return !mazoJugador1.isEmpty() && !mazoJugador2.isEmpty();
    }

    public int getCartasRestantesJugador1() {
        return mazoJugador1.size();
    }

    public int getCartasRestantesJugador2() {
        return mazoJugador2.size();
    }
}