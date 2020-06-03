package ep1_redes;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

public class ClienteConexao {

    private String userNamePopup;
    private String statusPopup;
    static long tempo_validacao_keep_alive = 10000;
    static int portalLocalParaConexao = 8083;
    static String ipServidor = "192.168.0.2";
    static int portaServidor = 8082;
    static String myLocalIp;
    static Map<String, Cliente> listaContatos = new HashMap<String, Cliente>();
    static String myUserName;
    public static Cliente cli;
    // variaveis de itens graficos
    javax.swing.JTable tblContatoStatus;
    static frmClienteJanelaConversa janela;
    static frmClienteJanelaConversaRetorno janelaRetorno;
    // variaveis para conexao Sockets
    static ServerSocket socket_listener_client;
    Socket c_socket;
    DataOutputStream output;
    DataInputStream input;
    static ServerSocket s_socket;

    public ClienteConexao(String user, String userIP, Integer userPorta, String ipServer, Integer portaServer) {
        myUserName = user;
        myLocalIp = userIP;
        portalLocalParaConexao = userPorta;
        ipServidor = ipServer;
        portaServidor = portaServer;
    }

    /* 
     * Seta a janela utilizada por essa conexao para enviar novos chats  
     */
    public void ClienteConexaoSetarJanela(frmClienteJanelaConversa jan) {
        janela = jan;
    }

    /* 
     * Seta a janela utilizada por essa conexao para receber novos chats  
     */
    public void ClienteConexaoSetarJanela(frmClienteJanelaConversaRetorno jan) {
        janelaRetorno = jan;
    }

