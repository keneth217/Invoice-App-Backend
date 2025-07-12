package com.app.invoice.master.dto;


import com.app.invoice.master.entity.MasterUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterUserDTO {
    private Long userID;
    private String fullName;
    private String phoneNo;
    private String email;
    private String userType;
    private Long addedBy;
    private String passcode;

    public MasterUserDTO(MasterUser masterUser) {
        setUserID(masterUser.getUserID());
        setFullName(masterUser.getFullName());
        setPhoneNo(masterUser.getPhoneNo());
        setUserType(masterUser.getUserType());
        setAddedBy(masterUser.getAddedBy());
        setPasscode(masterUser.getPasscode());
    }

}
