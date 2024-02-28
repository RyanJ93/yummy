package dev.enricosola.yummy.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.io.Serial;
import java.util.Date;

@Entity
@Table(name = "persistent_tokens")
public class PersistentToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 4807147561755432420L;

    @Column(name = "series")
    @Id
    private String series;

    @Column(name = "username")
    private String username;

    @Column(name = "token_value")
    private String tokenValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setSeries(String series){
        this.series = series;
    }

    public String getSeries(){
        return this.series;
    }

    public void setTokenValue(String tokenValue){
        this.tokenValue = tokenValue;
    }

    public String getTokenValue(){
        return this.tokenValue;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }
}
