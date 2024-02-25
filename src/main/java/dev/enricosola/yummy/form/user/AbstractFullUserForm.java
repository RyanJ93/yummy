package dev.enricosola.yummy.form.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import dev.enricosola.yummy.form.Form;

public abstract class AbstractFullUserForm implements Form, UserForm {
    @Size(max = 32, message = "Username cannot be longer than 32 characters.")
    @NotBlank(message = "Username cannot be empty.")
    protected String username;

    @Size(max = 128, min = 8, message = "Password must be between 8 and 128 characters.")
    @NotBlank(message = "Password cannot be empty.")
    protected String password;

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }
}
