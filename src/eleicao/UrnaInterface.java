package eleicao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;

public interface UrnaInterface extends Remote {

    HashSet<Candidato> getCandidatos() throws RemoteException;

    boolean votar(Integer numero) throws RemoteException;

}
