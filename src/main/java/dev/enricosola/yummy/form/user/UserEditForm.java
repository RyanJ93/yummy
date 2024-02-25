package dev.enricosola.yummy.form.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import dev.enricosola.yummy.entity.User;
import dev.enricosola.yummy.form.Form;

public class UserEditForm implements Form, UserForm {
    @Size(max = 32, message = "Username cannot be longer than 32 characters.")
    @NotBlank(message = "Username cannot be empty.")
    protected String username;

    public UserEditForm(){}

    public UserEditForm(User user){
        this.username = user.getUsername();
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }
}
