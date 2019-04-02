package com.bellini.recipecatalog.model.v1;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

//@Entity(name = "Publication")
//@Table(name = "PUBLICATION")
public class Publication {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "ID")
    private Long id;

    // @Column(name = "VOLUME")
    private Integer volume;

    // @Column(name = "YEAR")
    private Integer year;

    // @Column(name = "CREATION_TIME")
    private Instant creationTime = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    // @Column(name = "LAST_MODIFICATION_TIME")
    private Instant lastModificationTime = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime.truncatedTo(ChronoUnit.MILLIS);
    }

    public Instant getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(Instant lastModificationTime) {
        this.lastModificationTime = lastModificationTime.truncatedTo(ChronoUnit.MILLIS);
    }

}
