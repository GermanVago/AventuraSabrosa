import javax.swing.ImageIcon;

public class Personaje {
    private PersonajeInfo info;
    private int nivel;
    private int puntos;
    private ImageIcon imagenActual;
    
    public Personaje(PersonajeInfo info) {
        this.info = info;
        this.nivel = 1;
        this.puntos = 0;
        cargarImagen();
    }
    
    private void cargarImagen() {
        try {
            imagenActual = new ImageIcon(getClass().getResource("/resources/" + info.getImagen()));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + info.getImagen());
            e.printStackTrace();
        }
    }
    
    public ImageIcon getImagen() {
        return imagenActual;
    }
    
    public String getDescripcion() {
        return info.getDescripcion();
    }
    
    public int getNivel() {
        return nivel;
    }
    
    public int getPuntos() {
        return puntos;
    }
    
    public void aumentarPuntos(int cantidad) {
        puntos += cantidad;
        if (puntos >= nivel * 100) {
            subirNivel();
        }
    }
    
    private void subirNivel() {
        nivel++;
        // Aquí podrías modificar la imagen según el nivel
    }
}