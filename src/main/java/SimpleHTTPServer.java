import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * @Author Binod Shrestha <binodshrestha@lftechnology.com>
 * Created on 1/21/16
 */
public class SimpleHTTPServer {
  public static void main(String[] args) throws IOException {

    ServerSocket server = new ServerSocket(8080);
    System.out.println("listening for connection on port 8080");
    while (true) {

      try (Socket socket = server.accept()) {
        Date today = new Date();
        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
        socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        PrintStream output = new PrintStream(socket.getOutputStream());
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html");
        output.println("\r\n");
//        output.println("<html><head> <head/> <body> <p> Hello world </p> </body> </html>");
//        output.flush();
        File[] files = getAllFiles(new File("."));

        for(File file : files){
          output.println(file.getName());
        }

      }
    }

  }

  private static File[] getAllFiles(File curDir){
    return curDir.listFiles();
  }

}
