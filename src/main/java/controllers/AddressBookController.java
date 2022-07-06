package controllers;

import Models.AddressBook;
import Models.Human;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping("/book")
public class AddressBookController {
    AddressBook addressBook;
    public AddressBookController() {
        this.addressBook = new AddressBook();
        addressBook.AddHuman(new Human("Andrey","Grigoriev", "Viktorovich",
                "0684267002","Andrii@gmail.com","???","not note"));
        addressBook.AddHuman(new Human("Andrey","Grigoriev", "Viktorovich",
                "0684267002","Andrii@gmail.com","???","not note"));
    }
    @GetMapping("/main")
    public String ListHuman(Model model){
        model.addAttribute("humans", addressBook.GetHumans());
        return "addressBook";
    }

    @PostMapping("/search")
    public String ListHuman(Model model, @RequestParam("searchName") String searchName){
        model.addAttribute("humans", addressBook.GetHumans(searchName));
        return "addressBook";
    }

    @GetMapping("/delete")
    public String DeleteHuman(Model model, @RequestParam("name") String name){
        addressBook.DeleteHuman(name);
        return ListHuman(model);
    }

    @GetMapping("add")
    public String AddHuman(){
        return "addHuman";
    }

    @GetMapping("edit")
    public String EditHuman(Model model, @RequestParam("name") String name){
        model.addAttribute("human", addressBook.GetHuman(name));
        return "editHuman";
    }

    @GetMapping("show")
    public String ShowHuman(Model model, @RequestParam("name") String name){
        model.addAttribute("human", addressBook.GetHuman(name));
        return "showHuman";
    }

    @PostMapping("/editSave")
    public String SaveHuman(Model model, @RequestParam("oldName") String oldName, @RequestParam("name")
    String name, @RequestParam("surname") String surname, @RequestParam("patronymic")
    String patronymic,@RequestParam("phone") String phone, @RequestParam("email")
    String email,@RequestParam("linkToBlog") String linkToBlog, @RequestParam("note") String note){
        addressBook.ReplaceHuman(oldName, new Human(name,surname,patronymic,phone,email,linkToBlog,note));
        model.addAttribute("humans", addressBook.GetHumans());
        return "addressBook";
    }

    @PostMapping("/save")
    public String EditSaveHuman(Model model, @RequestParam("name") String name, @RequestParam("surname")
    String surname, @RequestParam("patronymic") String patronymic,@RequestParam("phone")
    String phone, @RequestParam("email") String email,@RequestParam("linkToBlog")
    String linkToBlog, @RequestParam("note") String note){
        addressBook.AddHuman(new Human(name,surname,patronymic,phone,email,linkToBlog,note));
        model.addAttribute("humans", addressBook.GetHumans());
        return "addressBook";
    }
}
