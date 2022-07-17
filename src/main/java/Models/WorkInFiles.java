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
    private static String IsArchivingOrCreator(Diary d){
        String title = d.getTitle();
        title += "&" + d.getArchive();
        if(!d.getCreator().equals(""))
            title += "&" + d.getCreator();
        return title;
    }
    public static void WriteUser(User user) throws IOException {
        List<String> titles = new ArrayList<>();
        for (Diary diary: user.diaries) {
            String pathU = diary.getCreator().equals("") ? user.NickName : diary.getCreator();
            CreateDirectory(pathDirectory + "\\" + pathU);
            CreateDirectory(pathDirectory + "\\" + pathU + "\\" + diary.getTitle());
            File file = new File(pathDirectory + "\\" + pathU + "\\"
                    + diary.getTitle() + "\\" + diary.getTitle() + ".txt");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(diary.getContent());
            writer.close();
            titles.add(IsArchivingOrCreator(diary));
        }
        WriteDiaryTitleList(user.NickName, titles);
    }
    public static void WriteDiaryTitleList(String nickName, List<String> titles) throws IOException {
        File file = new File(pathDirectory + "\\" + nickName);
        if (!file.exists())
                CreateDirectory(pathDirectory + "\\" + nickName);
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

    private static Diary IsArchiveOrCreator(String title){
        String[] strs = title.split("&");
        Diary d;
        if (strs.length >= 2) {
            d = new Diary(strs[0], "");
            if (strs[1].equals("true"))
                d.setArchive(true);
            if (strs.length == 3)
                d.setCreator(strs[2]);
        }
        else
            d  = new Diary(title, "");
        return d;
    }

    public static List<Diary> ReadDiaries(String nickName) throws IOException {
        List<String> titles = ReadDiaryTitleList(nickName);
        List<Diary> result = new ArrayList<>();
        for(String title: titles) {
            Diary d = IsArchiveOrCreator(title);
            File file = new File(pathDirectory + "\\" + (d.getCreator().equals("") ? nickName : d.getCreator())
                    + "\\" + d.getTitle() + "\\" + d.getTitle() + ".txt");
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
        CleaningList(nickName, title);
    }
    public static void DeleteDiary(String nickName, String title, String creator) throws IOException {
        RecursiveDelete(new File(pathDirectory + "\\" + creator + "\\" + title));
        CleaningList(creator, title);
        CleaningList(nickName, title);
    }
    private static void CleaningList(String nickName, String title) throws IOException {
        List<String> titles = ReadDiaryTitleList(nickName);
        String buffer = "";
        for(String item: titles)
            if (item.split("&")[0].equals(title))
                buffer = item;
        titles.remove(buffer);
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
            while ((length = is.read(buffer)) > 0)
                os.write(buffer, 0, length);
        } finally {
            is.close();
            os.close();
        }
    }
}
