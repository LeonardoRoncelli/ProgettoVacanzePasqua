public class Dati {
    private int voto;
    private String tema;
    public String getTema() {
        return tema;
    }
    public void setTema(String tema) {
        this.tema = tema;
    }
    public int getVoto() {
        return voto;
    }
    public void setVoto(int voto) {
        this.voto = voto;
    }
    public Dati(int voto,String tema){
        this.voto=voto;
        this.tema=tema;
    }
}