import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
public class Client {
        private Scanner scanner;
        private Socket connessione;
        private PrintWriter out;
        private BufferedReader in;
        ObjectMapper mapper = new ObjectMapper();
        public Client() {
            this.scanner =new Scanner(System.in);
        }
        public void run() {
            try {
                connessione = new Socket("localhost", 9999);
                System.out.println("Client > Connessione inviata");
                out = new PrintWriter(connessione.getOutputStream());
                out.flush();
                in = new BufferedReader(new InputStreamReader(connessione.getInputStream()));
                try {
                    //connessione
                    System.out.println("Server >" + mapper.readValue(in.readLine(), Comando.class).getOggetto());

                    //menù
                    int scelta;
                    do {
                        System.out.println(
                                "\nScegli un'opzione:" +
                                        "\n0) Manda una votazione" +
                                        "\n1) Ricevi lista" +
                                        "\n2) Chiudi connessione"
                        );
                        scelta = Integer.parseInt(scanner.next());
                        switch (scelta) {
                            case 0: {
                                int voto;
                                String tema;
                                System.out.print("Inserisci il voto: ");
                                voto=scanner.nextInt();
                                System.out.print("Inserisci il tema: ");
                                tema=scanner.next();
                                Dati dato=new Dati(voto,tema);
                                String oggetto=mapper.writeValueAsString(dato);
                                inviaMessaggio(mapper.writeValueAsString(new Comando("0",oggetto)));
                                System.out.println("Server >" + mapper.readValue(in.readLine(), Comando.class).getOggetto());
                                break;
                            }
                            case 1: {
                                inviaMessaggio(mapper.writeValueAsString(new Comando("1", "")));
                                Comando comando=mapper.readValue(in.readLine(),Comando.class);
                                ArrayList<Dati> lista = mapper.readValue(comando.getOggetto(), new TypeReference<ArrayList<Dati>>() {});
                                for(int i=0;i<lista.size();i++) {
                                    System.out.println(lista.get(i).toString());
                                }
                                break;
                            }
                            case 2: {
                                inviaMessaggio(mapper.writeValueAsString(new Comando("bye","")));
                                System.out.println("Arrivederci");
                                break;
                            }
                            default: {
                                System.err.println("[ERRORE] Opzione non valida, riprova.");
                            }
                        }
                    } while (scelta !=2);
                } catch (Exception classNot) {
                    System.err.println(classNot.getMessage());
                }
            } catch (UnknownHostException unknownHost) {
                System.err.println("[ERRORE] Host sconosciuto");
            } catch (Exception ioException) {
                System.err.println(ioException.getMessage());
            } finally {
                try {
                    scanner.close();
                    in.close();
                    out.close();
                    connessione.close();
                } catch (Exception ioException) {
                    System.err.println(ioException.getMessage());
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
        public static void main(String args[]) {
            Client client = new Client();
            client.run();
        }
    }