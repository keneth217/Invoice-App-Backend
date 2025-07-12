package com.app.invoice.tenant.dto.school_settings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PharmacyNameDTO {
        private String schoolName;
        private String schoolLogo;

        public PharmacyNameDTO(String schoolName, String schoolLogo) {
            this.schoolName = schoolName;
            this.schoolLogo = schoolLogo;
        }
}
