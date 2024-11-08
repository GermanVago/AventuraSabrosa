import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Carta {
    private String nombre;
    private int valorProteina;
    private int valorCarbohidratos;
    private int valorGrasas;
    private int valorVitaminas;
    private String imagen;
    
    public Carta(String nombre, int proteina, int carbohidratos, int grasas, int vitaminas, String imagen) {
        this.nombre = nombre;
        this.valorProteina = proteina;
        this.valorCarbohidratos = carbohidratos;
        this.valorGrasas = grasas;
        this.valorVitaminas = vitaminas;
        this.imagen = imagen;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getImagen() {
        return imagen;
    }
    
    public int getValorNutriente(String nutriente) {
        switch (nutriente.toLowerCase()) {
            case "proteina": return valorProteina;
            case "carbohidratos": return valorCarbohidratos;
            case "grasas": return valorGrasas;
            case "vitaminas": return valorVitaminas;
            default: return 0;
        }
    }
    
    public int[] getValoresNutricionales() {
        return new int[]{valorProteina, valorCarbohidratos, valorGrasas, valorVitaminas};
    }
}