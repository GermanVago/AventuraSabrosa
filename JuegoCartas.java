import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class JuegoCartas {
    private ArrayList<Carta> mazoJugador1;
    private ArrayList<Carta> mazoJugador2;
    private Personaje jugador1;
    private Personaje jugador2;
    private Random random;

    public JuegoCartas(Personaje jugador1, Personaje jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.mazoJugador1 = generarMazoInicial();
        this.mazoJugador2 = generarMazoBot();
        this.random = new Random();
    }

    private ArrayList<Carta> generarMazoInicial() {
        ArrayList<Carta> mazo = new ArrayList<>();

        // Agregar cartas de alimentos
        mazo.add(new Carta("Chocolate", 5, 25, 15, 2, "/resources/Cartas/Cartas Foodtrash/Comida_chocolate.png"));
        mazo.add(new Carta("Hamburguesa", 10, 30, 20, 2, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Hamburguesa.png"));
        mazo.add(new Carta("Hot Dog", 8, 25, 15, 1, "/resources/Cartas/Cartas Foodtrash/Foodtrash_HotDog.png"));
        mazo.add(new Carta("Pizza", 12, 35, 18, 3, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Pizza.png"));
        mazo.add(new Carta("Soda", 0, 40, 0, 0, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Soda.png"));

        // Barajar el mazo
        Collections.shuffle(mazo);
        return mazo;
    }

    private ArrayList<Carta> generarMazoBot() {
        ArrayList<Carta> mazo = new ArrayList<>();

        // Agregar cartas de alimentos para el bot (Señor Grasoso)
        mazo.add(new Carta("Chocolate", 5, 25, 15, 2, "/resources/Cartas/Cartas Foodtrash/Comida_chocolate.png"));
        mazo.add(new Carta("Hamburguesa", 10, 30, 20, 2, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Hamburguesa.png"));
        mazo.add(new Carta("Hot Dog", 8, 25, 15, 1, "/resources/Cartas/Cartas Foodtrash/Foodtrash_HotDog.png"));
        mazo.add(new Carta("Pizza", 12, 35, 18, 3, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Pizza.png"));
        mazo.add(new Carta("Soda", 0, 40, 0, 0, "/resources/Cartas/Cartas Foodtrash/Foodtrash_Soda.png"));

        // Barajar el mazo
        Collections.shuffle(mazo);
        return mazo;
    }

    public void jugarDuelo(String nutriente) {
        if (mazoJugador1.isEmpty() || mazoJugador2.isEmpty()) {
            return; // No hay más cartas para jugar
        }

        Carta cartaJugador1 = mazoJugador1.remove(0);
        Carta cartaJugador2 = mazoJugador2.remove(0);

        int valorJugador1 = cartaJugador1.getValorNutriente(nutriente);
        int valorJugador2 = cartaJugador2.getValorNutriente(nutriente);

        // Mostrar las cartas en la interfaz
        mostrarCartasEnPantalla(cartaJugador1, cartaJugador2);

        if (valorJugador1 > valorJugador2) {
            // Jugador 1 gana
            jugador1.aumentarPuntos(10);
            System.out.println("¡Jugador 1 gana este duelo!");
        } else if (valorJugador2 > valorJugador1) {
            // Jugador 2 gana
            jugador2.aumentarPuntos(10);
            System.out.println("¡Jugador 2 gana este duelo!");
        } else {
            // Empate
            System.out.println("¡Es un empate!");
        }

        // Mostrar los puntajes actualizados
        mostrarPuntajes();
    }

    private void mostrarCartasEnPantalla(Carta carta1, Carta carta2) {
        // Implementar la lógica para mostrar las cartas en la interfaz gráfica
        System.out.println("Carta 1: " + carta1.getNombre() + " - Imagen: " + carta1.getImagen());
        System.out.println("Carta 2: " + carta2.getNombre() + " - Imagen: " + carta2.getImagen());
    }

    private void mostrarPuntajes() {
        // Implementar la lógica para mostrar los puntajes de los jugadores en la interfaz
        System.out.println("Puntaje Jugador 1: " + jugador1.getPuntos());
        System.out.println("Puntaje Jugador 2: " + jugador2.getPuntos());
    }

    public boolean hayCartasRestantes() {
        return !mazoJugador1.isEmpty() && !mazoJugador2.isEmpty();
    }

    public String seleccionarNutrienteAleatorio() {
        String[] nutrientes = {"proteina", "carbohidratos", "grasas", "vitaminas"};
        return nutrientes[random.nextInt(nutrientes.length)];
    }
}