package Models;

public class Store {
        public String Title;
        public String Address;
        public String Phone;
        public String Email;
        public String Link;
        public String Category;
        public String Description ;
        public Store(String title, String address, String phone,
                     String email, String link, String category, String description){
            Link = link;
            Title = title;
            Phone = phone;
            Email = email;
            Address = address;
            Category = category;
            Description = description;
        }
        public void Copy(Models.Store human){
            Link = human.Link;
            Title = human.Title;
            Phone = human.Phone;
            Email = human.Email;
            Address = human.Address;
            Category = human.Category;
            Description = human.Description;
        }
}
