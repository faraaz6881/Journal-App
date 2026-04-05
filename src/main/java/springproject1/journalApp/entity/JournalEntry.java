package springproject1.journalApp.entity;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}