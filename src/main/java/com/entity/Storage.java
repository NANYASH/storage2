package com.entity;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "STORAGE")
public class Storage {
    private long id;
    private String formatsSupported;
    private String storageCountry;
    private long storageSize;
    private List<File> files;


    @SequenceGenerator(name = "STORAGE_SEQ", sequenceName = "STORAGE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STORAGE_SEQ")
    @Id
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "FORMATS_SUPPORTED")
    public String getFormatsSupported() {
        return formatsSupported;
    }

    public void setFormatsSupported(String formatsSupported) {
        this.formatsSupported = formatsSupported;
    }

    @Column(name = "STORAGE_COUNTRY")
    public String getStorageCountry() {
        return storageCountry;
    }

    public void setStorageCountry(String storageCountry) {
        this.storageCountry = storageCountry;
    }

    @Column(name = "STORAGE_SIZE")
    public long getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(long storageSize) {
        this.storageSize = storageSize;
    }

    @OneToMany(targetEntity = File.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "storage", orphanRemoval = false)
    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Storage storage = (Storage) o;

        if (id != storage.id) return false;
        if (storageSize != storage.storageSize) return false;
        if (formatsSupported != null ? !formatsSupported.equals(storage.formatsSupported) : storage.formatsSupported != null)
            return false;
        if (storageCountry != null ? !storageCountry.equals(storage.storageCountry) : storage.storageCountry != null)
            return false;
        return files != null ? files.equals(storage.files) : storage.files == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (formatsSupported != null ? formatsSupported.hashCode() : 0);
        result = 31 * result + (storageCountry != null ? storageCountry.hashCode() : 0);
        result = 31 * result + (int) (storageSize ^ (storageSize >>> 32));
        result = 31 * result + (files != null ? files.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", formatsSupported='" + formatsSupported + '\'' +
                ", storageCountry='" + storageCountry + '\'' +
                ", storageSize=" + storageSize +
                ", files=" + files +
                '}';
    }
}
