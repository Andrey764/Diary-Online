package Models;

public class Human {
    public String Name;
    public String Surname;
    public String Patronymic;
    public String Phone;
    public String Email;
    public String LinkToBlog;
    public String Note;

    public Human(String name, String surname, String patronymic, String phone,
                 String email, String linkToBlog, String note){
        Name = name;
        Note = note;
        Phone = phone;
        Email = email;
        Surname = surname;
        Patronymic = patronymic;
        LinkToBlog = linkToBlog;
    }
    public void Copy(Human human){
        Name = human.Name;
        Note = human.Note;
        Phone = human.Phone;
        Email = human.Email;
        Surname = human.Surname;
        Patronymic = human.Patronymic;
        LinkToBlog = human.LinkToBlog;
    }
}
