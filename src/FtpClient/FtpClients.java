package FtpClient;

import java.io.FileOutputStream;

//librerías de apache para FTP

//librerías de java
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * La clase FtpClient tiene un metodo main permite estabalecer conexion con el servidor ftp
 *  descargar fichieros usando la clase FTPClient
 *
 **/
public class FtpClients {

	private static FTPClient ftpClient;

	private static FileOutputStream fos;
	private static String servidorIP = "192.168.0.14";

	private static String rootPath = "/srv/ftp/";
	private static String nameFile = "";

	private static String userName = "alumno";
	private static String passWord = "alumno";

	private static FTPFile[] dirsName;

	private static FTPFile[] filesName;

	/**
	 * *primero instanciamos un objeto de la clase fTPClient llamando al metodo connect pasandole la dirrecion ip o host comrobamos la conexion con servidor
	 * ftp mediante codigo de respuesta que nos devuelve el metodo getReplyCode();  de la clase FTPclient  usando metodo isPositiveCompletion(reply code) pasandole 
	 * reply code y ha sido positiva abrimos una session con el metodo login(user,password) pasandole usario y contrasena que es metodo booleano devuelve true si se ha abierto la session
	 * una vez abierta la session podemos acceder a los fichieros del servidor en este caso usamos el metodo changeWorkingDirectory() para posicionarnos el la carptea root del sercidor ftp
	 * que es /srv/ftp/ una vez usamos metodo listFiles() y listDirectories() que nos devuelve una lista de tipo FTPFile recorremos las listas mostramos los fichieros  y directorios disponible en root path
	 * luego se crear un objeto Scanner para recibir nombre del fichiero que se quiere descargar instanciando objeto de escritura y llamando al metodo retrieveFile() de tipo boolean que recibe el nombre del fichiero y
	 * flujo para descargar fichiero return true si la operacion ha finalizado con exito 
	 * @param args String
	 */
	public static void main(String[] args) {
		int reply;
		boolean connStatus;

		ftpClient = new FTPClient();
		try {
			/**
			 * metodo connect estableze la conexion del cliente con el servidor
			 */
			ftpClient.connect(servidorIP);

			/**
			 * getReplyCode obetiene el reply code o codigo de respuesta de tipo int
			 */
			reply = ftpClient.getReplyCode();

			System.out.println(reply);
			/**
			 * se comprueba si la conexion es satisfactoria es positiva
			 */

			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.println("Error server response : " + reply);
				ftpClient.disconnect();
			}

			/**
			 * metodo login abre una sesion con el servidor pasando nombre de usario y la
			 * contraseña ftp.login(user,password)
			 */
			connStatus = ftpClient.login(userName, passWord);

			if (connStatus == true) {
				System.out.println("***A connection was successfully established with the  ftp server***");

				/**
				 * El metodo changeWorkingDirectorychange permite de cambiar la ruta en este
				 * caso root del servidor ftp
				 */
				ftpClient.changeWorkingDirectory(rootPath);

				 
				System.out.println("\n**Carpetas disponibles en el Servidor:**");
				dirsName = ftpClient.listDirectories();
				
				for (FTPFile dir : dirsName) {
					System.out.println(dir);
				}

				/**
				 * el metodo listFiles() devulve una lista de tipo FTPFile que son los fichieros
				 * de la ruta root del servidor ftp
				 */

				filesName = ftpClient.listFiles();
				 
				System.out.println("\n**Fichieros disponibles en el Servidor:**");
				for (FTPFile file : filesName) {
					if (file.getType() == FTPFile.FILE_TYPE) {
						System.out.println(file.getName());
					}
				}

				Scanner s = new Scanner(System.in);
				System.out.println("\n***Descarga de fichieros****");

				System.out.print("\nIntroduce el nombre del fichiero :");
				String downFileName = s.nextLine();

				/**
				 * se instancia objeto fos que flujo de escritura con mismo nombre del fichiero
				 * que se desea descargar
				 */
				fos = new FileOutputStream("./descargas/"+downFileName);

				System.out.println("\nDescargando el fichiero : " + downFileName + " de la carpta " + rootPath);

				/**
				 * retrieveFile() metodo boleano permite descargar fichieros recibe 2 param
				 * nombre de fichiero , flujo de escritura FileOutPutStream true si se ha
				 * descargado fichiero correctamente false si ha ocurrido algun error
				 */
				if (ftpClient.retrieveFile(downFileName, fos))
					System.out.println("Descarga Finalizada Correctamnte");

				else
					System.out.println("Error de descarga");

				fos.close();
				ftpClient.disconnect();

			} else {
				System.out.println("Error connection to the server, please check your username and password ");
			}

		} catch (SocketException e) {
			System.out.println("Error Socket" + e.getLocalizedMessage());

		} catch (IOException e) {

			System.out.println("Error File " + e.getLocalizedMessage());
		}

	}

}