    /**
     * Chamado pelo formulario de login, eh responsavel por trazer os contatos do cliente
     * @param ipServidor
     * @param portaServidor
     * @param cliente
     * @throws IOException
     */
    public void loginServer(String ipServidor, Integer portaServidor, Cliente cliente) throws IOException {
        try {
            System.out.println("Comunicacao Servidor");
            this.cli = cliente;
            Socket cServer_socket = estabelecerComunicacao(ipServidor, portaServidor);
            output = new DataOutputStream(cServer_socket.getOutputStream());
            output.writeUTF("logou<.>" + cliente.userName);
            DataInputStream input = new DataInputStream(cServer_socket.getInputStream());
            String result = input.readUTF();
            cServer_socket.close();
            if(!result.equals("")){
                System.out.println(result);
                preencherListaContatos(result);
                enviarKeepAlive();
            }else{
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        JOptionPane.showMessageDialog(null, "Voce nao cadastrou seus amigos!", "Mr. Lonely", JOptionPane.ERROR_MESSAGE);
                    }
                });                
            }
        } catch (Exception e) {
            System.err.println("Erro em loginServer: " + e);
        }
    }

    /* 
     * Metodo para atualizar status (OFFLINE) do usario quando fecha a janela home (faz logoof)
     */
    public void updateAntesFechar() {
        try {
            Socket cServer_socket = estabelecerComunicacao(ipServidor, portaServidor);
            System.out.println();
            output = new DataOutputStream(cServer_socket.getOutputStream());
            output.writeUTF("tofechando<.>" + myUserName);
            cServer_socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* 
     * Metodo para capturar informacoes passadas pelo servidor: Lista de contatos e tempo de espera para enviar keep alive
     */
    private void preencherListaContatos(String result) {
        String[] linhas = result.split("_");
        for (String linha : linhas) {
            List<String> colunas = Arrays.asList(linha.split("/"));
            boolean status = false;
            if (Integer.parseInt(colunas.get(1)) == 0) {
                status = false;
            } else {
                status = true;
            }
            Cliente cliente = new Cliente(colunas.get(0), status, colunas.get(2), Integer.parseInt(colunas.get(3)));
            if (colunas.size() > 4) {
                List<String> aux = Arrays.asList(colunas.get(4).split(":"));
                if (aux.size() > 1) {
                    tempo_validacao_keep_alive = Long.parseLong(aux.get(1));
                }
            }

            listaContatos.put(cliente.userName, cliente);
        }

    }

    /* 
     * Metodo para estabelecer comunicacao em dois pontos
     * Se trata da ponta que inicia a comunicacao per to per
     */
    public void ComunicacaoP2P(String userName, Cliente cliente) {
        String answer = "";
        try {

            System.out.println("Comunicação p2p - IP: " + cliente.ip + " porta: " + cliente.porta + " username: " + userName);
            //criacao do socket
            Socket cP2P_socket = estabelecerComunicacao(cliente.ip, cliente.porta);

            //envio do primeiro valor para conexao
            output = new DataOutputStream(cP2P_socket.getOutputStream());
            output.writeUTF(userName.trim().toString());
            System.out.println("Enviei user: " + userName.trim().toString());

            janela.c_socket = cP2P_socket;

            if (listaContatos.containsKey(cliente.userName)) {
                cliente.c_socket = cP2P_socket;
            }

            // quando chegar uma msg, distribui pra todos
            while (!answer.equals("exit")) {
                DataInputStream input = new DataInputStream(cP2P_socket.getInputStream());
                answer = input.readUTF();
                System.out.println("Mensagem: " + answer);
                janela.registraMensagemRecebida(answer);
            }
            //fechamento do socket client
            cP2P_socket.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /* 
     * Metodo para enviar mensagem para um socket já aberto iformado, utilizada pelo form que inicializa novos chats (ComunicacaoP2P)
     */
    public void enviaMensagemSocket(Socket cP2P_socket, String answer) {
        try {
            System.out.println("Enviando mensagem: " + answer);
            output = new DataOutputStream(cP2P_socket.getOutputStream());
            output.writeUTF(answer);
            System.out.println("Mensagem enviada...");
        } catch (Exception e) {
            System.out.println("Erro ao enviaMensagemSocket P2P: " + e.getMessage());
        }
    }

    /* 
     * Metodo para enviar mensagem para um socket já aberto iformado, utilizada pelo form de mensagens recebidas (aberto em novas conexoes)
     */
    public static void enviaMensagemSocketRetorno(Socket cP2P_socket, String answer) {
        try {
            System.out.println("Enviando mensagem retorno: " + answer);
            DataOutputStream saida = new DataOutputStream(cP2P_socket.getOutputStream());
            saida.writeUTF(answer);
            System.out.println("Mensagem enviada...");
        } catch (Exception e) {
            System.out.println("Erro ao enviaMensagemSocket Retorno: " + e.getMessage());
        }
    }

    /* 
     * Metodo para realizar uma conexao simples e devolver o socket
     */
    public Socket estabelecerComunicacao(String ip, int porta) {
        try {
            //cria do socket	
            Socket s = new Socket(ip, porta);
            return s;
        } catch (IOException e) {
            System.out.println("Erro: " + e);
            return null;
        }
    }

    /*
     *  Metodo para receber conexoes e tratar a leitura de novas mensagens
     *  Implementa a porta de conexao dos contatos
     */
    public void novasConexoes() throws IOException {

        System.out.println("Iniciando...");

        try {
            s_socket = new ServerSocket(portalLocalParaConexao);
            System.out.println("Socket listening: " + InetAddress.getLocalHost() + " porta: " + portalLocalParaConexao);
            Socket c_socket_novas_conexoes;
            DataOutputStream output;
            while (true) {
                // Captura todas as novas requisicoes       
                c_socket_novas_conexoes = s_socket.accept();
                System.out.println("Nova conexao com o cliente " + c_socket_novas_conexoes.getInetAddress().getHostAddress());
                //Realiza leitura do usuario
                DataInputStream input = new DataInputStream(c_socket_novas_conexoes.getInputStream());
                String mensagem = input.readUTF().toString();

                List<String> mensagemCodificada = Arrays.asList(mensagem.split("<.>"));

                if (mensagemCodificada.contains("acorda") || mensagemCodificada.contains("dorme")) {
                    if (mensagemCodificada.contains("acorda")) {
                        alterarValorTabela(mensagemCodificada.get(1), 1, "ONLINE");
                        mostrarJanelaAlteracaoStatus(mensagemCodificada.get(1), "ONLINE");
                    } else {
                        alterarValorTabela(mensagemCodificada.get(1), 1, "OFFLINE");
                        mostrarJanelaAlteracaoStatus(mensagemCodificada.get(1), "OFFLINE");
                    }
                } else //Valida se usuario cadastrado                                
                if (listaContatos.containsKey(mensagem)) {
                    output = new DataOutputStream(c_socket_novas_conexoes.getOutputStream());
                    output.writeUTF("Conexao estabelecida...");
                    System.out.println("User: " + mensagem);
                    cli = listaContatos.get(mensagem);
                    cli.c_socket = c_socket_novas_conexoes;
                    Runnable r = new Runnable() {

                        public void run() {
                            try {
                                frmClienteJanelaConversaRetorno frmRetorno = frmClienteJanelaConversaRetorno.createMyObject();
                                frmRetorno.strMyUserName = myUserName;
                                frmRetorno.strUserNameContato = cli.userName;
                                frmRetorno.strStatusContato = cli.status ? "ONLINE" : "OFFLINE";
                                frmRetorno.strIPContato = cli.ip;
                                frmRetorno.intPortaContato = cli.porta;
                                frmRetorno.c_socket = cli.c_socket;
                                frmRetorno.setVisible(true);
                                janelaRetorno = frmRetorno.getInstanceOfForm();
                            } catch (Exception e) {
                                System.err.println("Erro ao abrir thread janela retorno: " + e.toString());
                            }
                        }
                    };
                    new Thread(r).start();

                    System.out.println("Classe thread: " + r.getClass().toString());
                    System.out.println("frmRetorno.strUserNameContato: " + janelaRetorno.strUserNameContato);

                    // quando chegar uma msg, distribui pra todos
                    while (!mensagem.equals("exit")) {
                        //recebimento do primeiro valor
                        input = new DataInputStream(c_socket_novas_conexoes.getInputStream());
                        mensagem = input.readUTF();
                        System.out.println("Mensagem: " + mensagem);
                        janelaRetorno.registraMensagemRecebida(mensagem);
                    }
                    //fechamento do socket client
                    c_socket_novas_conexoes.close();
                } else {
                    output = new DataOutputStream(c_socket_novas_conexoes.getOutputStream());
                    output.writeUTF("USUARIO NAO CADASTRADO");
                    System.out.println("USUARIO NAO CADASTRADO - User: " + mensagem);
                }
            }
        } catch (Exception e) {
            System.out.println("erro: " + e);
        } finally {
        }
    }

    /*
     *  Metodo que altera valor do status do cliente na lista de contatos
     */
    public int alterarValorTabela(String nomeContato, int indiceCampo, String valor) {
        try {
            for (int i = 0; i < tblContatoStatus.getHeight(); i++) {
                if (tblContatoStatus.getValueAt(i, 0).equals(nomeContato)) {
                    tblContatoStatus.setValueAt(valor, i, indiceCampo);
                    tblContatoStatus.repaint();
                    return i;
                }
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    /* 
     * Metodo que abre uma pequena janela para informa mudanca de status de contatos
     * Recebe como parametros: Nome do usuario e novo status
     */
    private void mostrarJanelaAlteracaoStatus(String uN, String sP) {
        this.userNamePopup = uN;
        this.statusPopup = sP;
        Runnable r = new Runnable() {

            public void run() {
                try {
                    frmAlertaStatus frm;
                    frm = new frmAlertaStatus(userNamePopup, statusPopup);
                    frm.setVisible(true);
                    frm.sleepAndDie();
                } catch (Exception e) {
                    System.err.println("Erro ao abrir janela de status: " + e.toString());
                }
            }
        };
        new Thread(r).start();
    }

    /* 
     * Metodo que envia de tempo em tempo (configuravel) mensagens de keep alive para o servidor
     */
    private void enviarKeepAlive() throws IOException, InterruptedException {
        Runnable r = new Runnable() {

            public void run() {
                try {
                    while (true) {
                        Thread.sleep(tempo_validacao_keep_alive);
                        System.out.println("Comunicacao Servidor, enviando enviarKeepAlive...");
                        Socket cServer_socket = estabelecerComunicacao(ipServidor, portaServidor);
                        output = new DataOutputStream(cServer_socket.getOutputStream());
                        output.writeUTF("keepAlive<.>" + cli.userName);
                    }
                } catch (Exception e) {
                    System.err.println("Erro no metodo ClienteConexao.enviarKeepAlive: " + e.toString());
                }
            }
        };
        new Thread(r).start();
    }
}
