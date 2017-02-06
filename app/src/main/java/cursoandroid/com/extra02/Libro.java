package cursoandroid.com.extra02;

/**
 * Created by Natalia on 21/01/2017.
 */

public class Libro {
    String titulo,autor,fecha,portada,sinopsis;

    public Libro() {
        this.titulo = "";
        this.autor = "";
        this.fecha = "";
        this.portada = "";
        this.sinopsis = "";
    }

    public Libro(String titulo) {
        this.titulo = titulo;
        this.autor = "";
        this.fecha = "";
        this.portada = "";
        this.sinopsis = "";
    }

    public Libro(String titulo, String autor, String fecha, String portada, String sinopsis) {
        this.titulo = titulo;
        this.autor = autor;
        this.fecha = fecha;
        this.portada = portada;
        this.sinopsis = sinopsis;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

}
