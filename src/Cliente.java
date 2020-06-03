package ep1_redes;

import java.net.ServerSocket;
import java.net.Socket;

public class Cliente {

    public String userName;
    public boolean status;
    public int contador_keep_alive;
    public String ip;
    Socket c_socket;
    ServerSocket server_socket_p2p;
    public int porta;

    public Cliente(String user, boolean status, String ip, Socket socket) {
        this.userName = user;
        this.status = status;
        this.ip = ip;
        this.c_socket = socket;
    }

    public Cliente(String user, boolean status, String ip, int p) {
        this.userName = user;
        this.status = status;
        this.ip = ip;
        this.porta = p;
    }

    public Cliente() {
    }
}
