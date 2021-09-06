package eleicao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;

public class Urna extends UnicastRemoteObject implements UrnaInterface {
    private HashMap<Candidato, Integer> votos;

    public Urna() throws RemoteException {
    }

    @Override
    public HashSet<Candidato> getCandidatos() throws RemoteException {
        return new HashSet<>(this.votos.keySet());
    }

    @Override
    public boolean votar(Integer numero) throws RemoteException {
        this.votos.keySet().stream()
                .filter((candidato) -> (candidato.getNumero().equals(numero)))
                .forEach((candidato) -> {this.votos.put(candidato, this.votos.get(candidato) + 1);
        });
        return true;
    }

    public void adicionaCandidato(Candidato candidato) {
        if (this.votos == null) {
            this.votos = new HashMap<>();
        }
        this.votos.put(candidato, 0);
    }

    public void getParcial() {
        System.out.println("RESULTADO PARCIAL: ");
        this.votos.keySet().stream()
                .forEach((candidato) -> {
                    System.out.println("CANDIDATO: "+candidato.getNome()+": TOTAL DE VOTOS: "+this.votos.get(candidato));
        });
    }
}
