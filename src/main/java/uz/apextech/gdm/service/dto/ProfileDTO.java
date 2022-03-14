package uz.apextech.gdm.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import uz.apextech.gdm.domain.enumeration.ProfileStatus;

/**
 * A DTO for the {@link uz.apextech.gdm.domain.Profile} entity.
 */
public class ProfileDTO implements Serializable {

    private Long id;

    @Size(max = 12)
    private String passport;

    @Size(max = 12)
    private String jshshir;

    @Size(max = 15)
    private String inn;

    @Size(min = 12, max = 23)
    private String phone;

    @NotNull
    @Size(min = 32, max = 255)
    private String accessToken;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10")
    private Double score;

    @NotNull
    private ProfileStatus status;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getJshshir() {
        return jshshir;
    }

    public void setJshshir(String jshshir) {
        this.jshshir = jshshir;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public ProfileStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDTO)) {
            return false;
        }

        ProfileDTO profileDTO = (ProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + getId() +
            ", passport='" + getPassport() + "'" +
            ", jshshir='" + getJshshir() + "'" +
            ", inn='" + getInn() + "'" +
            ", phone='" + getPhone() + "'" +
            ", accessToken='" + getAccessToken() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", score=" + getScore() +
            ", status='" + getStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
