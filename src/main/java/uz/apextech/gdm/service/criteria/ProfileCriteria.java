package uz.apextech.gdm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import uz.apextech.gdm.domain.enumeration.ProfileStatus;

/**
 * Criteria class for the {@link uz.apextech.gdm.domain.Profile} entity. This class is used
 * in {@link uz.apextech.gdm.web.rest.ProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProfileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProfileStatus
     */
    public static class ProfileStatusFilter extends Filter<ProfileStatus> {

        public ProfileStatusFilter() {}

        public ProfileStatusFilter(ProfileStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProfileStatusFilter copy() {
            return new ProfileStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter passport;

    private StringFilter jshshir;

    private StringFilter inn;

    private StringFilter phone;

    private StringFilter accessToken;

    private StringFilter firstName;

    private StringFilter lastName;

    private DoubleFilter score;

    private ProfileStatusFilter status;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter debtId;

    private Boolean distinct;

    public ProfileCriteria() {}

    public ProfileCriteria(ProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.passport = other.passport == null ? null : other.passport.copy();
        this.jshshir = other.jshshir == null ? null : other.jshshir.copy();
        this.inn = other.inn == null ? null : other.inn.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.accessToken = other.accessToken == null ? null : other.accessToken.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.score = other.score == null ? null : other.score.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.debtId = other.debtId == null ? null : other.debtId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProfileCriteria copy() {
        return new ProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPassport() {
        return passport;
    }

    public StringFilter passport() {
        if (passport == null) {
            passport = new StringFilter();
        }
        return passport;
    }

    public void setPassport(StringFilter passport) {
        this.passport = passport;
    }

    public StringFilter getJshshir() {
        return jshshir;
    }

    public StringFilter jshshir() {
        if (jshshir == null) {
            jshshir = new StringFilter();
        }
        return jshshir;
    }

    public void setJshshir(StringFilter jshshir) {
        this.jshshir = jshshir;
    }

    public StringFilter getInn() {
        return inn;
    }

    public StringFilter inn() {
        if (inn == null) {
            inn = new StringFilter();
        }
        return inn;
    }

    public void setInn(StringFilter inn) {
        this.inn = inn;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAccessToken() {
        return accessToken;
    }

    public StringFilter accessToken() {
        if (accessToken == null) {
            accessToken = new StringFilter();
        }
        return accessToken;
    }

    public void setAccessToken(StringFilter accessToken) {
        this.accessToken = accessToken;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public DoubleFilter getScore() {
        return score;
    }

    public DoubleFilter score() {
        if (score == null) {
            score = new DoubleFilter();
        }
        return score;
    }

    public void setScore(DoubleFilter score) {
        this.score = score;
    }

    public ProfileStatusFilter getStatus() {
        return status;
    }

    public ProfileStatusFilter status() {
        if (status == null) {
            status = new ProfileStatusFilter();
        }
        return status;
    }

    public void setStatus(ProfileStatusFilter status) {
        this.status = status;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getDebtId() {
        return debtId;
    }

    public LongFilter debtId() {
        if (debtId == null) {
            debtId = new LongFilter();
        }
        return debtId;
    }

    public void setDebtId(LongFilter debtId) {
        this.debtId = debtId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfileCriteria that = (ProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(passport, that.passport) &&
            Objects.equals(jshshir, that.jshshir) &&
            Objects.equals(inn, that.inn) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(accessToken, that.accessToken) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(score, that.score) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(debtId, that.debtId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            passport,
            jshshir,
            inn,
            phone,
            accessToken,
            firstName,
            lastName,
            score,
            status,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            debtId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (passport != null ? "passport=" + passport + ", " : "") +
            (jshshir != null ? "jshshir=" + jshshir + ", " : "") +
            (inn != null ? "inn=" + inn + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (accessToken != null ? "accessToken=" + accessToken + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (score != null ? "score=" + score + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (debtId != null ? "debtId=" + debtId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
