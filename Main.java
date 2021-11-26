import java.io.*;
import java.util.zip.*;

public class Main {
    final static String SAVE1 = "D://netologi//Games//savegames//save1.dat";
    final static String SAVE2 = "D://netologi//Games//savegames//save2.dat";
    final static String SAVE3 = "D://netologi//Games//savegames//save3.dat";
    final static String SAVE_GAMES = "D://netologi//Games//savegames";
    final static String SAVE_GAMES_ZIP = "D://netologi//Games//savegames";


    public static void main(String[] args) {
        GameProgress game1 = new GameProgress(100, 2, 1, 5.0);
        GameProgress game2 = new GameProgress(92, 3, 52, 15.0);
        GameProgress game3 = new GameProgress(23, 1, 56, 6.4);

        saveGame(game1, 1);
        saveGame(game2, 2);
        saveGame(game3, 3);


        zipSave(SAVE1, SAVE2, SAVE3);

        deleteSave(SAVE_GAMES);


        zipUnpack(SAVE_GAMES_ZIP, SAVE_GAMES);

        saveUnpack(SAVE1, SAVE2, SAVE3);
    }

    public static void saveGame(GameProgress game, int numberSave) {
        try (FileOutputStream fos = new FileOutputStream("D://netologi//Games//savegames//save" + numberSave + ".dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipSave(String... files) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(SAVE_GAMES_ZIP))) {
            for (String file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.substring(32));
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteSave(String path) {
        File save = new File(path);
        if (save.isDirectory()) {
            for (File item : save.listFiles()) {
                if (item.isFile()) {
                    if (item.getName().contains(".zip")) {
                        continue;
                    } else {
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
                FileOutputStream fout = new FileOutputStream(pathSave + "//" + name);
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

    public static void saveUnpack(String... files) {
        GameProgress gameProgress = null;
        for (int i = 1; i <= files.length; i++) {
            // откроем входной поток для чтения файла
            try (FileInputStream fis = new FileInputStream("D://netologi//Games//savegames//save" + i + ".dat");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                // десериализуем объект и скастим его в класс
                gameProgress = (GameProgress) ois.readObject();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(gameProgress);
        }
    }
}
