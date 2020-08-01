import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;



public class IOServer {

    DataInputStream is;
    DataOutputStream os;
    ServerSocket server;

    public IOServer() throws IOException {
        server = new ServerSocket(8189); //запуск сервера на на порту 8189
        Socket socket = server.accept(); //проверка клиента

        System.out.println("Client accepted!"); //подтверждение что клиент поднят

        /*is = new DataInputStream(socket.getInputStream()); //взяли имя файла
        os = new DataOutputStream(socket.getOutputStream()); //взяли содержимое файла
        String fileName = is.readUTF();

        System.out.println("fileName: " + fileName);
        File file = new File ("./cloud_server/filesServer/" + fileName); //создали файл с полученным от клиента именем
        file.createNewFile();
        try (FileOutputStream os = new FileOutputStream(file)){ //вписали из сокета в файл
            byte [] buffer = new byte [8192];
            while (true){
                int r = is.read(buffer);
                if (r == -1) break;
                os.write(buffer, 0, r);
            }
        }
        System.out.println("File uploaded!");*/
        InputStream iss = new FileInputStream("./cloud_server/filesServer/456.txt");
        try(DataOutputStream oss = new DataOutputStream(socket.getOutputStream())){
            byte [] buffer = new byte[8192];
            //oss.writeUTF(file.getName());
            while (iss.available() > 0) {
                int readBytes = iss.read(buffer);
                oss.write(buffer, 0, readBytes);
            }
        }


            System.out.println("Файл отправлен на клиент.");


    }

    public static void main(String[] args) throws IOException{
        new IOServer();
    }
}