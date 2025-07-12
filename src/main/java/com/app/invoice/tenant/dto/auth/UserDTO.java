package com.app.invoice.tenant.dto.auth;

import com.app.invoice.tenant.entity.auth.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userID;
    private String fullName;
    private String phoneNo;
    private String email;
    private Set<String> userTypes = new HashSet<>();
    private Long addedBy;
    private String passcode;

    public UserDTO(User user) {
        setUserID(user.getUserID());
        setFullName(user.getFullName());
        setPhoneNo(user.getPhoneNo());
        setUserTypes(user.getUserTypes());
        setAddedBy(user.getAddedBy());
        setPasscode(user.getPasscode());
    }

}
