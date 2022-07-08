package Models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    public String NickName;
    public String Email;
    public String Password;

    public List<Diary> diaries;

    public User(String nickName,String email,String password){
        NickName = nickName;
        Email = email;
        Password = password;
    }

    public Diary AddDiary(String title, String content) throws IOException {
        if (diaries == null)
            diaries = new ArrayList<>();
        for (Diary d : diaries)
            if (d.getTitle().equals(title))
                return null;
        Diary diary = new Diary(title, content);
        diaries.add(diary);
        WorkInFiles.WriteUser(this);
        return diary;
    }

    public Diary AddDiary(Diary diary) throws IOException {
        if (diaries == null)
            diaries = new ArrayList<>();
        for (Diary d : diaries)
            if (d.getTitle().equals(diary.getTitle()))
                return null;
        diaries.add(diary);
        WorkInFiles.WriteUser(this);
        return diary;
    }

    public void EditDiary(String oldTitle, Diary diary){
        for (Diary d : diaries)
            if (d.getTitle().equals(oldTitle)) {
                d.Copy(diary);
                break;
            }
    }

    public void DeleteDiary(String title){
        Diary buffer = null;
        for (Diary d : diaries)
            if (d.getTitle().equals(title)) {
                buffer = d;
                break;
            }
        if (buffer != null) {
            try {
                if (!buffer.getCreator().equals(""))
                    WorkInFiles.DeleteDiary(NickName, buffer.getTitle(), buffer.getCreator());
                else
                    WorkInFiles.DeleteDiary(NickName, buffer.getTitle());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            diaries.remove(buffer);
        }
    }


    public void Copy(User user){
        NickName = user.NickName;
        Email = user.Email;
        Password = user.Password;
        diaries = null;
        for (Diary d : user.diaries)
            diaries.add(d);
    }

    public static boolean MyConteins(List<User> users, User searchUser){
        for (User u : users)
            if (u.NickName.equals(searchUser.NickName))
                return true;
        return false;
    }
}
