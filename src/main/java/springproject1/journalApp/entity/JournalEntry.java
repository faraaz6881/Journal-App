package springproject1.journalApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;


import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Table(name = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    private String content;

    private LocalDateTime date;
    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id",nullable =false)
    @JsonIgnore
    private User user;
}