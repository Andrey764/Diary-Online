package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    public String NickName;
    public String Email;
    public String Password;

    public List<Diary> diaries;

    public User(String nickName,String email,String password){
        NickName = nickName;
        Email = email;
        Password = password;
    }

    public Diary AddDiary(String title, String content){
        if (diaries == null)
            diaries = new ArrayList<>();
        for (Diary d : diaries)
            if (d.getTitle().equals(title))
                return null;
        Diary diary = new Diary(title, content);
        diaries.add(diary);
        return diary;
    }

    public Diary AddDiary(Diary diary){
        if (diaries == null)
            diaries = new ArrayList<>();
        for (Diary d : diaries)
            if (d.getTitle().equals(diary.getTitle()))
                return null;
        diaries.add(diary);
        return diary;
    }

    public void EditDiary(String oldTitle, String diaryNewTitle, String newContent, String creator, boolean isArchive){
        for (Diary d : diaries)
            if (d.getTitle().equals(oldTitle)) {
                d.setArchive(isArchive);
                d.setTitle(diaryNewTitle);
                d.setContent(newContent);
                d.setCreator(creator);
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
        diaries.remove(buffer);
    }


    public void Copy(User user){
        NickName = user.NickName;
        Email = user.Email;
        Password = user.Password;
        diaries = null;
        for (Diary d : user.diaries)
            diaries.add(d);
    }
}
