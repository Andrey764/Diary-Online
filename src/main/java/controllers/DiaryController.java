package controllers;

import Models.Context;
import Models.Diary;
import Models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/diary")
public class DiaryController {
    static Context context = new Context();
    private String MySetAttribute(Model model, String attribute, String value, String returnPage){
        model.addAttribute(attribute, value);
        return returnPage;
    }
    private String MySetAttribute(Model model, String attribute, Diary value, String returnPage){
        model.addAttribute(attribute, value);
        return returnPage;
    }
    private String MySetAttribute(Model model, String attribute, User value, String returnPage){
        model.addAttribute(attribute, value);
        return returnPage;
    }

    @GetMapping("/")
    public String Login() throws IOException {
        return "login";
    }

    @GetMapping("/registration")
    public String Registration(){
        return "registration";
    }

    @PostMapping("/")
    public String Login(@RequestParam("nickName") String nickName,
                        @RequestParam("password") String password,
                        Model model){
        User user = context.GetUser(nickName);
        if(user == null)
            return MySetAttribute(model, "error", "user is not found", "login");
        if(!user.Password.equals(password))
            return MySetAttribute(model, "error", "password is not correct", "login");
        return MySetAttribute(model, "user", user, "index");
    }

    @PostMapping("/registration")
    public String Registration(@RequestParam("nickName") String nickName, @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("confPassword") String confPassword, Model model) {
        if (!password.equals(confPassword))
            return MySetAttribute(model, "error", "password not confirmed", "registration");
        if (password.length() < 4)
            return MySetAttribute(model, "error", "password is too short", "registration");
        context.AddUser(new User(nickName, email, password));
        return "login";
    }

    @GetMapping("/addDiary")
    public String AddDiaryToUser(Model model, @RequestParam("nickName") String nickName){
        return MySetAttribute(model, "nickName", nickName, "addDiary");
    }

    @PostMapping("/addDiary")
    public String AddDiaryToUser(@RequestParam("nickName") String nickName, @RequestParam("title") String title,
                                 @RequestParam("content") String content, Model model) throws IOException {
        context.AddDiary(nickName, title, content);
        return MySetAttribute(model, "user", context.GetUser(nickName), "index");
    }

    @GetMapping("/deleteDiary")
    public String DeleteDiary(@RequestParam("nickName") String nickName, @RequestParam("title") String title,
                              Model model){
        context.DeleteDiary(nickName,title);
        return MySetAttribute(model, "user", context.GetUser(nickName), "index");
    }
    @GetMapping("/editDiary")
    public String EditDiary(@RequestParam("nickName") String nickName, @RequestParam("title") String title,
                            @RequestParam("content") String content, Model model){
        model.addAttribute("title", title);
        model.addAttribute("content", content);
        model.addAttribute("link", "editDiary");
        return MySetAttribute(model, "nickName", nickName, "addDiary");
    }

    @PostMapping("/editDiary")
    public String EditDiary(@RequestParam("nickName") String nickName, @RequestParam("title") String title,
                            @RequestParam("content") String content, @RequestParam("oldTitle") String oldTitle,
                            Model model) throws IOException {
        context.ReplaceDiary(nickName,oldTitle,title,content);
        return MySetAttribute(model, "user", context.GetUser(nickName), "index");
    }

    @GetMapping("/showDiary")
    public String ShowDiary(@RequestParam("nickName") String nickName, @RequestParam("title") String title,
                            Model model){
        model.addAttribute("nickName", nickName);
        return MySetAttribute(model, "diary", context.GetDiary(nickName, title), "diary");
    }

    @PostMapping("/showDiary")
    public String ShowDiary(@RequestParam("nickName") String nickName, @RequestParam("title") String title,
                            @RequestParam("file") File file, Model model){
        context.AddFileToDiary(file, nickName, title);
        model.addAttribute("userName", nickName);
        return MySetAttribute(model, "diary", context.GetDiary(nickName, title), "diary");
    }

    @PostMapping("/saveFile")
    public String SaveFile(@RequestParam("nickName") String nickName, @RequestParam("title") String title,
                           @RequestParam("path") String path){
        return "";
    }

    @GetMapping("/main")
    public String Main(@RequestParam("nickName") String nickName, Model model){
        return MySetAttribute(model, "user", context.GetUser(nickName), "index");
    }

    @GetMapping("/archiving")
    public String Main(@RequestParam("nickName") String nickName, @RequestParam("archive") boolean isArchive,
                       @RequestParam("title") String title, Model model){
        context.ArchivingDiary(nickName, title, isArchive);
        return MySetAttribute(model, "user", context.GetUser(nickName), "index");
    }

    @GetMapping("/share")
    public String ShareDiary(@RequestParam("title") String title, @RequestParam("whoseUser")  String whoseUser,
                             @RequestParam("whowUser") String whowUser, Model model){
        context.ShareDiary(whoseUser, whowUser, context.GetDiary(whoseUser, title));
        model.addAttribute("userName", whoseUser);
        return MySetAttribute(model, "diary", context.GetDiary(whoseUser, title), "diary");
    }

    //TODO После выполнение предыдущих пунктов и при наличии времени и желания
    // сделать возможность делится дневниками с другими пользователями
}
