package ks.glowlyapp.owner;

import jakarta.persistence.*;
import ks.glowlyapp.business.Business;
import ks.glowlyapp.user.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Owner extends User {
    @OneToMany(mappedBy = "owner")
    private List<Business> businessList = new ArrayList<>();
}
