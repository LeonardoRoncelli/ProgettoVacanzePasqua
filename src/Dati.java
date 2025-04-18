public class Dati {
    private int voto;
    private String tema;
    public String getTema() {
        return tema;
    }
    public void setTema(String tema) throws Exception {
        if(tema.equalsIgnoreCase("Argomento1")||tema.equalsIgnoreCase("Argomento2")||tema.equalsIgnoreCase("Argomento3")) {
            this.tema = tema;
        }else {
            throw new Exception("Tema non disponibile");
        }
    }
    public int getVoto() {
        return voto;
    }
    public void setVoto(int voto) throws Exception {
        if(voto>=1&&voto<=10) {
            this.voto = voto;
        }else{
            throw new Exception("Voto errato");
        }
    }
    public Dati(int voto,String tema) throws Exception {
        setVoto(voto);
        setTema(tema);
    }
    public String toString(){
        return "Voto: "+voto+
                "\nTema: "+tema;
    }
}