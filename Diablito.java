public class Diablito {
    private static final String[] CONSEJOS_NEGATIVOS = {
        "¡Usa la carta de la soda!",
        "¡Las papas fritas son la mejor opción!",
        "¡Los dulces te darán más energía!",
        "¡La comida chatarra es más divertida!",
        "¡No necesitas verduras para ser fuerte!",
        "¡Las frutas son aburridas!",
        "¡El ejercicio es para débiles!"
    };
    
    public static String obtenerConsejoAleatorio() {
        int indice = (int)(Math.random() * CONSEJOS_NEGATIVOS.length);
        return CONSEJOS_NEGATIVOS[indice];
    }
}