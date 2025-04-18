public class Comando {
    private String comando;
    private String oggetto;
    public String getComando() {
        return comando;
    }
    public void setComando(String comando) throws Exception{
        if(comando.equals("0")||comando.equals("1")){
            this.comando = comando;
        }else{
            throw new Exception("Comando non valido");
        }
    }
    public String getOggetto() {
        return oggetto;
    }
    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }
    public Comando(String comando,String oggetto)throws Exception{
        setComando(comando);
        setOggetto(oggetto);
    }
}