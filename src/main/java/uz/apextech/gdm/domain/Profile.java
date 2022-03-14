package uz.apextech.gdm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import uz.apextech.gdm.domain.enumeration.ProfileStatus;

/**
 * A Profile.
 */
@Entity
@Table(name = "apex_profile")
public class Profile extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 12)
    @Column(name = "passport", length = 12, unique = true)
    private String passport;

    @Size(max = 12)
    @Column(name = "jshshir", length = 12, unique = true)
    private String jshshir;

    @Size(max = 15)
    @Column(name = "inn", length = 15, unique = true)
    private String inn;

    @Size(min = 12, max = 23)
    @Column(name = "phone", length = 23, unique = true)
    private String phone;

    @NotNull
    @Size(min = 32, max = 255)
    @Column(name = "access_token", length = 255, nullable = false, unique = true)
    private String accessToken;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10")
    @Column(name = "score")
    private Double score;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProfileStatus status;

    @OneToMany(mappedBy = "profile")
    @JsonIgnoreProperties(value = { "profile", "currency" }, allowSetters = true)
    private Set<Debt> debts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Profile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassport() {
        return this.passport;
    }

    public Profile passport(String passport) {
        this.setPassport(passport);
        return this;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getJshshir() {
        return this.jshshir;
    }

    public Profile jshshir(String jshshir) {
        this.setJshshir(jshshir);
        return this;
    }

    public void setJshshir(String jshshir) {
        this.jshshir = jshshir;
    }

    public String getInn() {
        return this.inn;
    }

    public Profile inn(String inn) {
        this.setInn(inn);
        return this;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getPhone() {
        return this.phone;
    }

    public Profile phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public Profile accessToken(String accessToken) {
        this.setAccessToken(accessToken);
        return this;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Profile firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Profile lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getScore() {
        return this.score;
    }

    public Profile score(Double score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public ProfileStatus getStatus() {
        return this.status;
    }

    public Profile status(ProfileStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProfileStatus status) {
        this.status = status;
    }

    public Profile createdBy(String createdBy) {
        setCreatedBy(createdBy);
        return this;
    }

    public Profile createdDate(Instant createdDate) {
        setCreatedDate(createdDate);
        return this;
    }

    public Profile lastModifiedBy(String lastModifiedBy) {
        setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public Profile lastModifiedDate(Instant lastModifiedDate) {
        setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Debt> getDebts() {
        return this.debts;
    }

    public void setDebts(Set<Debt> debts) {
        if (this.debts != null) {
            this.debts.forEach(i -> i.setProfile(null));
        }
        if (debts != null) {
            debts.forEach(i -> i.setProfile(this));
        }
        this.debts = debts;
    }

    public Profile debts(Set<Debt> debts) {
        this.setDebts(debts);
        return this;
    }

    public Profile addDebt(Debt debt) {
        this.debts.add(debt);
        debt.setProfile(this);
        return this;
    }

    public Profile removeDebt(Debt debt) {
        this.debts.remove(debt);
        debt.setProfile(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profile{" +
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
