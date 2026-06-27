

public class Usuario {
    private final String id;
    private final String nombre;
    private int elo;                                  // puntaje de habilidad, estilo videojuego
    private final MiListaEnlazada<String> historialPartidos; // ids de partidos jugados

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.elo = 1000; // puntaje inicial neutro, como en sistemas tipo ELO de ajedrez/videojuegos
        this.historialPartidos = new MiListaEnlazada<>();
    }

    public void registrarPartidoJugado(String idPartido) {
        historialPartidos.insertarFinal(idPartido);
    }

    public MiListaEnlazada<String> getHistorialPartidos() { return historialPartidos; }

    public void ajustarElo(int delta) { this.elo += delta; }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public int getElo() { return elo; }

    @Override
    public String toString() {
        return nombre + " (ELO: " + elo + ")";
    }
}
