import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.zip.*;

public class Main {
    public static void main(String[] args) {

        GameProgress game1= new GameProgress(100,2,1,5.0);
        GameProgress game2= new GameProgress(92,3,52,15.0);
        GameProgress game3= new GameProgress(23,1,56,6.4);

        savegame(game1,1);
        savegame(game2,2);
        savegame(game3,3);

        zipSave("D://netologi//Games//savegames//save1.dat",
                "D://netologi//Games//savegames//save2.dat",
                "D://netologi//Games//savegames//save3.dat");

        deleteSave("D://netologi//Games//savegames");
        }
    public static void zipSave(String ... files) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream("D://netologi//Games//savegames//Saves.zip"))) {
            for (String file : files) {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(file.substring(32));
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void savegame(GameProgress game, int numberSave){
        try (FileOutputStream fos = new FileOutputStream("D://netologi//Games//savegames//save"+numberSave+".dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(game);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteSave(String path){
        File dir = new File(path);
        if (dir.isDirectory()){
            for (File item : dir.listFiles()){
                if (item.isFile()){
                    if (item.getName().contains(".zip")){
                        continue;
                    }
                    else {
                        item.delete();
                    }
                }
            }
        }
    }
}
