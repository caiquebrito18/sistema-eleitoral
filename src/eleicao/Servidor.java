package eleicao;

import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Servidor implements Runnable{
    private Urna urna;
    private ScheduledExecutorService executorService;

    @Override
    public void run() {
        try {
            this.urna = this.prepararUrna();

            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi:///Urna", this.urna);

            System.out.println("URNA INICIADA");
        } catch (RemoteException | MalformedURLException ex) {
            System.err.println(ex);
            System.exit(1);
        }

        this.apuracaoVotos();
    }

    private Urna prepararUrna() throws RemoteException {

        final Urna urnaVazia = new Urna();

        System.out.println("CADASTRE OS CANDIDATOS NO SEGUINTE PADRAO: NOME:NUMERO, NOME:NUMERO");

        final String comando = Console.readCommand(new Scanner(System.in));
        if (comando != null && !comando.isEmpty()) {
            final String[] arrayCandidatos = comando.split(",");
            for (String candidato : arrayCandidatos) {
                final String nome = candidato.split(":")[0];
                final String numero = candidato.split(":")[1];
                urnaVazia.adicionaCandidato(new Candidato(nome, Integer.parseInt(numero)));
            }
        }

        this.getCandidatos(urnaVazia);

        return urnaVazia;
    }

    private void getCandidatos(Urna urna) throws RemoteException {
        System.out.println("CANDIDATOS: ");
        final Set<Candidato> candidatos = urna.getCandidatos();

        candidatos.stream()
                .forEach((candidato) -> {
                    System.out.println("NOME: "+candidato.getNome()+"; NUMERO: "+candidato.getNumero());
        });
    }

    private void apuracaoVotos() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleAtFixedRate(() -> { urna.getParcial(); }, 5, 5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Servidor());
    }
}
