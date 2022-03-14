package uz.apextech.gdm.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TextLocalization.
 */
@Entity
@Table(name = "apex_text_localization")
public class TextLocalization extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "code", length = 150, nullable = false)
    private String code;

    @NotNull
    @Size(max = 150)
    @Column(name = "message", length = 150, nullable = false)
    private String message;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "locale", length = 2, nullable = false)
    private String locale;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TextLocalization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public TextLocalization code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public TextLocalization message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocale() {
        return this.locale;
    }

    public TextLocalization locale(String locale) {
        this.setLocale(locale);
        return this;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public TextLocalization createdBy(String createdBy) {
        setCreatedBy(createdBy);
        return this;
    }

    public TextLocalization createdDate(Instant createdDate) {
        setCreatedDate(createdDate);
        return this;
    }

    public TextLocalization lastModifiedBy(String lastModifiedBy) {
        setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public TextLocalization lastModifiedDate(Instant lastModifiedDate) {
        setLastModifiedDate(lastModifiedDate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TextLocalization)) {
            return false;
        }
        return id != null && id.equals(((TextLocalization) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TextLocalization{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", message='" + getMessage() + "'" +
            ", locale='" + getLocale() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
