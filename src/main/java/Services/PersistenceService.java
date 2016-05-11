package Services;

import Model.PersistenceMessage;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class PersistenceService {

    static final String PERSISTENCE_FILE_PATH = ".\\src\\main\\resources\\persistence.file";
    static Gson _gson = new Gson();

    static PersistenceMessage _persistance;
    public static PersistenceMessage ReadFromPersistanceFile() {

        if(_persistance != null ){
            return _persistance;
        }
        try {
            String currentDir = GetCurrentDirPath();
            File file = new File(currentDir, PERSISTENCE_FILE_PATH);

            if (file.exists() && file.canRead()) {
                Path path = FileSystems.getDefault().getPath(currentDir, PERSISTENCE_FILE_PATH);
                String fileContent = null;
                fileContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

                _persistance = _gson.fromJson(fileContent, PersistenceMessage.class);
            }

        } catch (IOException ex) {
            //...
        }

        return _persistance;
    }

    public static boolean WriteToPersistanceFile(PersistenceMessage message) {

        boolean result = false;
        try {
            String currentDir = GetCurrentDirPath();
            File file = new File(currentDir, PERSISTENCE_FILE_PATH);

            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
            writer.print(_gson.toJson(message));
            writer.close();


            result = true;
        } catch (IOException io) {
            //...
        }finally {
            _persistance = message;
        }
        return result;
    }

    public static String GetCurrentDirPath() {
        String currentDir = null;

        File currentDirFile = new File(".");
        currentDir = currentDirFile.getAbsolutePath();


        return currentDir;
    }
}
