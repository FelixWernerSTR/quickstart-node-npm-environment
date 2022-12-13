package de.svi.retrospective.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "retrospective_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RetrospectiveItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "svi_file")
    private byte[] file;

    @Column(name = "svi_file_content_type")
    private String fileContentType;

    @Size(max = 70)
    @Column(name = "titel", length = 70)
    private String titel;

    @ManyToOne
    private RetrospectiveType retrospectiveType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RetrospectiveItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public RetrospectiveItem content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getFile() {
        return this.file;
    }

    public RetrospectiveItem file(byte[] file) {
        this.setFile(file);
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public RetrospectiveItem fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getTitel() {
        return this.titel;
    }

    public RetrospectiveItem titel(String titel) {
        this.setTitel(titel);
        return this;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public RetrospectiveType getRetrospectiveType() {
        return this.retrospectiveType;
    }

    public void setRetrospectiveType(RetrospectiveType retrospectiveType) {
        this.retrospectiveType = retrospectiveType;
    }

    public RetrospectiveItem retrospectiveType(RetrospectiveType retrospectiveType) {
        this.setRetrospectiveType(retrospectiveType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RetrospectiveItem)) {
            return false;
        }
        return id != null && id.equals(((RetrospectiveItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RetrospectiveItem{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            ", titel='" + getTitel() + "'" +
            "}";
    }
}
