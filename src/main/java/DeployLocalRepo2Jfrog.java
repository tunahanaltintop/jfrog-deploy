import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeployLocalRepo2Jfrog {

    private static String PATH = "/Users/youruser/.m2/repository-jfrog-deploy/";

    private static ProcessBuilder process = new ProcessBuilder();

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        deployFiles(PATH);
    }

    private static void deployFiles(String PATH) throws TransformerException, IOException, ParserConfigurationException, SAXException {
        System.out.println("\nStarted.");

        //Visit all files
        System.out.println("\nDeploying...");
        deployFiles(new File(PATH));

        System.out.println("\nFinished.");
    }


    private static void deployFiles(File file) throws IOException {
        File[] allContents = file.listFiles();
        if (allContents != null) {
            for (File fileContent : allContents) {
                deployFiles(fileContent);
            }
        }

        if (!file.isDirectory() && !file.getName().startsWith("_")) {
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getName());

            String[] commandStr = {"/usr/bin/curl", "-X", "PUT", "-u", "username:password", "-T", file.getAbsolutePath(),
                    "http://your.artifactory/artifactory/libs-release-local/" + file.getAbsolutePath().replace(PATH, "")};
            process = process.command(commandStr);
            Process p;
            try {
                p = process.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.getProperty("line.separator"));
                }
                String result = builder.toString();
                System.out.print(result);

                p.destroy();

            } catch (IOException e) {
                System.out.print("error");
                e.printStackTrace();
            }

        }
    }
}
