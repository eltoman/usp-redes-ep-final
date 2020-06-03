package ep1_redes;

import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Servidor {

    static ServerSocket s_socket;
    static int porta = 8081;
    static Map<String, Cliente> clientes = new HashMap<String, Cliente>();
    static Map<String, Collection<String>> clientesContatos = new HashMap<String, Collection<String>>();
    static frm_Servidor form;
    static ClienteBD clienteBD;
    private String mensagemContato;
    private String nomeContato;
    static int numero_maximo_keep_off = 3;
    static long tempo_validacao_keep_alive = 50000;

    /*
     * Na instaciacao carrega as configuracoes e os contatos
    */           
    public Servidor(frm_Servidor f) {
        form = f;
        clienteBD = new ClienteBD();
        try {
            carregarConfiguracoes();
            carregarContatos();
        } catch (Exception e) {
            System.err.println("gravando erro public Servidor(frm_Servidor f)... ");
        }
    }

    /*
     * Carre lista de clientes e lista de contatos dos clientes em memoria
    */       
    public void carregarContatos() {
        String sqlClientes = "SELECT * FROM CLIENTE";
        String sqlClienteContatos = "SELECT * FROM LISTA_CONTATOS WHERE USER_NAME = '";
        Cliente novoCliente;
        List<String> aux;
        try {
            ResultSet rs = clienteBD.getResultSet(sqlClientes);
            while (rs.next()) {
                String nome = rs.getString("USER_NAME").toString();
                boolean status;
                if (rs.getString("STATUS").toString().equals("1") || rs.getString("STATUS").toString().equalsIgnoreCase("true")) {
                    status = true;
                } else {
                    status = false;
                }
                String novo_ip = rs.getString("IP").toString();
                Integer nova_porta = Integer.parseInt(rs.getString("PORTA"));
                novoCliente = new Cliente(nome, status, novo_ip, nova_porta);
                clientes.put(nome, novoCliente);

                ResultSet rs2 = clienteBD.getResultSet(sqlClienteContatos + nome + "';");
                aux = new ArrayList<String>();
                while (rs2.next()) {
                    aux.add(rs2.getString("USER_NAME_CONTATO").toString());
                }
                clientesContatos.put(nome, aux);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Atualiza status de um usuario na base
     * Priorizar utilizacao dos contatos na memoria
    */           
    @Deprecated
    public void updateStatusUsuario(String userName, String status) {
        String sql = "UPDATE CLIENTE"
                + " SET STATUS = " + status
                + " WHERE USER_NAME = '" + userName + "'";
        clienteBD.executeUpdate(sql);
        clienteBD.closeConnection();
    }

    /*
     * Popula dados da tabela configuracoes
     * numero_maximo_keep_off e tempo_validacao_keep_alive
    */                
    public void carregarConfiguracoes() {
        String sql = "SELECT * FROM ep_redes.configuracoes where id = 1;";
        ResultSet rs = clienteBD.getResultSet(sql);
        try {
            while (rs.next()) {
                tempo_validacao_keep_alive = Long.parseLong(rs.getString("tempo_validacao_keep_alive"));
                numero_maximo_keep_off = Integer.parseInt(rs.getString("numero_maximo_keep_off"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    * Metodo central da classe Servidor, recebe novas conexoes e as trata
    * Uma requisicao pode ser: Login (devolve contatos e tempo de espera do keepAlive), Logoff, KeepALive
    */                
    public void novasConexoes() throws IOException {
        Runnable r= new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int contadorExececoes = 0;
		        try {
		            s_socket = new ServerSocket(porta);
		            System.out.println("Socket listening porta: " + porta);
		            Socket c_socket;
		            DataOutputStream output;
		            // Incia validacao de keep alive
		            startValidaKeepAlive();
		            while (true) {
		                // Captura todas as novas requisicoes 
		            	
		                c_socket = s_socket.accept();
		                System.out.println("Nova conexao com o cliente " + c_socket.getInetAddress().getHostAddress());

		                try {
		                    //Realiza leitura do usuario
		                    DataInputStream input = new DataInputStream(c_socket.getInputStream());
		                    String mensagem = input.readUTF().toString();
		                    System.out.println(mensagem);
		                    List<String> mensagemCodificada = Arrays.asList(mensagem.split("<.>"));

		                    if (mensagemCodificada.contains("logou")) {
		                        if (clientes.containsKey(mensagemCodificada.get(1).trim().toString())) {
		                            System.out.println("Testando: logou");                                
		                            String result = getContatosMemoria(mensagemCodificada.get(1));
		                            if(!result.equals("")){
		                                result = result.substring(0, result.length() - 1);
		                                result = result.concat("/keepAlive:" + String.valueOf(tempo_validacao_keep_alive) + "_");                                
		                            }
		                            //updateStatusUsuario(mensagemCodificada.get(1), "true");
		                            Cliente aux = clientes.get(mensagemCodificada.get(1));
		                            aux.status = true;
		                            clientes.put(mensagemCodificada.get(1), aux);
		                            // Avisa Contatos que o cara ta online
		                            mensagemContato = "acorda";
		                            nomeContato = mensagemCodificada.get(1).trim().toString();
		                            Runnable r = new Runnable() {
		                                public void run() {
		                                    try {
		                                        avisarContatos();
		                                    } catch (Exception e) {
		                                        System.err.println("Erro ao avisar contatos: " + e.toString());
		                                    }
		                                }
		                            };
		                            new Thread(r).start();
		                            output = new DataOutputStream(c_socket.getOutputStream());
		                            output.writeUTF(result);
		                        } else {
		                            output = new DataOutputStream(c_socket.getOutputStream());
		                            output.writeUTF("Usuario nao existe");
		                            System.err.println("Usuario nao existe");
		                        }
		                    }

		                    if (mensagemCodificada.contains("keepAlive")) {
		                        System.out.println("Testando: keepAlive");
		                        updateKeepAliveUser(mensagemCodificada.get(1), 0);
		                    }

		                    if (mensagemCodificada.contains("tofechando")) {
		                        System.out.println("Testando: fechou");
		                        updateStatusUsuario(mensagemCodificada.get(1), "false");
		                        Cliente aux = clientes.get(mensagemCodificada.get(1));
		                        aux.status = false;
		                        clientes.put(mensagemCodificada.get(1), aux);
		                        mensagemContato = "dorme";
		                        nomeContato = mensagemCodificada.get(1).trim().toString();
		                        Runnable r = new Runnable() {
		                            public void run() {
		                                try {
		                                    avisarContatos();
		                                } catch (Exception e) {
		                                    System.err.println("Erro ao avisar contatos: " + e.toString());
		                                }
		                            }
		                        };
		                        new Thread(r).start();
		                    }
		                } catch (Exception e) {
		                    contadorExececoes++;
		                    if(contadorExececoes < 3){
		                        System.err.println("Erro, tentando novamente, contdor" + contadorExececoes);
		                        continue;
		                    }else{
		                        break;
		                    }                    
		                }
		                contadorExececoes = 0;                    
		            }
		        } catch (Exception e) {
		            System.out.println("erro: " + e.getMessage());
		            System.out.println("erro: " + e);
		            if(e.getMessage().equals("Address already in use: JVM_Bind")){
		                return;
		            }
		        }
				
				
			}
        	
        };
    	
    new Thread(r).start();

    }

    /*
    * Envia mensagem para o cliente informando mudanca de status do contato do cara
    * Envia "acorda<.>Contato" para avisar contato online e "dorme<.>Contato" contato offline
    */           
    private void avisarContatos() throws IOException {
        DataOutputStream output;
        for (String item : clientesContatos.get(nomeContato)) {
            Cliente cliente = clientes.get(item);
            if (cliente.status == true) {
                Socket cServer_socket = estabelecerComunicacao(cliente.ip, cliente.porta);
                output = new DataOutputStream(cServer_socket.getOutputStream());
                output.writeUTF(mensagemContato + "<.>" + nomeContato);
                cServer_socket.close();
            }
        }
    }

    /*
    * Retorna string formadata no padrao do cliente com a lista de contatos dele
     * 
    */            
    private String getContatosMemoria(String nomeCont) {
        String result = "";
        for (String item : clientesContatos.get(nomeCont)) {
            Cliente cliente = clientes.get(item);
            result = result.concat(result + String.valueOf(cliente.userName)
                    + "/" + String.valueOf(cliente.status ? "1" : "0")
                    + "/" + String.valueOf(cliente.ip) + "/" + String.valueOf(cliente.porta) + "_");
        }
        return result;
    }

    /*
    * Inicia a thread responsavel por de tempos em tempos (configuravel) validar se o cliente esta alive
    */            
    private void startValidaKeepAlive() throws IOException, InterruptedException {

        Runnable r = new Runnable() {

            public void run() {
                try {
                    while (true) {
                        Thread.sleep(tempo_validacao_keep_alive);
                        validaKeepAlive();
                        System.out.println("Keep alive validado!");
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao start thread validaKeepAlive: " + e.toString());
                }
            }
        };
        new Thread(r).start();
    }

    /*
    * Valida cada cliente online para ver se o keep alive esta na quantidade aceitavel
    */            
    private void validaKeepAlive() throws IOException {
        for (Entry<String, Cliente> item : clientes.entrySet()) {
            Cliente cliente = item.getValue();
            if (cliente.status == true) {
                if (cliente.contador_keep_alive > numero_maximo_keep_off) {
                    mensagemContato = "acorda";
                    nomeContato = cliente.userName;
                    Runnable r = new Runnable() {

                        public void run() {
                            try {
                                avisarContatos();
                            } catch (Exception e) {
                                System.err.println("Erro ao abrir thread enviar validaKeepAlive: " + e.toString());
                            }
                        }
                    };
                    new Thread(r).start();
                    cliente.status = false;
                    cliente.contador_keep_alive = 0;
                } else {
                    cliente.contador_keep_alive++;
                }
                clientes.put(cliente.userName, cliente);
            }
        }
    }

    /*
    * Atualiza a quantidade de keepAlive sem responder do cliente
    */        
    private boolean updateKeepAliveUser(String userNameKeep, int cont) {
        try {
            if (clientes.containsKey(userNameKeep)) {
                Cliente aux = clientes.get(userNameKeep);
                aux.contador_keep_alive = cont;
                clientes.put(userNameKeep, aux);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("erro em updateKeepAliveUser: " + e);
            return false;
        }
    }

    /*
    * Cria uma comunicacao simples e retorna um socket
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
}
