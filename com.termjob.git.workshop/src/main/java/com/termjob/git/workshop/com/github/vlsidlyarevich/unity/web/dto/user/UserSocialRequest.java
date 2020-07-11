package com.github.vlsidlyarevich.unity.web.dto.user;

import com.github.vlsidlyarevich.unity.domain.model.UserSocial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UserSocialRequest implements Serializable {

    private static final long serialVersionUID = -8632737767330638824L;

    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String linkedIn;
    private String twitter;
    private String facebook;
    private String email;
    private String skype;
    private String additional;
    private String image;
    private Date createdAt;
    private Date updatedAt;

    public static UserSocialRequest fromDomain(final UserSocial model) {
        UserSocialRequest dto = new UserSocialRequest();
        dto.setId(model.getId());
        dto.setUserId(model.getUserId());
        dto.setFirstName(model.getFirstName() != null ? model.getFirstName() : "");
        dto.setLastName(model.getLastName() != null ? model.getLastName() : "");
        dto.setLinkedIn(model.getLinkedIn() != null ? model.getLinkedIn() : "");
        dto.setTwitter(model.getTwitter() != null ? model.getTwitter() : "");
        dto.setFacebook(model.getFacebook() != null ? model.getFacebook() : "");
        dto.setEmail(model.getEmail() != null ? model.getEmail() : "");
        dto.setSkype(model.getSkype() != null ? model.getSkype() : "");
        dto.setAdditional(model.getAdditional() != null ? model.getAdditional() : "");
        dto.setImage(model.getImage() != null ? model.getImage() : "");
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());

        return dto;
    }
}
