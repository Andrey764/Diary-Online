package Models;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Context implements Serializable {
    public List<User> users;
    public Context(){
        users = new ArrayList<>();
    }
    public void AddUser(User user) {
        if (users == null)
            users = new ArrayList<>();
        users.add(user);
        WorkInFiles.WriteContext(this);
    }

    public User GetUser(String userName){
        for (User user : users)
            if (user.NickName.equals(userName))
                return user;
        return null;
    }

    public void AddDiary(String userName, String title, String context){
        for (User u: users)
            if (u.NickName.equals(userName)){
                u.AddDiary(title, context);
                break;
            }
        WorkInFiles.WriteContext(this);
    }
    public void AddDiary(String userName, Diary diary) throws IOException {
        for (User u: users)
            if (u.NickName.equals(userName)){
                u.AddDiary(diary);
            }
        WorkInFiles.WriteContext(this);
    }

    public void ReplaceDiary(String userName, String diaryOldTitle, String diaryNewTitle, String newContent){
        for (User u: users)
            if (u.NickName.equals(userName))
                for (Diary d : u.diaries)
                    if (d.getTitle().equals(diaryOldTitle)) {
                        u.EditDiary(diaryOldTitle, diaryNewTitle, newContent, d.getCreator(), d.getArchive());
                        break;
                    }
        WorkInFiles.WriteContext(this);
    }

    public void DeleteDiary(String userName, String diaryTitle){
        Diary d = GetDiary(userName, diaryTitle);
        for (User u: users) {
            if (!d.getCreator().equals(""))
                if (u.NickName.equals(d.getCreator()))
                    u.diaries.remove(d);
            if (u.NickName.equals(userName))
                u.DeleteDiary(diaryTitle);
        }
        WorkInFiles.WriteContext(this);
    }

    public Diary GetDiary(String userName, String diaryTitle){
        for (User u: users)
            if (u.NickName.equals(userName))
                for(Diary d: u.diaries)
                    if (d.getTitle().equals(diaryTitle))
                        return d;
        return null;
    }
    public void ArchivingDiary(String nickName, String title, boolean isArcive) {
        for (User u : users)
            if (u.NickName.equals(nickName))
                for (Diary d : u.diaries)
                    if (d.getTitle().equals(title)) {
                        d.setArchive(isArcive);
                        u.EditDiary(d.getTitle(), d.getTitle(), d.getContent(), d.getCreator(), d.getArchive());
                        break;
                    }
        WorkInFiles.WriteContext(this);
    }

    public void ShareDiary(String whoseUser, String whomUser, Diary diary){
        diary.setCreator(whoseUser);
        try {
            AddDiary(whomUser, diary);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
