import java.io.*;
import java.net.*;

/**
 * @Author Binod Shrestha <binodshrestha@lftechnology.com>
 * Created on 1/21/16
 */
public class SimpleHTTPServer {

  private static final String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "Content-Length: ";
  private static final String OUTPUT_END_OF_HEADERS = "\r\n\r\n";
  private static String basePath = null;

  public static void main(String[] args) throws IOException {

    if(args.length > 0){
      basePath = args[0];
      System.out.println("base path: "+basePath);
    }

    ServerSocket server = new ServerSocket(8080);
    System.out.println("listening for connection on port 8080");
    while (true) {

      try (Socket socket = server.accept()) {

        if(basePath == null){
          basePath = System.getProperty("user.dir");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String line = br.readLine();

        String urlString = line.split(" ")[1];
        urlString = urlString.split("\\?")[0];
        urlString = urlString.replace("%20"," ");

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()), "UTF-8"));

        String fullPath = basePath+urlString;
        File testFile = new File(fullPath);


        if(testFile.isFile()){
          InputStream is = new FileInputStream(testFile);
          OutputStream os = socket.getOutputStream();
          byte[] bytes = new byte[(int) testFile.length()];
          int count;
          while((count = is.read(bytes)) > 0){
            os.write(bytes,0,count);
          }
          os.close();
          is.close();

        }else{
          File[] files = getAllFiles(new File(fullPath));
          String fileName = "";
          System.out.println("------------------------------------------------------------");
          fileName = fileName.concat("<ul>");
          for(File file : files){
            String lastChar = urlString.substring(urlString.length()-1);
            if(!lastChar.equals("/")){
              urlString = urlString.concat("/");
            }
            System.out.println("files: " + file.getName());
            fileName = fileName.concat("<li><a href='"+urlString+file.getName()+"'>"+file.getName()+"</a></li>");
          }
          fileName.concat("</ul>");
          System.out.println("------------------------------------------------------------");

          final String OUTPUT = "<html><head><title>Server</title></head><body><h2>Directory list</h2>"+fileName+"</body></html>";
          output.write(OUTPUT_HEADERS + OUTPUT.length() + OUTPUT_END_OF_HEADERS + OUTPUT);
        }


        output.flush();
        output.close();


      }catch (NullPointerException e){}
      catch (SocketException e){}
    }

  }

  private static File[] getAllFiles(File curDir){
    if(curDir.isFile()){
      return new File[] {curDir};
    }else{
      return curDir.listFiles();
    }

  }

}
