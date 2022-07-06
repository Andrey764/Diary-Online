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

    public void EditDiary(String oldTitle, String newTitle, String newContent) throws IOException {
        for (Diary d : diaries)
            if (d.getTitle().equals(oldTitle)) {
                d.setTitle(newTitle);
                d.setContent(newContent);
                WorkInFiles.DeleteDiary(NickName, oldTitle);
                WorkInFiles.WriteUser(this);
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
            diaries.remove(buffer);
            try {
                WorkInFiles.DeleteDiary(NickName, buffer.getTitle());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
