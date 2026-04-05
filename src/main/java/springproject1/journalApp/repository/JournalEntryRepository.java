package springproject1.journalApp.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import springproject1.journalApp.entity.JournalEntry;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    boolean existsByIdAndUserId(Long entryId, Long userId);
}
//controller ---->> service -->  repository