import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client extends Thread{
	private Socket client;
	private String ip;
	private int porta;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String nome;
	private boolean confermaRicezione;

	public Client(String ip, int porta, String nome) {
		super();
		
		this.ip = ip;
		this.porta = porta;
		setNome(nome);
		in=null;
		out=null;
		
		connetti();
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	private void connetti() {
		try {
			client=new Socket(ip,porta);
			out=new ObjectOutputStream(client.getOutputStream());
			in=new ObjectInputStream(client.getInputStream());

			System.out.println("\nDopo essermi connesso al server invio il pacchetto per la richiesta di connessione");
			out.writeObject(new Pacchetto(nome, 100));

			Pacchetto risposta = (Pacchetto) in.readObject();
			System.out.println("Risposta dal server: "+risposta);
			if(risposta.getCode() != 101) {
				System.out.println("Il server non ha approvato la connessione");
				chiudiSocket();
			} else {
				this.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problemi di apertura del socket");
			chiudiSocket();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Problemi nella ricezione dell'oggetto ricevuto dal server");
			chiudiSocket();
		}
	}

	@Override
	public void run() {
		boolean closed=false;
		while (!closed) {
            Pacchetto entrata = new Pacchetto();
            try {
				entrata = (Pacchetto) in.readObject();
				switch (entrata.getCode()) {
					case 200:
						System.out.println(entrata);
						invia(new Pacchetto("messaggio ricevuto",entrata.getCode()+1));
						break;
					case 201:
						System.out.println("risposta server: "+entrata);//debug
						break;
					case 210:
						String[] split=entrata.getMess().split("!",2);
						System.out.println(split[0]+" has whispered to you: "+split[1]);
						out.writeObject(new Pacchetto(entrata.getMess(),entrata.getCode()+1));
						break;
					case 211:
						System.out.println("Conferma consegna del whisper: "+entrata);
						break;
					case 411:
						System.out.println("Termina comunicazione con: "+entrata);
						chiudiSocket();
						closed=true;
						break;
					default:
						break;
				}
            } catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				break;
            }
		}
	}
	
	public void invia(Pacchetto pacchetto) {
		try {
			confermaRicezione=pacchetto.getCode()%10==1;
			//i codici esenti sono le conferme entranti di avvenuta consegna
			do {
				out.writeObject(pacchetto);
				System.out.println("invio: " + pacchetto);//debug
				Thread.sleep(100);
			} while (!confermaRicezione);
		} catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

	private void chiudiSocket() {
		try {
			client.close();
			System.out.println("Chiusura socket.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problemi nella chiusura del socket");
			System.exit(1);
		}
	}
}