// JuegoCartas.java
import java.util.ArrayList;
import java.util.Collections;

public class JuegoCartas {
    private ArrayList<Carta> mazoJugador1;
    private ArrayList<Carta> mazoJugador2;
    private Personaje jugador1;
    private Personaje jugador2;

    public JuegoCartas(Personaje jugador1, Personaje jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.mazoJugador1 = generarMazoInicial();
        this.mazoJugador2 = generarMazoInicial();
    }

    private ArrayList<Carta> generarMazoInicial() {
        ArrayList<Carta> mazo = new ArrayList<>();

        // Agregar cartas de alimentos aquí
        mazo.add(new Carta("Manzana", 2, 14, 0, 4, "manzana.png"));
        mazo.add(new Carta("Hamburguesa", 15, 30, 20, 2, "hamburguesa.png"));
        mazo.add(new Carta("Zanahoria", 1, 10, 0, 6, "zanahoria.png"));
        mazo.add(new Carta("Soda", 0, 40, 0, 0, "soda.png"));
        // Agrega más cartas según sea necesario

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
    }

    private void mostrarPuntajes() {
        // Implementar la lógica para mostrar los puntajes de los jugadores en la interfaz
    }

    public boolean hayCartasRestantes() {
        return !mazoJugador1.isEmpty() && !mazoJugador2.isEmpty();
    }
}