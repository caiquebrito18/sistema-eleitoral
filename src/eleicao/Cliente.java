package eleicao;

import java.awt.*;
import java.rmi.Naming;
import java.util.Scanner;
import java.util.Set;

public class Cliente implements Runnable {

    private UrnaInterface urna;

    @Override
    public void run() {
        try {
            this.urna = this.getUrnaService();
            System.out.println("INICIADA A VOTAÇÃO");
        } catch (Exception ex) {
            System.err.println(ex);
            System.exit(1);
        }

        this.capturarVotos();

    }

    private UrnaInterface getUrnaService() throws Exception {
        return (UrnaInterface) Naming.lookup("rmi://localhost/Urna");
    }

    public void capturarVotos() {

        boolean exit = false;

        try {
            final Scanner teclado = new Scanner(System.in);

            System.out.println("INFORME APENAS O NUMERO DO SEU CANDIDATO E PRESSIONE ENTER");


            while (!exit) {
                System.out.println("\n------- CANDIDATOS -------");
                this.mostrarCandidatos();
                System.out.println("------------------");

                System.out.print("Número do candidato: ");
                final String voto = Console.readCommand(teclado);

                try {
                    Integer numero = Integer.parseInt(voto);
                    this.urna.votar(numero);
                } catch (NumberFormatException ex) {
                    System.err.println("cliente@localhost$> voto invalido!");
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void mostrarCandidatos() throws Exception {
        final Set<Candidato> candidatos = this.urna.getCandidatos();
        candidatos.stream()
                .forEach((candidato) -> {
                    System.out.println("CANDIDATO: "+candidato.getNome()+"| NUMERO: "+candidato.getNumero());
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Cliente());
    }

}
