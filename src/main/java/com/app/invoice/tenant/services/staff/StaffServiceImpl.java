package com.app.invoice.tenant.services.staff;

import com.app.invoice.tenant.dto.StaffDto;
import com.app.invoice.tenant.entity.nurse.Staff;
import com.app.invoice.tenant.repos.nurse.StaffRepository;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository nurseRepository;

    public StaffServiceImpl(StaffRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    public StaffDto saveNurse(StaffDto nurseDTO) {
        Staff nurseToSave = new Staff();
        nurseToSave.setId(nurseDTO.getId());
        nurseToSave.setNationalIdNumber(nurseDTO.getNationalIdNumber());
        nurseToSave.setNurseNumber(nurseDTO.getNurseNumber());
        nurseToSave.setFullName(nurseDTO.getFullName());
        nurseToSave.setInitials(nurseDTO.getInitials());
        nurseToSave.setPhoneNumber(nurseDTO.getPhoneNumber());
        nurseToSave.setGender(nurseDTO.getGender());
        nurseToSave.setAddress(nurseDTO.getAddress());
        nurseToSave.setSignature(nurseDTO.getSignature());
        nurseToSave.setProfilePhoto(nurseDTO.getProfilePhoto());
        nurseToSave.setEmail(nurseDTO.getEmail());

        Staff savedNurse = nurseRepository.save(nurseToSave);

        // Map back to DTO
        StaffDto savedDto = new StaffDto();
        savedDto.setId(savedNurse.getId());
        savedDto.setNationalIdNumber(savedNurse.getNationalIdNumber());
        savedDto.setNurseNumber(savedNurse.getNurseNumber());
        savedDto.setFullName(savedNurse.getFullName());
        savedDto.setInitials(savedNurse.getInitials());
        savedDto.setPhoneNumber(savedNurse.getPhoneNumber());
        savedDto.setGender(savedNurse.getGender());
        savedDto.setAddress(savedNurse.getAddress());
        savedDto.setSignature(savedNurse.getSignature());
        savedDto.setProfilePhoto(savedNurse.getProfilePhoto());
        savedDto.setEmail(savedNurse.getEmail());

        return savedDto;
    }

}
