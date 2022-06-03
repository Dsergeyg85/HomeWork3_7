import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GameProgress implements Serializable {
    private static final long serialVersionUID = 1L;

    private int health;
    private int weapons;
    private int lvl;
    private double distance;

    public GameProgress(int health, int weapons, int lvl, double distance) {
        this.health = health;
        this.weapons = weapons;
        this.lvl = lvl;
        this.distance = distance;
    }

    public static void saveGame (String filePath, GameProgress gameProgress) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gameProgress);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String fileZipPath, List<String> list) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(fileZipPath))) {
            for (String filePath : list) {
                try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                    ZipEntry zipEntry = new ZipEntry(filePath);
                    zipOutputStream.putNextEntry(zipEntry);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    zipOutputStream.write(buffer);
                    zipOutputStream.closeEntry();
                    File file = new File(filePath);
                    file.delete();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String fileZipPath, String filePath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileZipPath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String name = entry.getName();
                FileOutputStream fileOutputStream = new FileOutputStream(name);
                    int i;
                    for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                        fileOutputStream.write(c);
                    }
                    fileOutputStream.flush();
                    zipInputStream.closeEntry();
                    fileOutputStream.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static GameProgress openProgress(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (GameProgress) objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "GameProgress{" +
                "health=" + health +
                ", weapons=" + weapons +
                ", lvl=" + lvl +
                ", distance=" + distance +
                '}';
    }
}