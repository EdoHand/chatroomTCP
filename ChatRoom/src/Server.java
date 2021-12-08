import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;

import static java.lang.System.in;
import static java.lang.System.out;

public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections;

    @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(4747);
            Socket client = serverSocket.accept();
            ConnectionHandler connectionHandler = new ConnectionHandler(client);
            connections.add(connectionHandler);
        }catch (IOException e){

        }
    }
    public void broadcast(String message){
        for (ConnectionHandler ch : connections){
            if (ch != null){
                ch.SendMessage(message);
            }
        }
    }

    class ConnectionHandler implements Runnable {
        private Socket client ;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private String clientNickName;

        public ConnectionHandler(Socket client){
            this.client = client;
        }

        @Override
        public void run() {
            try {
                dataOutputStream = new DataOutputStream(client.getOutputStream());
                dataInputStream = new DataInputStream(client.getInputStream());
                out.println("enter nickname : ");
                clientNickName = String.valueOf(in.read());
                System.out.println(clientNickName + "Connected");
                broadcast(clientNickName + "Joined the room");
                String message;
                while((message = String.valueOf(in.read())) != null) {
                    if (message.startsWith("/Nickname")){
                        String[] messageSplit = message.split("", 2);
                        if (messageSplit.length == 2){
                            broadcast(clientNickName + "rename to" + messageSplit[1] );
                            System.out.println(clientNickName + "rename to" + messageSplit[1]);
                            clientNickName = messageSplit[1];
                            System.out.println("Success changed Nickname " + clientNickName);
                        }
                    }else if (message.startsWith("/Quit")){

                    }else{
                        broadcast(clientNickName + ":" + message);
                    }
                }
            }catch (IOException e){

            }
        }

        public void SendMessage(String message){
            System.out.println(message);
        }
    }


}
