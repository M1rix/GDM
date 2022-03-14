package uz.apextech.gdm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import uz.apextech.gdm.domain.enumeration.DebtStatus;

/**
 * Criteria class for the {@link uz.apextech.gdm.domain.Debt} entity. This class is used
 * in {@link uz.apextech.gdm.web.rest.DebtResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /debts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class DebtCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DebtStatus
     */
    public static class DebtStatusFilter extends Filter<DebtStatus> {

        public DebtStatusFilter() {}

        public DebtStatusFilter(DebtStatusFilter filter) {
            super(filter);
        }

        @Override
        public DebtStatusFilter copy() {
            return new DebtStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter amount;

    private DebtStatusFilter status;

    private InstantFilter dueDate;

    private StringFilter description;

    private IntegerFilter debtScore;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter profileId;

    private LongFilter currencyId;

    private Boolean distinct;

    public DebtCriteria() {}

    public DebtCriteria(DebtCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.debtScore = other.debtScore == null ? null : other.debtScore.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.profileId = other.profileId == null ? null : other.profileId.copy();
        this.currencyId = other.currencyId == null ? null : other.currencyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DebtCriteria copy() {
        return new DebtCriteria(this);
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

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            amount = new BigDecimalFilter();
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public DebtStatusFilter getStatus() {
        return status;
    }

    public DebtStatusFilter status() {
        if (status == null) {
            status = new DebtStatusFilter();
        }
        return status;
    }

    public void setStatus(DebtStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getDueDate() {
        return dueDate;
    }

    public InstantFilter dueDate() {
        if (dueDate == null) {
            dueDate = new InstantFilter();
        }
        return dueDate;
    }

    public void setDueDate(InstantFilter dueDate) {
        this.dueDate = dueDate;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getDebtScore() {
        return debtScore;
    }

    public IntegerFilter debtScore() {
        if (debtScore == null) {
            debtScore = new IntegerFilter();
        }
        return debtScore;
    }

    public void setDebtScore(IntegerFilter debtScore) {
        this.debtScore = debtScore;
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

    public LongFilter getProfileId() {
        return profileId;
    }

    public LongFilter profileId() {
        if (profileId == null) {
            profileId = new LongFilter();
        }
        return profileId;
    }

    public void setProfileId(LongFilter profileId) {
        this.profileId = profileId;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public LongFilter currencyId() {
        if (currencyId == null) {
            currencyId = new LongFilter();
        }
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
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
        final DebtCriteria that = (DebtCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(debtScore, that.debtScore) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(profileId, that.profileId) &&
            Objects.equals(currencyId, that.currencyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            amount,
            status,
            dueDate,
            description,
            debtScore,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            profileId,
            currencyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DebtCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (debtScore != null ? "debtScore=" + debtScore + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (profileId != null ? "profileId=" + profileId + ", " : "") +
            (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
