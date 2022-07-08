package Models;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Context {
    public List<User> users;
    public Context(){
        users = new ArrayList<>();
        Download();
    }

    public void AddUser(User user) {
        if (users == null)
            users = new ArrayList<>();
        users.add(user);
        Save();
    }

    public User GetUser(String userName){
        for (User user : users)
            if (user.NickName.equals(userName))
                return user;
        return null;
    }

    public void DeletUser(String userName){
        for (User user : users)
            if (user.NickName.equals(userName)){
                users.remove(user);
                return;
            }
    }

    public void CopyUser(User user){
        for (User u : users)
            if (user.NickName.equals(u.NickName))
                u.Copy(user);
    }

    public void AddDiary(String userName, String title, String context) throws IOException {
        for (User u: users)
            if (u.NickName.equals(userName)){
                u.AddDiary(title, context);
                break;
            }
    }
    public void AddDiary(String userName, Diary diary) throws IOException {
        for (User u: users)
            if (u.NickName.equals(userName)){
                u.AddDiary(diary);
            }
    }

    public void ReplaceDiary(String userName, String diaryOldTitle, String diaryNewTitle, String newContent)
            throws IOException {
        for (User u: users)
            if (u.NickName.equals(userName))
                for (Diary d : u.diaries)
                    if (d.getTitle().equals(diaryOldTitle)) {
                        ReplaceDiary(u, d, diaryOldTitle, diaryNewTitle, newContent);
                        break;
                    }
    }

    private void ReplaceDiary(User u, Diary d, String diaryOldTitle, String diaryNewTitle, String newContent) throws IOException {
        u.EditDiary(diaryOldTitle, d);
        if (!d.getCreator().equals("")) {
            User creator = GetUser(d.getCreator());
            creator.EditDiary(diaryOldTitle, d);
            WorkInFiles.DeleteDiary(u.NickName, diaryOldTitle, creator.NickName);
            WorkInFiles.WriteUser(creator);
            WorkInFiles.WriteUser(u);
        }
        else {
            WorkInFiles.DeleteDiary(u.NickName, diaryOldTitle);
            WorkInFiles.WriteUser(u);
        }
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
    }

    public Diary GetDiary(String userName, String diaryTitle){
        for (User u: users)
            if (u.NickName.equals(userName))
                for(Diary d: u.diaries)
                    if (d.getTitle().equals(diaryTitle))
                        return d;
        return null;
    }

    public void Save(){
        try {
            WorkInFiles.WriteUsers(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Download(){
        try {
            users.addAll(WorkInFiles.ReadUsers());
            for(User user: users)
                user.diaries = WorkInFiles.ReadDiaries(user.NickName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void AddFileToDiary(File file, String nickName, String title){
        try {
            WorkInFiles.TransferFile(file, nickName, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ArchivingDiary(String nickName, String title, boolean isArcive) {
        for (User u : users)
            if (u.NickName.equals(nickName))
                for (Diary d : u.diaries)
                    if (d.getTitle().equals(title)) {
                        d.setArchive(isArcive);
                        try {
                            ReplaceDiary(u, d, d.getTitle(), d.getTitle(), d.getContent());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
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
