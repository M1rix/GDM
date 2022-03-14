package uz.apextech.gdm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import uz.apextech.gdm.domain.enumeration.CurrencyStatus;

/**
 * A Currency.
 */
@Entity
@Table(name = "apex_currency")
public class Currency extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 3)
    @Column(name = "code", length = 3, nullable = false)
    private String code;

    @NotNull
    @Column(name = "country_flag", nullable = false)
    private String countryFlag;

    @Column(name = "position")
    private Integer position;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CurrencyStatus status;

    @OneToMany(mappedBy = "currency")
    @JsonIgnoreProperties(value = { "profile", "currency" }, allowSetters = true)
    private Set<Debt> debts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Currency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Currency code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryFlag() {
        return this.countryFlag;
    }

    public Currency countryFlag(String countryFlag) {
        this.setCountryFlag(countryFlag);
        return this;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }

    public Integer getPosition() {
        return this.position;
    }

    public Currency position(Integer position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public CurrencyStatus getStatus() {
        return this.status;
    }

    public Currency status(CurrencyStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CurrencyStatus status) {
        this.status = status;
    }

    public Currency createdBy(String createdBy) {
        setCreatedBy(createdBy);
        return this;
    }

    public Currency createdDate(Instant createdDate) {
        setCreatedDate(createdDate);
        return this;
    }

    public Currency lastModifiedBy(String lastModifiedBy) {
        setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public Currency lastModifiedDate(Instant lastModifiedDate) {
        setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<Debt> getDebts() {
        return this.debts;
    }

    public void setDebts(Set<Debt> debts) {
        if (this.debts != null) {
            this.debts.forEach(i -> i.setCurrency(null));
        }
        if (debts != null) {
            debts.forEach(i -> i.setCurrency(this));
        }
        this.debts = debts;
    }

    public Currency debts(Set<Debt> debts) {
        this.setDebts(debts);
        return this;
    }

    public Currency addDebt(Debt debt) {
        this.debts.add(debt);
        debt.setCurrency(this);
        return this;
    }

    public Currency removeDebt(Debt debt) {
        this.debts.remove(debt);
        debt.setCurrency(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currency)) {
            return false;
        }
        return id != null && id.equals(((Currency) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Currency{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", countryFlag='" + getCountryFlag() + "'" +
            ", position=" + getPosition() +
            ", status='" + getStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
