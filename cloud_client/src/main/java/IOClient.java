import java.io.*;
import java.net.Socket;

public class IOClient {

    public static void createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if(!file.exists()) {
            file.createNewFile();
        }
    }

    public static void createDirectory(String dirName) throws IOException {
        File file = new File(dirName);
        if(!file.exists()) {
            file.mkdir();
        }
    }

    public static void moveFile(File dir, File file) throws IOException {
        String path = dir.getAbsolutePath() + "\\" + file.getName();
        createFile(path);
        InputStream is = new FileInputStream(file);
        try(OutputStream os = new FileOutputStream(new File(path))) {
            byte [] buffer = new byte[8192];
            while (is.available() > 0) {
                int readBytes = is.read(buffer);
                System.out.println(readBytes);
                os.write(buffer, 0, readBytes);
                //os.write(is.read());
            }
        }
        //os.close();

    }

    public static void sendFile(Socket socket, File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long size = file.length();
        int count = (int) (size/8192)/20, readBuckets = 0;

        try(DataOutputStream os = new DataOutputStream(socket.getOutputStream())) {
            byte [] buffer = new byte[8192];
            os.writeUTF(file.getName());
            System.out.print("| ");
            while (is.available() > 0) {
                int readBytes = is.read(buffer);
                readBuckets++;
//                if (readBuckets % count == 0){//отключил анимацию загрузки
//                    System.out.print("# ");
//                }

                os.write(buffer, 0, readBytes);
                //os.write(is.read());
            }
            System.out.println("|");
            System.out.println("Файл отправлен на сервер.");
        }
        //os.close();

    }
    
    public static void loadFile(Socket socket, File file) throws IOException {
        DataInputStream iss = new DataInputStream(socket.getInputStream()); //взяли имя файла
        //oss = new DataOutputStream(socket.getOutputStream()); //взяли содержимое файла
        //String fileName = is.readUTF();

        //System.out.println("fileName: " + fileName);
        //File file = new File ("./cloud_server/filesServer/" + fileName); //создали файл с полученным от клиента именем
        //file.createNewFile();
        try (FileOutputStream oss = new FileOutputStream(file)){ //вписали из сокета в файл
            byte [] buffer = new byte [8192];
            while (true){
                int r = iss.read(buffer);
                if (r == -1) break;
                oss.write(buffer, 0, r);
            }
        }
        System.out.println("Файл загружен с сервера на клиента");
    }

    public static void main(String[] args) throws IOException{
        //createFile("./common/1.txt"); //данная запись сохраняет в root проекта не путать с \\
        //createDirectory("./common/dir1");
        //moveFile(new File("./common/dir1"), new File("./common/1.txt"));

        //отправить файл на сервер
        //sendFile(new Socket("localhost", 8189), new File ("./cloud_client/filesClient/123.txt"));

        //получить файл с сервера
        loadFile(new Socket("localhost", 8189), new File ("./cloud_client/filesClient/456.txt"));
    }
}