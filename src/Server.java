import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
public class Server {
    //associare a 0 del client una votazione. associare a 1 la lista delle votazioni.
        private ServerSocket serverSocket;
        private Socket connessione;
        private PrintWriter out;
        private BufferedReader in;
        List<Dati>listaDati;
        ObjectMapper mapper = new ObjectMapper();
        public Server(ArrayList<Dati>listaDati) {
            this.connessione = null;
            this.listaDati=listaDati;
            try {
                serverSocket = new ServerSocket(9999, 10);
                System.out.println("Server > ASPETTANDO LA CONNESSIONE");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        public void run() {
            try {
                connessione = serverSocket.accept();
                System.out.println("Server > CONNESSO CON " + connessione.getInetAddress().getHostName());
                out = new PrintWriter(connessione.getOutputStream());
                out.flush();
                in = new BufferedReader(new InputStreamReader(connessione.getInputStream()));
                Comando fromClient=null;
                inviaMessaggio(mapper.writeValueAsString(new Comando("3","Sei connesso")));
                do {
                    try {
                        fromClient = mapper.readValue(in.readLine(), Comando.class);
                        System.out.println("Client >" + fromClient.getComando());
                        if (fromClient.getComando().equals("0")) {
                            String oggetto=fromClient.getOggetto();
                            Dati dati=mapper.readValue(oggetto,Dati.class);
                            listaDati.add(dati);
                            scriviSuFile();
                            inviaMessaggio(mapper.writeValueAsString(new Comando("0","tutto corretto")));
                        }else if (fromClient.getComando().equals("1")) {
                            System.out.println("Server > Inviando lista delle pizze...");
                            String oggetto= mapper.writeValueAsString(listaDati);
                            inviaMessaggio(mapper.writeValueAsString(new Comando("1",oggetto)));
                        } else if (fromClient.getComando().equals("bye")) {
                            System.out.println("Il client si sta disconnettendo");
                        } else{
                            System.out.println("Errore nei dati");
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                } while (fromClient!=null && !fromClient.getComando().equals("bye"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    connessione.close();
                    out.close();
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        private void inviaMessaggio(String msg) {
            try {
                PrintWriter pw = new PrintWriter(out);
                pw.println(msg);
                pw.flush();
                System.out.println("JSON: " + msg);
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        }
    private void scriviSuFile() {
        try {
            File file = new File("votazioni.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, listaDati);
            System.out.println("Server > Lista votazioni scritta su file.");
        } catch (IOException e) {
            System.err.println("Errore nella scrittura su file: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
            ArrayList<Dati>listaDati = new ArrayList<Dati>();
            Server server = new Server(listaDati);
            while (true) {
                server.run();
            }
        }
}