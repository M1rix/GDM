package uz.apextech.gdm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import uz.apextech.gdm.domain.enumeration.DebtStatus;

/**
 * A Debt.
 */
@Entity
@Table(name = "apex_debt")
public class Debt extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DebtStatus status;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "debt_score")
    private Integer debtScore;

    @ManyToOne
    @JsonIgnoreProperties(value = { "debts" }, allowSetters = true)
    private Profile profile;

    @ManyToOne
    @JsonIgnoreProperties(value = { "debts" }, allowSetters = true)
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Debt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Debt amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DebtStatus getStatus() {
        return this.status;
    }

    public Debt status(DebtStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DebtStatus status) {
        this.status = status;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public Debt dueDate(Instant dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return this.description;
    }

    public Debt description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDebtScore() {
        return this.debtScore;
    }

    public Debt debtScore(Integer debtScore) {
        this.setDebtScore(debtScore);
        return this;
    }

    public void setDebtScore(Integer debtScore) {
        this.debtScore = debtScore;
    }

    public Debt createdBy(String createdBy) {
        setCreatedBy(createdBy);
        return this;
    }

    public Debt createdDate(Instant createdDate) {
        setCreatedDate(createdDate);
        return this;
    }

    public Debt lastModifiedBy(String lastModifiedBy) {
        setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public Debt lastModifiedDate(Instant lastModifiedDate) {
        setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Debt profile(Profile profile) {
        this.setProfile(profile);
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Debt currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Debt)) {
            return false;
        }
        return id != null && id.equals(((Debt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Debt{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", debtScore=" + getDebtScore() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
