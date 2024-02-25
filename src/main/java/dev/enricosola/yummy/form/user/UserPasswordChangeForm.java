package dev.enricosola.yummy.form.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import dev.enricosola.yummy.form.Form;

public class UserPasswordChangeForm implements Form, UserForm {
    @Size(max = 128, min = 8, message = "Password must be between 8 and 128 characters.")
    @NotBlank(message = "Password confirm cannot be empty.")
    protected String passwordConfirm;

    @Size(max = 128, min = 8, message = "Password must be between 8 and 128 characters.")
    @NotBlank(message = "Password cannot be empty.")
    protected String password;

    public UserPasswordChangeForm(){}

    public void setPasswordConfirm(String passwordConfirm){
        this.passwordConfirm = passwordConfirm;
    }

    public String getPasswordConfirm(){
        return this.passwordConfirm;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }
}
