package Models;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WorkInFiles {
    private static String pathDirectory = "C:\\Library";
    private static String pathUsersFile = pathDirectory + "\\users.txt";

    private static String diaryTitleList = "diaryTitleList.txt";

    public static void CreateDirectory(String uri){
        Path path = Paths.get(uri);
        if (!Files.exists(path))
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    private static String IsArchiving(Diary d){
        String title = d.getTitle();
        if (d.getArchive())
            title += "&" + d.getArchive();
        return title;
    }
    public static void WriteUser(User user) throws IOException {
        List<String> titles = new ArrayList<>();
        for (Diary diary: user.diaries) {
            CreateDirectory(pathDirectory + "\\" + user.NickName);
            CreateDirectory(pathDirectory + "\\" + user.NickName + "\\" + diary.getTitle());
            File file = new File(pathDirectory + "\\" + user.NickName + "\\"
                    + diary.getTitle() + "\\" + diary.getTitle() + ".txt");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(diary.getContent());
            writer.close();
            titles.add(IsArchiving(diary));
        }
        WriteDiaryTitleList(user.NickName, titles);
    }
    public static void WriteDiaryTitleList(String nickName, List<String> titles) throws IOException {
        FileWriter writer = new FileWriter(pathDirectory + "\\" + nickName + "\\" + diaryTitleList);
            for(String title: titles)
                writer.write(title + "\n");
        writer.close();
    }

    public static List<String> ReadDiaryTitleList(String nickName) throws IOException {
        File file = new File(pathDirectory + "\\" + nickName + "\\" + diaryTitleList);
        if(!file.exists())
            return new ArrayList<>();
        FileReader read = new FileReader(file);
        Scanner scanner = new Scanner(read);
        List<String> result = new ArrayList<>();
        while (scanner.hasNextLine())
            result.add(scanner.nextLine());
        read.close();
        return result;
    }

    public static void WriteUsers(List<User> content) throws IOException {
        File file = new File(pathUsersFile);
        file.createNewFile();
        List<User> users = ReadUsers();
        try(FileWriter writer = new FileWriter(file, true)) {
            for (User user : content)
                if (!User.MyConteins(users, user))
                    writer.write(user.NickName + "&" + user.Email + "&" + user.Password+"\n");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static List<User> ReadUsers() throws IOException {
        FileReader read = new FileReader(pathUsersFile);
        Scanner scanner = new Scanner(read);
        List<User> result = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String[] buffer = scanner.nextLine().split("&");
            result.add(new User(buffer[0],buffer[1],buffer[2]));
        }
        read.close();
        return result;
    }

    private static Diary IsArchive(String title){
        String[] str = title.split("&");
        Diary d;
        if (str.length == 2) {
            d = new Diary(str[0], "");
            if (str[1].equals("true"))
            d.setArchive(true);
        }
        else
            d  = new Diary(title, "");
        return d;
    }

    public static List<Diary> ReadDiaries(String nickName) throws IOException {
        List<String> titles = ReadDiaryTitleList(nickName);
        List<Diary> result = new ArrayList<>();
        for(String title: titles) {
            Diary d = IsArchive(title);
            File file = new File(pathDirectory + "\\" + nickName + "\\" + d.getTitle()
                    + "\\" + d.getTitle() + ".txt");
            if(file.exists()) {
                FileReader read = new FileReader(file);
                Scanner scanner = new Scanner(read);
                while (scanner.hasNextLine())
                    d.setContent(d.getContent() + scanner.nextLine() + "\n");
                read.close();
                result.add(d);
            }
        }
        return result;
    }
    public static void DeleteDiary(String nickName, String title) throws IOException {
        RecursiveDelete(new File(pathDirectory + "\\" + nickName + "\\" + title));
        List<String> titles = ReadDiaryTitleList(nickName);
        titles.remove(title);
        RecursiveDelete(new File(pathDirectory + "\\" + nickName + "\\" + diaryTitleList));
        WriteDiaryTitleList(nickName, titles);
        if (titles.size() == 0)
            RecursiveDelete(new File(pathDirectory + "\\" + nickName));

    }

    private static void RecursiveDelete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory())
            for (File f : file.listFiles())
                RecursiveDelete(f);
        file.delete();
    }

    public static void TransferFile(File file, String nickName, String title) throws IOException {
        File dest = new File(pathDirectory + "\\" + nickName + "\\" + title);
        //copyFileUsingStream(file, dest);
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
