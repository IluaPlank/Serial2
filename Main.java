import java.io.*;
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


        zipUnpack("D://netologi//Games//savegames//Saves.zip","D://netologi//Games//savegames");

        saveUnpack("D://netologi//Games//savegames//save1.dat",
                "D://netologi//Games//savegames//save2.dat",
                "D://netologi//Games//savegames//save3.dat");
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

    public static void zipUnpack(String pathZip, String pathSave) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                // получим название файла
                // распаковка
                FileOutputStream fout = new FileOutputStream(pathSave+"//"+name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void saveUnpack(String ... files) {
        GameProgress gameProgress = null;
        for (int i=1;i <= files.length;i++) {
            // откроем входной поток для чтения файла
            try (FileInputStream fis = new FileInputStream("D://netologi//Games//savegames//save" + i + ".dat");
                 ObjectInputStream ois = new ObjectInputStream(fis))
            {
                // десериализуем объект и скастим его в класс
                gameProgress = (GameProgress) ois.readObject();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(gameProgress);
        }
    }
}
