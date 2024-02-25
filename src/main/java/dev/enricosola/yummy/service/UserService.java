package dev.enricosola.yummy.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import dev.enricosola.yummy.form.user.UserPasswordChangeForm;
import dev.enricosola.yummy.repository.UserRepository;
import dev.enricosola.yummy.form.user.UserCreateForm;
import dev.enricosola.yummy.form.user.UserEditForm;
import org.springframework.stereotype.Service;
import dev.enricosola.yummy.entity.User;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private User user;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    public User getById(int id){
        return this.user = this.userRepository.findOne(id);
    }

    public User getByUsername(String username){
        return this.user = this.userRepository.findByUsername(username);
    }

    public List<User> getAll(){
        return this.userRepository.findAll();
    }

    public User createFromForm(UserCreateForm userCreateForm){
        return this.create(userCreateForm.getUsername(), userCreateForm.getPassword());
    }

    public User updateFromForm(UserEditForm userEditForm){
        return this.update(userEditForm.getUsername());
    }

    public User changePasswordFromForm(UserPasswordChangeForm userPasswordChangeForm){
        return this.changePassword(userPasswordChangeForm.getPassword());
    }

    public User create(String username, String password){
        String encryptedPassword = this.passwordEncoder.encode(password);
        User user = new User();
        user.setPassword(encryptedPassword);
        user.setUsername(username);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return this.user = this.userRepository.store(user);
    }

    public User update(String username){
        this.user.setUsername(username);
        this.user.setUpdatedAt(new Date());
        return this.userRepository.update(this.user);
    }

    public User changePassword(String password){
        String encryptedPassword = this.passwordEncoder.encode(password);
        this.user.setPassword(encryptedPassword);
        this.user.setUpdatedAt(new Date());
        return this.userRepository.update(this.user);
    }

    public void delete(){
        this.userRepository.delete(this.user);
        this.user = null;
    }
}
